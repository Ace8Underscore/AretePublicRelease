package com.cousinware.arete.module.world;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.events.event.RenderWorldEvent;
import com.cousinware.arete.managers.Render3DManager;
import com.cousinware.arete.managers.RotationManager;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.ColorUtils;
import com.cousinware.arete.utils.Timer;
import com.cousinware.arete.utils.game.BlockInteractionHelper;
import com.cousinware.arete.utils.game.InventoryUtils;
import com.cousinware.arete.utils.rotations.Rotation;
import com.cousinware.arete.utils.rotations.RotationSystem;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.IntSetting;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class AutoBuild extends Module implements Rotation {

    ArrayList<Structure> structures;
    Queue<BlockPos> placePoses = new LinkedList<>();
    Structure currentStructure;
    RotationSystem rotationSystem;
    BlockPos targetBlock = null;
    Timer timer = new Timer(40);
    BlockPos lightPos;
    InventoryUtils.InventoryTask swapTask = null;
    boolean lighting = false;

    ModeSetting selectedStructure = new ModeSetting();
    BoolSetting light = new BoolSetting();
    ModeSetting placeMode = new ModeSetting();
    IntSetting distance = new IntSetting();
    IntSetting placeDelay = new IntSetting();
    BoolSetting airPlace = new BoolSetting();
    BoolSetting swap = new BoolSetting();
    BoolSetting render = new BoolSetting();


    public AutoBuild() {
        super("AutoBuild", Category.World, -1);
        initStuctures();

        selectedStructure.setName("Structure").setValue(structures.getFirst().getName()).setModes(structures.stream().map(Structure::getName).toArray(String[]::new)).build(this).setChangeMode(() -> {
            light.setShown(selectedStructure.getValue().equalsIgnoreCase("portal"));
        });
        light.setName("Light").setValue(true).build(this);
        placeMode.setName("Place").setValue("Auto").setModes("Auto", "Direction", "CrossHair").build(this).setChangeMode(() -> {
            distance.setShown(!placeMode.getValue().equalsIgnoreCase("Crosshair"));
        });
        distance.setName("Distance").setMin(1).setMax(5).setValue(3).build(this).setDescription("Build Distance for Direction Place Mode");
        placeDelay.setName("PlaceDelay").setMin(0).setValue(40).setMax(200).build(this);
        airPlace.setName("AirPlace").setValue(true).build(this);
        swap.setName("Swap").setValue(true).build(this);
        render.setName("Render").setValue(true).build(this);
        setSaveToConfig(false);

    }

    public static Vec3i rotateY(Vec3i vec, Direction direction) {
        if (direction.equals(Direction.EAST)) {
            return new Vec3i(vec.getZ(), vec.getY(), vec.getX());
        } else if (direction.equals(Direction.WEST)) {
            return new Vec3i(vec.getZ(), vec.getY(), vec.getX());
        }//swapped x and y
        return new Vec3i(vec.getX(), vec.getY(), vec.getZ());
    }

    @Subscribe
    public void onRender(RenderWorldEvent event) {
        if (!render.getValue()) return;
        Iterator<BlockPos> iterator = placePoses.iterator();
        if (iterator.hasNext()) {
            BlockPos pos = iterator.next();

        }
    }

    public void onEnable() {
        genCurrentStructure();
        genPlacePoses();
        rotationSystem = new RotationSystem();
        timer = new Timer(placeDelay.getValue());
        lightPos = placePoses.peek();

    }

    public void onDisable() {
        targetBlock = null;
        placePoses.clear();
        currentStructure = null;
        lighting = false;
        rotationSystem.forceStop();
        swapTask = null;

    }

    public void onUpdate() {
        if (placePoses.isEmpty() && !selectedStructure.getValue().equalsIgnoreCase("portal")) {
            this.disable();
            return;
        }
        if (placePoses.isEmpty() && selectedStructure.getValue().equalsIgnoreCase("portal")) {
            InventoryUtils.InventoryTask lighterTask = new InventoryUtils.InventoryTask(Items.FLINT_AND_STEEL);
            lighterTask.swapItem();
            lighterTask.useItemCustom(() -> {
                if (!lighterTask.itemNotFound) {
                    lighting = true;
                    RotationManager.Rotation rotation = AreteClient.rotationManager.genRotation(lightPos.toCenterPos());
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(rotation.getYaw(), rotation.getPitch(), mc.player.isOnGround(), mc.player.horizontalCollision));
                    mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(lightPos.toCenterPos(), Direction.UP, lightPos, true), 0));
                }
            });
            lighterTask.swapItem();
            this.disable();
            return;
        }

        if (!timer.canTick()) return;

        BlockPos pos = placePoses.poll();


        RotationManager.Rotation rotation = airPlace.getValue() ? BlockInteractionHelper.placeBlockInAir(pos, false) : BlockInteractionHelper.placeBlock(pos, pos.toCenterPos());

        //cant place? or no rotation found we stop
        if (rotation == null) return;
        rotationSystem.setRotation(rotation, true);
        if (render.getValue()) {
            AreteClient.render3DManager.postRender(Render3DManager.RenderMode.BlockOutline, new Box(pos), pos.toCenterPos(), ColorUtils.convertAlpha(AreteClient.getClientColor(), 255), "AutoBuildOutline" + pos, 500f);
            AreteClient.render3DManager.postRender(Render3DManager.RenderMode.SolidBlock, new Box(pos), pos.toCenterPos(), ColorUtils.convertAlpha(AreteClient.getClientColor(), 125), "AutoBuildSolid" + pos, 500f);
        }
    }

    @Override
    @Subscribe
    public void packetListener(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet) {
            rotationSystem.modifyPacket(packet);
            rotationSystem.setAction(RotationSystem.ACTION.Nothing);

        }
    }

    @Subscribe
    public void sendPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {
            if (swap.getValue() && !lighting) {
                if (selectedStructure.getValue().equalsIgnoreCase("Wither")) {
                    if (placePoses.size() < 3) swapTask = new InventoryUtils.InventoryTask(Items.WITHER_SKELETON_SKULL);
                    else swapTask = new InventoryUtils.InventoryTask(Items.SOUL_SAND);
                    swapTask.swapItem();
                } else if (!isHoldingBlockItem()) {
                    swapTask = new InventoryUtils.InventoryTask(Items.OBSIDIAN);
                    swapTask.swapItem();
                }

            }
            if (airPlace.getValue()) {
                mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
                packet.hand = Hand.OFF_HAND;
            }

        }
    }

    @Subscribe
    public void sendPacketPost(PacketEvent.Send.Post event) {

        if (event.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {

            if (airPlace.getValue())
                mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
            if (swap.getValue() && !lighting && swapTask != null) {
                swapTask.swapItem();
                swapTask = null;
            }
        }
    }

    public boolean isHoldingBlockItem() {
        return mc.player.getMainHandStack().getItem() instanceof BlockItem;
    }

    public void genCurrentStructure() {
        for (Structure struct : structures) {
            if (selectedStructure.getValue().equalsIgnoreCase(struct.getName())) currentStructure = struct;
        }
    }

    public void genPlacePoses() {
        Direction lookingDirection = com.cousinware.arete.utils.game.PlayerUtils.getLoookingDirection();
        BlockPos pos;
        HitResult crossHair = mc.crosshairTarget;

        if (placeMode.getValue().equalsIgnoreCase("direction")) {
            pos = mc.player.getBlockPos().add(lookingDirection.getVector().multiply(distance.getValue()));
        } else if (placeMode.getValue().equalsIgnoreCase("crossHair")) {
            if (crossHair.getPos() == null) {
                //couldnt find palce pos
                disable();
                return;
            } else {
                pos = BlockInteractionHelper.vec3dToPos(crossHair.getPos());
            }
        } else {
            //Auto
            if (crossHair.getType() == HitResult.Type.BLOCK) {
                pos = BlockInteractionHelper.vec3dToPos(crossHair.getPos());


            } else {
                pos = mc.player.getBlockPos().add(lookingDirection.getVector().multiply(distance.getValue()));
            }
        }

        for (int i = 0; i < currentStructure.vec3is.length; i++) {
            Vec3i rotatedVec = rotateY(currentStructure.vec3is[i], lookingDirection);
            placePoses.add(pos.add(rotatedVec));
        }


    }

    public void initStuctures() {
        if (structures != null) return;
        structures = new ArrayList<>();
        structures.add(new Structure("Omega", new Vec3i[]{
                new Vec3i(-2, 0, 0),
                new Vec3i(2, 0, 0),
                new Vec3i(-1, 0, 0),
                new Vec3i(1, 0, 0),
                new Vec3i(-1, 1, 0),
                new Vec3i(1, 1, 0),
                new Vec3i(-2, 2, 0),
                new Vec3i(2, 2, 0),
                new Vec3i(-2, 3, 0),
                new Vec3i(2, 3, 0),
                new Vec3i(-2, 4, 0),
                new Vec3i(2, 4, 0),
                new Vec3i(-1, 5, 0),
                new Vec3i(1, 5, 0),
                new Vec3i(0, 5, 0),


        }));

        structures.add(new Structure("Penis", new Vec3i[]{new Vec3i(-1, 0, 0), new Vec3i(0, 0, 0), new Vec3i(1, 0, 0), new Vec3i(0, 1, 0), new Vec3i(0, 2, 0)}));
        structures.add(new Structure("Portal", new Vec3i[]{new Vec3i(-1, 0, 0), new Vec3i(0, 0, 0), new Vec3i(-2, 1, 0), new Vec3i(-2, 2, 0), new Vec3i(-2, 3, 0), new Vec3i(-1, 4, 0), new Vec3i(0, 4, 0), new Vec3i(1, 3, 0), new Vec3i(1, 2, 0), new Vec3i(1, 1, 0)}));
        structures.add(new Structure("Wither", new Vec3i[]{new Vec3i(0, 0, 0), new Vec3i(0, 1, 0), new Vec3i(-1, 1, 0), new Vec3i(1, 1, 0), new Vec3i(0, 2, 0), new Vec3i(-1, 2, 0), new Vec3i(1, 2, 0)}));
    }


    @AllArgsConstructor
    @Getter
    public static class Structure {
        String name;
        Vec3i[] vec3is;


    }
}
