package com.cousinware.arete.module.player;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.managers.RotationManager;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.BlockInteractionHelper;
import com.cousinware.arete.utils.InventoryUtils;
import com.cousinware.arete.utils.RotationSystem;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.SlotChangedStateC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class Scaffold extends Module {

    ModeSetting rotaionsMode = new ModeSetting();
    BoolSetting fakeBlocks = new BoolSetting();

    public Scaffold() {
        super("Scaffold", Category.Player, -1, "description");
        rotaionsMode.setName("Rotation").setModes("None", "Old", "New").setValue("New").build(this);
        fakeBlocks.setName("FastSwap").setValue(true).build(this);
        enablePriority(7);
    }

    int itemSlot = -1;
    int currentSlot = -1;
    RotationManager.Rotaion rotaion = null;
    int swapHands = -1;
    boolean doSwap = false;
    int serverSideHand = -1;
    int delay = 0;

//    public void onUpdate() {
//        delay++;
//        currentSlot = mc.player.getInventory().selectedSlot;
//
//
//        itemSlot = InventoryUtils.getFirstBlockInHotBar();
//        if (itemSlot == -1) return;
//        //movement fix for new rotations
//        rotationSystem.updateMoveFix(rotaionsMode);
//        //mc.player.serverPitch = 2;
//
//        //mc.player.setYaw(mc.player.getBodyYaw() + .0005f);
//        //mc.player.setMovementSpeed(mc.player.getMovementSpeed() * .08f);
//        //check to see if we can place
//        BlockPos pos = mc.player.getBlockPos().down();
//        if (!mc.world.getBlockState(pos).isAir()) {
//            //if (serverSideHand != mc.player.getInventory().selectedSlot && !doSwap) InventoryUtils.swapToItem(mc.player.getInventory().selectedSlot, true);
//            return;
//        }
//        //check to see if we even have a block in the hot bar
//
//
//        //Vec3d vec3d = new Vec3d(pos.getX() + .5f, (int)pos.getY(), pos.getZ() + .5f);
//        if (delay < 2) return;
//        //rotaion = BlockInteractionHelper.placeBlockRecursive(pos, -1);
//        if (rotaion != null && rotaionsMode.getValue().equalsIgnoreCase("new")) {
//            //swapHands(itemSlot);
//            rotationSystem.setCurrentAction(RotationSystem.ACTION.Rotating);
//            rotationSystem.updateRotation(rotaion);
//            //TODO Fix desync issues
//            if (fakeBlocks.getValue() && mc.player.isOnGround()) mc.world.setBlockState(pos, ((BlockItem)mc.player.getInventory().getStack(itemSlot).getItem()).getBlock().getDefaultState());
//        } else if (rotaion != null && rotaionsMode.getValue().equalsIgnoreCase("old")) {
//            //swapHands(itemSlot);
//            rotationSystem.setCurrentAction(RotationSystem.ACTION.Rotating);
//            rotationSystem.updateRotation(rotaion);
//            //we set the position where the block will placed so we dont packet spam as we wait for the server to verify a block is there
//            if (fakeBlocks.getValue() && mc.player.isOnGround()) mc.world.setBlockState(pos, ((BlockItem)mc.player.getInventory().getStack(itemSlot).getItem()).getBlock().getDefaultState());
//        } else if (rotaion != null && rotaionsMode.getValue().equalsIgnoreCase("none")) {
//            swapHands(itemSlot);
//            BlockInteractionHelper.placeBlockFix(rotaion);
//            swapHandsBack(itemSlot);
//            if (fakeBlocks.getValue() && mc.player.isOnGround() && !mc.player.getInventory().getMainHandStack().getItem().equals(Items.AIR)) mc.world.setBlockState(pos, ((BlockItem)mc.player.getInventory().getStack(itemSlot).getItem()).getBlock().getDefaultState());
//            //ANTI FAKE BLOCKS fuk u
//            BlockHitResult blockHitResult = new BlockHitResult(pos.toCenterPos(), Direction.UP, pos, false);
//            MinecraftClient.getInstance().getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, blockHitResult, 1));
//        }
//
//
//        delay = 0;
//        mc.player.swingHand(Hand.MAIN_HAND);
//
//
//    }

    public void swapHands(int itemSlot) {
        if (itemSlot != -1) {
            swapHands = InventoryUtils.swapToItem(itemSlot, true);
            Command.sendClientSideMessage("swap hand");
        }
    }

    public void swapHandsBack(int itemSlot) {
        if (itemSlot != mc.player.getInventory().selectedSlot) {
            InventoryUtils.swapToItem(swapHands, true);
        }
    }

    public void onEnable() {
        rotationSystem = new RotationSystem();

    }

    public void onDisable() {
        rotationSystem.stop();

    }


    @Override
    public void onPostUpdate() {
        //AreteClient.rotationManager.fixRotationMovement(mc.player.getYaw(), mc.player.getPitch());
        //rotaion = null;
    }

    @Subscribe
    public void inComingPackets(PacketEvent.Send event) {
        if (event.getPacket() instanceof SlotChangedStateC2SPacket) {
            serverSideHand = ((SlotChangedStateC2SPacket) event.getPacket()).slotId();
        }

        if (!rotationSystem.getCurrentAction().equals(RotationSystem.ACTION.Rotating) || rotaionsMode.getValue().equalsIgnoreCase("None")) return;
        if (event.getPacket() instanceof PlayerMoveC2SPacket) {
            if (!AreteClient.packetManager.shouldModifyPacket(this)) {
                Command.sendClientSideMessage("Not fixing packet");
                rotationSystem.setCurrentAction(RotationSystem.ACTION.Nothing);
                return;
            }
            //TODO flags bad packets(no penalty via grim)
            swapHands(itemSlot);



            ((PlayerMoveC2SPacket) event.getPacket()).yaw = rotationSystem.getYaw();
            ((PlayerMoveC2SPacket) event.getPacket()).pitch = rotationSystem.getPitch();

            //BlockInteractionHelper.placeBlockFix(rotationSystem.getRotaion());
            swapHandsBack(mc.player.getInventory().selectedSlot);


            Command.sendClientSideMessage("Rotation Modified");
            rotationSystem.setCurrentAction(RotationSystem.ACTION.Nothing);



        }
        //Command.sendClientSideMessage(event.getPacket());
    }




//    @Subscribe
//    public void inComingPackets(PacketEvent.Send event) {
//        if (target == null || !rotating || !rotaionsMode.getValue().equalsIgnoreCase("packet")) return;
//        if (event.getPacket() instanceof PlayerMoveC2SPacket) {
//            float yaw = (float) AreteClient.rotationManager.getYaw(target.getEyePos(), true);
//            float pitch = (float) AreteClient.rotationManager.getPitch(target.getEyePos(), true);
//            ((PlayerMoveC2SPacket) event.getPacket()).yaw = yaw;
//            ((PlayerMoveC2SPacket) event.getPacket()).pitch = pitch;
//            AreteClient.rotationManager.rotateBody(yaw, pitch);
//            Command.sendClientSideMessage("fixing packet");
//            rotating = false;
//            //mc.player.setYaw(yaw);
//            //mc.player.setPitch(pitch);
//
//
//
//        }
//        //Command.sendClientSideMessage(event.getPacket());
//    }




}
