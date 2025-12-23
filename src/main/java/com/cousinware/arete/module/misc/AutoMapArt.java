package com.cousinware.arete.module.misc;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.EntitySpawnEvent;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.events.event.PlayerPlaceBlockEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.game.InventoryUtils;
import com.cousinware.arete.utils.rotations.Rotation;
import com.cousinware.arete.utils.rotations.RotationSystem;
import com.cousinware.arete.utils.settings.IntSetting;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class AutoMapArt extends Module implements Rotation {

    IntSetting serverDelay = new IntSetting();
    static RotationSystem rotationSystem;
    ModeSetting mode = new ModeSetting();
    AutoMapArtToggle autoMapArtToggle;
    AutoMapArtPlace autoMapArtPlace;

    public AutoMapArt() {
        super("AutoMapPlace", Category.Misc, -1, "Places maparts and item frames together");
        serverDelay.setName("Delay").setValue(3).setMin(1).setMax(20).setDescription("You need to have a delay to place map in item frame").build(this);
        mode.setName("Mode").setValue("Place").setModes("Place", "Toggle").setDescription("Toggle will automatically place item frame and mapart. Whereas Place will put a map art into an item frame when placed").build(this);
        mode.setChangeMode(() -> {
            if (mode.getValue().equalsIgnoreCase("toggle")) {
                autoMapArtToggle.onEnable();
                autoMapArtToggle.onDisable();
            } else if (mode.getValue().equalsIgnoreCase("place")) {
                autoMapArtPlace.onEnable();
                autoMapArtPlace.onDisable();
            }
        });
        setSaveToConfig(false);
        autoMapArtToggle = new AutoMapArtToggle();
        autoMapArtPlace = new AutoMapArtPlace();
    }

    public void onUpdate() {
        if (mode.getValue().equalsIgnoreCase("toggle")) autoMapArtToggle.onUpdate();
        else if (mode.getValue().equalsIgnoreCase("place")) autoMapArtPlace.onUpdate();

    }

    public void onEnable() {
        if (mode.getValue().equalsIgnoreCase("toggle")) autoMapArtToggle.onEnable();
        else if (mode.getValue().equalsIgnoreCase("place")) autoMapArtPlace.onEnable();

    }

    public void onDisable() {
        if (mode.getValue().equalsIgnoreCase("toggle")) autoMapArtToggle.onDisable();
        else if (mode.getValue().equalsIgnoreCase("place")) autoMapArtPlace.onDisable();

    }


    @Subscribe
    public void packetListener(PacketEvent.Send event) {
        if (!(event.getPacket() instanceof PlayerMoveC2SPacket)) return;
        if (rotationSystem.getAction().equals(RotationSystem.ACTION.Rotating)) {
            rotationSystem.modifyPacket(((PlayerMoveC2SPacket) event.getPacket()));
            rotationSystem.setAction(RotationSystem.ACTION.Nothing);
        }

    }


    class AutoMapArtPlace {

        PlayerPlaceBlockEvent placedBlock = null;
        Entity itemFrameEntity = null;
        InventoryUtils.HotBarTask map;


        public void onUpdate() {
            if (placedBlock != null && itemFrameEntity != null) {
                Vec3d placePos = placedBlock.getHitResult().getPos();
                if (placePos.distanceTo(itemFrameEntity.getPos()) > 1.5 || placePos.distanceTo(mc.player.getPos()) > 7)
                    return;

                map = new InventoryUtils.HotBarTask(true, Items.FILLED_MAP);
                if (map.isItemNotFound()) {
                    placedBlock = null;
                    itemFrameEntity = null;
                    return;
                }

                rotationSystem.rotate(placePos, true);
                place(placePos, itemFrameEntity);

            }
        }

        public void onEnable() {
            AreteClient.eventBus.register(this);
            rotationSystem = new RotationSystem();
        }

        public void onDisable() {
            AreteClient.eventBus.unregister(this);
            rotationSystem.forceStop();
        }

        public void place(Vec3d pos, Entity entity) {
            mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.interact(entity, false, Hand.MAIN_HAND));
            mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.interactAt(entity, false, Hand.MAIN_HAND, pos));
            mc.player.swingHand(Hand.MAIN_HAND);
            map.swapBack(true);

            placedBlock = null;
            itemFrameEntity = null;

        }

        @Subscribe
        public void entitySpawn(EntitySpawnEvent event) {
            if (event.getEntity() instanceof ItemFrameEntity || event.getEntity() instanceof GlowItemFrameEntity) {
                itemFrameEntity = event.getEntity();
            }
        }

        @Subscribe
        public void itemFramePlace(PlayerPlaceBlockEvent event) {
            if (event.getItem().getItem().equals(Items.ITEM_FRAME) || event.getItem().getItem().equals(Items.GLOW_ITEM_FRAME)) {
                placedBlock = event;
            }
        }

    }


    class AutoMapArtToggle {
        PlaceMode mode = PlaceMode.ItemFrame;
        HitResult placeResult;
        int delay = 0;
        int placeDelay = 0;
        InventoryUtils.HotBarTask map;

        public void onEnable() {
            rotationSystem = new RotationSystem();
            placeResult = mc.crosshairTarget;
            InventoryUtils.HotBarTask itemFrame = new InventoryUtils.HotBarTask(true, Items.ITEM_FRAME, Items.GLOW_ITEM_FRAME);
            if (itemFrame.isItemNotFound()) {
                disable();
                itemFrame.swapBack(true);
                return;
            }
            mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, (BlockHitResult) placeResult, 0));
            //itemFrame.swapBack(true);
            mode = PlaceMode.PlaceMap;

        }

        public void onDisable() {
            placeResult = null;
            mode = PlaceMode.ItemFrame;
            delay = 0;
            placeDelay = 0;
            map = null;
            rotationSystem.forceStop();
        }

        public void onUpdate() {
            delay++;
            if (placeResult == null && mode.equals(PlaceMode.PlaceMap)) {
                disable();
                return;
            }
            Entity closestItemFrame = getClosestItemFrame();
            if (getClosestItemFrame() == null && mc.targetedEntity == null && delay > 5) {
                disable();
                return;
            }
            if (map == null) map = new InventoryUtils.HotBarTask(true, Items.FILLED_MAP);
            if (map.isItemNotFound()) {
                disable();
                return;
            }
            if (closestItemFrame != null || mc.targetedEntity != null) {
                place(serverDelay.getValue(), closestItemFrame, map);
            }

        }

        public void place(int tickDelay, Entity closestItemFrame, InventoryUtils.HotBarTask map) {
            placeDelay++;
            Entity useEntity = mc.targetedEntity != null ? mc.targetedEntity : closestItemFrame;
            if (placeDelay == tickDelay - 1) {
                rotationSystem.rotate(useEntity.getPos(), true);
            }
            if (placeDelay > tickDelay) {

                //mc.interactionManager.interactEntity(mc.player, useEntity, Hand.MAIN_HAND);
                mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.interact(useEntity, false, Hand.MAIN_HAND));
                mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.interactAt(useEntity, false, Hand.MAIN_HAND, useEntity.getPos()));
                //mc.interactionManager.interactEntityAtLocation(mc.player, useEntity, new EntityHitResult(useEntity, useEntity.getPos()), Hand.MAIN_HAND);
                mc.player.swingHand(Hand.MAIN_HAND);
                map.swapBack(true);
                disable();
            }
        }

        @Nullable
        public Entity getClosestItemFrame() {
            Entity closestEntity = null;
            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof ItemFrameEntity || entity instanceof GlowItemFrameEntity) {
                    if ((entity instanceof ItemFrameEntity) && !((ItemFrameEntity) entity).getHeldItemStack().isEmpty())
                        continue;
                    if ((entity instanceof GlowItemFrameEntity) && !((GlowItemFrameEntity) entity).getHeldItemStack().isEmpty())
                        continue;

                    if (closestEntity == null) closestEntity = entity;
                    else if (mc.crosshairTarget.squaredDistanceTo(entity) < mc.crosshairTarget.squaredDistanceTo(closestEntity)) {
                        closestEntity = entity;
                    }
                }
            }
            //if (closestEntity != null && mc.crosshairTarget.squaredDistanceTo(closestEntity) > 7) closestEntity = null;
            return closestEntity;
        }


        public enum PlaceMode {
            ItemFrame,
            PlaceMap
        }

    }
}