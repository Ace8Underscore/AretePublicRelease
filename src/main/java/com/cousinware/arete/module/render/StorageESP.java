package com.cousinware.arete.module.render;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RenderWorldEvent;
import com.cousinware.arete.managers.Render3DManager;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.game.EntityUtil;
import com.cousinware.arete.utils.settings.BoolSettingContainer;
import com.cousinware.arete.utils.settings.DoubleSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.entity.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

import java.awt.*;

public class StorageESP extends Module {


    public static BoolSettingContainer chest = new BoolSettingContainer();
    public static DoubleSetting chestRange = new DoubleSetting();
    public static BoolSettingContainer eChest = new BoolSettingContainer();
    public static DoubleSetting eChestRange = new DoubleSetting();
    public static BoolSettingContainer shulker = new BoolSettingContainer();
    public static DoubleSetting shulkerRange = new DoubleSetting();
    public static BoolSettingContainer barrel = new BoolSettingContainer();
    public static DoubleSetting barrelRange = new DoubleSetting();


    public StorageESP() {
        super("StorageESP", Category.Render, -1, "");

        chest.setValue(true).setName("Chest").build(this);
        chestRange.setName("Range").setMin(0).setMax(500).setValue(125).build(this, chest, "ChestRange");
        eChest.setValue(true).setName("EChest").build(this);
        eChestRange.setName("Range").setMin(0).setMax(500).setValue(125).build(this, eChest, "EChestRange");
        shulker.setValue(true).setName("Shulker").build(this);
        shulkerRange.setName("Range").setMin(0).setMax(500).setValue(125).build(this, shulker, "ShulkerRange");
        barrel.setValue(true).setName("Barrel").build(this);
        barrelRange.setName("Range").setMin(0).setMax(500).setValue(125).build(this, barrel, "BarrelRange");
    }

    public static Box getBlockBoundingBox(BlockEntity blockEntity) {
        if (blockEntity instanceof ShulkerBoxBlockEntity)
            return new Box(blockEntity.getPos().getX(), blockEntity.getPos().getY(), blockEntity.getPos().getZ(), blockEntity.getPos().getX() + 1, blockEntity.getPos().getY() + 1, blockEntity.getPos().getZ() + 1);
        BlockPos block = blockEntity.getPos();
        VoxelShape voxelShape = mc.world.getBlockState(blockEntity.getPos()).getCollisionShape(mc.world, blockEntity.getPos());
        Box boundBoxPre = voxelShape.getBoundingBox();
        return new Box(block.getX() + boundBoxPre.minX, block.getY() + boundBoxPre.minY, block.getZ() + boundBoxPre.minZ, block.getX() + boundBoxPre.maxX, block.getY() + boundBoxPre.maxY, block.getZ() + boundBoxPre.maxZ);

    }

    @Subscribe
    public void renderWorld(RenderWorldEvent event) {

        //! debug stuff kinda cool


        BlockPos pos = mc.player.getBlockPos().down();

        for (BlockEntity blockEntity : EntityUtil.getBlockEntities()) {

            if (!eChest.getValue() && blockEntity instanceof EnderChestBlockEntity || !chest.getValue() && blockEntity instanceof ChestBlockEntity || !shulker.getValue() && blockEntity instanceof ShulkerBoxBlockEntity || !barrel.getValue() && blockEntity instanceof BarrelBlockEntity)
                continue;
            if (withinRange(blockEntity))
                if (isChest(blockEntity)) {

                    Color color = getColor(blockEntity);

                    Box bb = getBlockBoundingBox(blockEntity);
                    AreteClient.render3DManager.postRender(Render3DManager.RenderMode.SolidBlock, bb, blockEntity.getPos().toCenterPos(), event, new Color(color.getRed(), color.getGreen(), color.getBlue(), 125), "StorageESPSolid", 500);
                    AreteClient.render3DManager.postRender(Render3DManager.RenderMode.BlockOutline, bb, blockEntity.getPos().toCenterPos(), event, color, "StorageESPOutline", 500);
                }
        }
    }

    private Color getColor(BlockEntity blockEntity) {
        if (blockEntity instanceof ChestBlockEntity) return Color.ORANGE;
        else if (blockEntity instanceof BarrelBlockEntity) return Color.GRAY;
        else if (blockEntity instanceof EnderChestBlockEntity) return Color.MAGENTA;
        else if (blockEntity instanceof ShulkerBoxBlockEntity) return Color.CYAN;
        return Color.WHITE;
    }

    private boolean withinRange(BlockEntity entity) {
        double distance = Math.sqrt(mc.player.squaredDistanceTo(entity.getPos().toCenterPos()));
        if (entity instanceof EnderChestBlockEntity && distance <= eChestRange.getValue()) return true;
        else if (entity instanceof ChestBlockEntity && distance <= chestRange.getValue()) return true;
        else if (entity instanceof ShulkerBoxBlockEntity && distance <= shulkerRange.getValue()) return true;
        else return entity instanceof BarrelBlockEntity && distance <= barrelRange.getValue();
    }

    private boolean isChest(BlockEntity blockEntity) {
        return (blockEntity instanceof ChestBlockEntity || blockEntity instanceof BarrelBlockEntity || blockEntity instanceof EnderChestBlockEntity || blockEntity instanceof ShulkerBoxBlockEntity || blockEntity instanceof PistonBlockEntity);
    }


}
