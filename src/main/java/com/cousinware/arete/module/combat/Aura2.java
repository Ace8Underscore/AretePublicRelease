package com.cousinware.arete.module.combat;


import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.events.event.RenderWorldEvent;
import com.cousinware.arete.managers.Render3DManager;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.ColorUtils;
import com.cousinware.arete.utils.game.InventoryUtils;
import com.cousinware.arete.utils.rendering.nvg.string.coloredstring.ColoredString;
import com.cousinware.arete.utils.rotations.Rotation;
import com.cousinware.arete.utils.rotations.RotationSystem;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.BoolSettingContainer;
import com.cousinware.arete.utils.settings.DoubleSetting;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/*
New Aura to use with my new rewritten rotation system
 */
public class Aura2 extends Module implements Rotation {

    DoubleSetting range = new DoubleSetting();
    BoolSettingContainer targets = new BoolSettingContainer();
    BoolSetting players = new BoolSetting();
    ModeSetting attackMode = new ModeSetting();
    ModeSetting rotaionsMode = new ModeSetting();
    BoolSetting passive = new BoolSetting();
    BoolSetting hostile = new BoolSetting();
    BoolSetting neutral = new BoolSetting();
    BoolSetting autoSwitch = new BoolSetting();
    RotationSystem rotationSystem;
    InventoryUtils.HotBarTask swap = null;

    public Aura2() {
        super("Aura", Category.Combat, -1, "");
        range.setName("Range").setMin(1).setMax(8).setValue(3.3f).build(this);
        attackMode.setName("Mode").setModes("Switch", "Nodelay").setValue("Switch").build(this);
        rotaionsMode.setName("Rotation").setModes("None", "Old", "Sim").setValue("Sim").build(this);
        targets.setName("Targets").setValue(true).build(this);
        players.setName("Players").setValue(true).build(this, targets, "AuraTargetPlayers");
        passive.setName("Passive").setValue(true).build(this, targets, "AuraTargetPassive");
        hostile.setName("Hostile").setValue(true).build(this, targets, "AuraTargetHostile");
        neutral.setName("Neutral").setValue(false).build(this, targets, "AuraTargetNeutral");
        autoSwitch.setName("AutoSwitch").setValue(false).build(this).setDescription("Currently broken :(");

        rotationSystem = new RotationSystem();
    }

    Entity target;
    int delay = 0;
    boolean attack = false;

    @Override
    public void onUpdate() {

        if (attack) {
            attack();
            attack = false;
        }

        delay++;
        //we search for a new target every tick to find best option
        target = getTarget();
        //if no valid target is found we get null so then we return
        if (target == null) {
            if (swap != null && autoSwitch.getValue()) {
                swap.swapBack(false);
                swap = null;
            }
            return;
        }

        //target doesnt equal null so we swap to sword if we need to; Method verifies if we're holding sword or not
        if (autoSwitch.getValue()) {
            boolean shouldCancel = swapToSword();
            System.out.println(shouldCancel);
            //wait a tick so client doesnt swing when cooldown is reset t
            if (shouldCancel) return;
        }
        //rotate!

        //attack!
        // i think we need to attack post tick after we look but ima try reverse rq
        //! TRUEEEE We rotate then next tick we attack!
        if (attackMode.getValue().equalsIgnoreCase("switch")) switchAttack();
        else attack();


    }

    public void onEnable() {
        attack = false;
    }

    public void onDisable() {
        rotationSystem.safeStop();
    }


    public static Vec3d getLerpPos(Entity entity, float partialTicks) {
        double x = MathHelper.lerp(partialTicks, entity.getX(), entity.getX());
        double y = MathHelper.lerp(partialTicks, entity.getY(), entity.getY());
        double z = MathHelper.lerp(partialTicks, entity.getZ(), entity.getZ());
        return new Vec3d(x, y, z);
    }

    public boolean swapToSword() {
        if (!isHoldingSword()) {
            swap = new InventoryUtils.HotBarTask(false, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.WOODEN_SWORD);
            return true;
        }
        return false;
    }

    public boolean isHoldingSword() {
        Item currentItem = mc.player.getStackInHand(Hand.MAIN_HAND).getItem();
        return currentItem.equals(Items.DIAMOND_SWORD) || currentItem.equals(Items.NETHERITE_SWORD) || currentItem.equals(Items.STONE_SWORD) || currentItem.equals(Items.IRON_SWORD) || currentItem.equals(Items.GOLDEN_SWORD) || currentItem.equals(Items.WOODEN_SWORD);
    }

