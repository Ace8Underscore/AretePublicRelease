package com.cousinware.arete.module.player;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.DoubleSetting;
import com.cousinware.arete.utils.settings.ModeSetting;
import net.minecraft.client.Mouse;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class AutoClicker extends Module {

    BoolSetting onlyInContainer = new BoolSetting();
    DoubleSetting sDelay = new DoubleSetting();
    ModeSetting button = new ModeSetting();
    ModeSetting swing = new ModeSetting();
    DoubleSetting range = new DoubleSetting();
    Mouse mouse = new Mouse(mc);
    int delay = 0;

    public AutoClicker() {
        super("AutoClicker", Category.Player, 5989507);

        sDelay.setName("Delay").setValue(1).setMin(0).setMax(60).setDescription("Delay In Second").build(this);
        button.setName("Button").setValue("MB1").setModes("MB1", "MB2").build(this);
        swing.setName("Type").setValue("Attack").setModes("Swing", "Click", "Attack").build(this);
        range.setName("Range").setValue(3).setMin(0).setMax(10).build(this);
        onlyInContainer.setName("OnlyGui").setValue(false).build(this);


    }

    public void onUpdate() {
        if (mc.currentScreen == AreteClient.newGui) return;
        if (onlyInContainer.getValue() && mc.currentScreen == null) return;
        delay++;
        if (delay >= sDelay.getValue() * 20) {

            if (swing.getValue().equalsIgnoreCase("click")) doClick();
            else if (swing.getValue().equalsIgnoreCase("Swing")) doSwing();
            else doAttack();
            delay = 0;
        }

    }

    public void doAttack() {
        HitResult hit = mc.crosshairTarget;
        //System.out.println(hit.getType());
        BlockPos pos = new BlockPos((int) hit.getPos().getX(), (int) hit.getPos().getY(), (int) hit.getPos().getZ());

        if (hit.getType().equals(HitResult.Type.ENTITY)) {
            Entity entity = findClosestEntityToBlock(pos, hit);
            if (entity != null) {
                //System.out.println(entity);
                mc.interactionManager.attackEntity(mc.player, entity);
            }
        }
        if (hit.getType().equals(HitResult.Type.BLOCK)) {
            Entity entity = findClosestEntityToBlock(pos, hit);
            if (entity != null) {
                //System.out.println(entity);
                mc.interactionManager.attackEntity(mc.player, entity);
            }
        } else {
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    public void doSwing() {

        //if (mc.player.hit)
        HitResult hit = mc.crosshairTarget;
        BlockPos pos = new BlockPos((int) hit.getPos().getX(), (int) hit.getPos().getY(), (int) hit.getPos().getZ());

        if (hit.getType().equals(HitResult.Type.ENTITY)) {
            Entity entity = findClosestEntityToBlock(pos, hit);
            if (entity != null) {
                mc.interactionManager.attackEntity(mc.player, entity);
            }
        }
        if (hit.getType().equals(HitResult.Type.BLOCK)) {
            mc.interactionManager.attackBlock(pos, Direction.UP);
            mc.player.swingHand(Hand.MAIN_HAND);
        } else {
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    public Entity findClosestEntityToBlock(BlockPos targetBlockPos, HitResult hitResult) {
        double closestDistance = Double.MAX_VALUE;
        Entity closestEntity = null;

        for (Entity entity : mc.world.getEntities()) {
            if (entity != mc.world.getEntityById(mc.player.getId()) && mc.player.distanceTo(entity) <= range.getValue() && entity.canHit() && !(entity instanceof VexEntity)) {
                Vec3d entityPos = new Vec3d(entity.getX(), entity.getY(), entity.getZ());
                double distance = hitResult.squaredDistanceTo(entity);


                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestEntity = entity;
                }
            }
        }

        return closestEntity;
    }

    public void doClick() {
        int num = getNumba();
        mouse.onMouseButton(mc.getWindow().getHandle(), num, 1, 0);
        //used to stop holding button down
        mouse.onMouseButton(mc.getWindow().getHandle(), num, 0, 0);
        delay = 0;

    }

    public int getNumba() {
        if (button.getValue().equals("MB2")) {
            return 1;
        }
        return 0;

    }

    public boolean canHit(Entity entity) {
        return !(entity instanceof EvokerFangsEntity) && !(entity instanceof ExperienceOrbEntity) && !(entity instanceof ItemEntity) && !(entity instanceof EnderPearlEntity);
    }
}
