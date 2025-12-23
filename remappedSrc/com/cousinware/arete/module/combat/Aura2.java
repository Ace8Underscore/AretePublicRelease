package com.cousinware.arete.module.combat;


import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.rotations.Rotation;
import com.cousinware.arete.utils.rotations.RotationSystem2;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.DoubleSetting;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;

/*
New Aura to use with my new rewritten rotation system
 */
public class Aura2 extends Module implements Rotation {

    DoubleSetting range = new DoubleSetting();
    BoolSetting players = new BoolSetting();
    ModeSetting attackMode = new ModeSetting();
    ModeSetting rotaionsMode = new ModeSetting();
    BoolSetting passive = new BoolSetting();
    BoolSetting hostile = new BoolSetting();
    BoolSetting neutral = new BoolSetting();
    RotationSystem2 rotationSystem;

    public Aura2() {
        super("Aura2", Category.Combat, -1, "Testing Rotations");
        range.setName("Range").setMin(1).setMax(8).setValue(5.5f).build(this);
        attackMode.setName("Mode").setModes("Switch", "Nodelay").setValue("Switch").build(this);
        rotaionsMode.setName("Rotation").setModes("None", "Old", "Sim").setValue("Sim").build(this);
        players.setName("Players").setValue(true).build(this);
        passive.setName("Passive").setValue(true).build(this);
        hostile.setName("Hostile").setValue(true).build(this);
        neutral.setName("Neutral").setValue(false).build(this);

        rotationSystem = new RotationSystem2();
    }

    Entity target;
    int delay = 0;

    @Override
    public void onUpdate() {
        delay++;
        //we search for a new target every tick to find best option
        target = getTarget();
        //if no valid target is found we get null so then we return
        if (target == null) return;

        //rotate!
        if (!rotaionsMode.getValue().equalsIgnoreCase("none")) {
            rotationSystem.rotate(target.getEyePos(), !mc.player.isFallFlying() && rotaionsMode.getValue().equalsIgnoreCase("sim"));
        }

        //attack!
        // i think we need to attack post tick after we look but ima try reverse rq
        switchAttack();


    }

    public void switchAttack() {
        int totalWaitTime = (int) (mc.player.getAttackCooldownProgressPerTick());

        //Command.sendClientSideMessage(String.valueOf(getAttackSpeed(mc.player)));
        if (delay > totalWaitTime) {
            delay = 0;
            attack();


        }
    }

    public void attack() {
        //mc.player.setBodyYaw(mc.player.bodyYaw + .05f);
        //mc.player.setYaw(mc.player.getBodyYaw() - .05f);

        //Rotations

        //AreteClient.rotationManager.modifyRotatePacketVec3d(target.getEyePos(), true);
        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
        //rotationSystem.setAction(RotationSystem2.ACTION.Nothing);   \
        }

    @Subscribe
    @Override
    public void packetListener(PacketEvent.Send event) {
        if (!(event.getPacket() instanceof PlayerMoveC2SPacket)) return;
        if (rotationSystem.getAction().equals(RotationSystem2.ACTION.Rotating)) {
            rotationSystem.modifyPacket(((PlayerMoveC2SPacket) event.getPacket()));
            rotationSystem.setAction(RotationSystem2.ACTION.Nothing);
        }
    }




    @Nullable
    public Entity getTarget() {
        Entity target = null;
        for (Entity entity : mc.world.getEntities()) {
            if (!players.getValue() && entity instanceof PlayerEntity || entity.getName().equals(mc.player.getName())) continue;
            if (entity instanceof PlayerEntity && AreteClient.friendManager.isFriend(entity.getName().getString())) continue;
            if (!passive.getValue() && entity instanceof PassiveEntity) continue;
            if (!hostile.getValue() && entity instanceof HostileEntity) {
                if (entity instanceof ZombifiedPiglinEntity && !(((ZombifiedPiglinEntity) entity).isAngryAt(mc.player))) continue;
                if (entity instanceof EndermanEntity && !(((EndermanEntity) entity).isAngryAt(mc.player))) continue;
            }
            if (!neutral.getValue() && !isNeutralEntity(entity)) continue;
            if (mc.player.distanceTo(entity) > range.getValue()) continue;
            if (!entity.isAlive()) continue;
            if (!entity.isAttackable()) continue;
            if (!entity.isSpectator()) continue;
            if (entity instanceof FireworkRocketEntity || entity instanceof FireballEntity || entity instanceof ExperienceOrbEntity || entity instanceof ExperienceBottleEntity || entity instanceof ItemEntity) continue;

            if (target == null) target = entity;
            else if (mc.player.distanceTo(entity) < mc.player.distanceTo(target)) target = entity;


        }
        return target;
    }

    public boolean isNeutralEntity(Entity entity) {
        if (entity instanceof ZombifiedPiglinEntity) {
            return ((ZombifiedPiglinEntity) entity).isAngryAt(mc.player);
        } else if (entity instanceof EndermanEntity) {
            return ((EndermanEntity) entity).isAngryAt(mc.player);
        }
        return true;

    }

    @Override
    public String getHudInfo() {
        return " [" + attackMode.getValue() + "]";
    }
}