    public void switchAttack() {
        int totalWaitTime = (int) (mc.player.getAttackCooldownProgressPerTick());


        if (mc.player.getAttackCooldownProgress(mc.player.handSwingTicks) == 1) {
            if (!rotaionsMode.getValue().equalsIgnoreCase("none")) {
                rotationSystem.rotate(target.getEyePos(), !mc.player.isGliding() && rotaionsMode.getValue().equalsIgnoreCase("sim"));
            }
            delay = 0;
            attack = true;


        }
    }

    @Subscribe
    @Override
    public void packetListener(PacketEvent.Send event) {
        if (!(event.getPacket() instanceof PlayerMoveC2SPacket)) return;
        if (rotationSystem.getAction().equals(RotationSystem.ACTION.Rotating)) {
            rotationSystem.modifyPacket(((PlayerMoveC2SPacket) event.getPacket()));
            rotationSystem.setAction(RotationSystem.ACTION.Nothing);
        }
    }

    @Subscribe
    public void worldRender(RenderWorldEvent event) {
        if (target == null) return;
        //Box bb = new Box(target.getX() + target.getBoundingBox().minX, target.getY() + target.getBoundingBox().minY, target.getZ() + target.getBoundingBox().minZ, target.getX() + target.getBoundingBox().maxX, target.getY() + target.getBoundingBox().maxY, target.getZ() + target.getBoundingBox().maxZ);
        AreteClient.render3DManager.postRender(Render3DManager.RenderMode.SolidBlock, target, event, ColorUtils.convertAlpha(AreteClient.getClientColor(), 125), "AuraHighlightSolid", 500f);
        AreteClient.render3DManager.postRender(Render3DManager.RenderMode.BlockOutline, target, event, ColorUtils.convertAlpha(AreteClient.getClientColor(), 255), "AuraHighlightOutline", 500f);
        //Renderer.drawSolidBoxTEST(target.getBoundingBox(), event, ColorUtils.convertAlpha(AreteClient.getClientColor(), 125), getLerpPos(target, partialTicks));

    }

    public void attack() {
        mc.player.setSprinting(false);
        mc.interactionManager.attackEntity(mc.player, target);
        //PlayerInteractEntityC2SPacket attack = PlayerInteractEntityC2SPacket.attack(target, false);
        // mc.getNetworkHandler().sendPacket(attack);
        mc.player.swingHand(Hand.MAIN_HAND);
        //rotationSystem.setAction(RotationSystem2.ACTION.Nothing);   \
    }


    @Nullable
    public Entity getTarget() {
        Entity target = null;
        for (Entity entity : mc.world.getEntities()) {
            if (!players.getValue() && entity instanceof PlayerEntity || entity.getName().equals(mc.player.getName()))
                continue;
            if (entity instanceof PlayerEntity && AreteClient.friendManager.isFriend(entity.getName().getString()))
                continue;
            if (!passive.getValue() && entity instanceof PassiveEntity) continue;
            if (!hostile.getValue() && entity instanceof HostileEntity) {
                //if (entity instanceof ZombifiedPiglinEntity && !(((ZombifiedPiglinEntity) entity).isAngryAt(mc.player)))
                //continue;
                //if (entity instanceof EndermanEntity && !(((EndermanEntity) entity).isAngryAt(mc.player))) continue;
            }
            //if (!neutral.getValue() && !isNeutralEntity(entity)) continue;
            if (mc.player.distanceTo(entity) > range.getValue()) continue;
            if (!entity.isAlive()) continue;
            if (!entity.isAttackable()) continue;
            if (entity instanceof FireworkRocketEntity || entity instanceof WitherSkullEntity || entity instanceof FireballEntity || entity instanceof ExperienceOrbEntity || entity instanceof ExperienceBottleEntity || entity instanceof ItemEntity || entity instanceof PotionEntity || entity instanceof EnderPearlEntity || entity instanceof TntEntity || entity instanceof MinecartEntity || entity instanceof EggEntity || entity instanceof ItemFrameEntity || entity instanceof EndCrystalEntity)
                continue;

            if (target == null) target = entity;
            else if (mc.player.distanceTo(entity) < mc.player.distanceTo(target)) target = entity;


        }
        return target;
    }

//    public boolean isNeutralEntity(Entity entity) {
//        if (entity instanceof ZombifiedPiglinEntity) {
//            return ((ZombifiedPiglinEntity) entity).isAngryAt(mc.player);
//        } else if (entity instanceof EndermanEntity) {
//            return ((EndermanEntity) entity).isAngryAt(mc.player);
//        }
//        return true;
//
//    }

    @Override
    public ColoredString getHudInfo() {
        return ColoredString.of(Color.WHITE, target == null ? "None" : target.getName().getString());
        //return null;
    }
}