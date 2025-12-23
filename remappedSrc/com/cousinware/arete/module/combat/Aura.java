package com.cousinware.arete.module.combat;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.managers.RotationManager;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.RotationSystem;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.DoubleSetting;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.component.Component;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

public class Aura extends Module {

    DoubleSetting range = new DoubleSetting();
    ModeSetting attackMode = new ModeSetting();
    ModeSetting rotaionsMode = new ModeSetting();
    BoolSetting players = new BoolSetting();
    BoolSetting passive = new BoolSetting();
    BoolSetting hostile = new BoolSetting();

    boolean rotating = false;

    public Aura() {
        super("Aura", Category.Combat, -1, "Fight Stuff");
        range.setName("Range").setMin(1).setMax(8).setValue(5.5f).build(this);
        attackMode.setName("Mode").setModes("Switch", "Nodelay").setValue("Switch").build(this);
        rotaionsMode.setName("Rotation").setModes("None", "Old", "New").setValue("New").build(this);
        players.setName("Players").setValue(true).build(this);
        passive.setName("Passive").setValue(true).build(this);
        hostile.setName("Hostile").setValue(true).build(this);
        enablePriority(5);
    }

    Entity target;
    int delay = 0;

    public void onUpdate() {
        //movement fix for new rotations
        if (rotaionsMode.getValue().equalsIgnoreCase("New")) rotationSystem.updateMoveFix(rotaionsMode);

        //mc.player.setYaw(mc.player.getBodyYaw() + .0005f);
        delay++;

        //Find Target
        target = getTarget();
        if (target == null) return;

        //Attack Target

        if (attackMode.getValue().equalsIgnoreCase("Switch"))switchAttack();
        else if (attackMode.getValue().equalsIgnoreCase("nodelay")) attackNoDelay();

        }

        public void doRotation() {
            if (target != null) {
                RotationManager.Rotaion rotaion = AreteClient.rotationManager.genRotation(target.getEyePos());
                rotationSystem.setCurrentAction(RotationSystem.ACTION.Rotating);
                rotationSystem.updateRotation(rotaion);

            }
        }


    public void switchAttack() {
        int totalWaitTime = (int) (mc.player.getAttackCooldownProgressPerTick());
        Command.sendClientSideMessage(String.valueOf(totalWaitTime));

       //Command.sendClientSideMessage(String.valueOf(getAttackSpeed(mc.player)));
        if (delay > totalWaitTime) {
            delay = 0;
            attack();


        }
    }

    public double getAttackSpeed(PlayerEntity player) {
        // Get the ItemStack held in the player's main hand
        Item heldItem = player.getMainHandStack().getItem();

        if (!heldItem.equals(Items.AIR)) {


            //if (heldItem.)
            while (heldItem.getComponents().iterator().hasNext()) {
                Component component = heldItem.getComponents().iterator().next();
                if (component.value() instanceof AttributeModifiersComponent) {
                    if (((AttributeModifiersComponent) component.value()).modifiers().equals(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
                        return (double) component.value();
                    }
                }
            }


        }

        // Default attack speed if no modifier found
        return 1.5;
    }

    public void attack() {
        //mc.player.setBodyYaw(mc.player.bodyYaw + .05f);
        //mc.player.setYaw(mc.player.getBodyYaw() - .05f);

        //Rotations

        //AreteClient.rotationManager.modifyRotatePacketVec3d(target.getEyePos(), true);
        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
        rotating = true;
        if (rotaionsMode.getValue().equalsIgnoreCase("new"))doRotation();
    }

    public void attackNoDelay() {
        attack();
    }

    public Entity getTarget() {
        Entity target = null;
        for (Entity entity : mc.world.getEntities()) {
            if (!players.getValue() && entity instanceof PlayerEntity || entity.getName().equals(mc.player.getName())) continue;
            if (!passive.getValue() && entity instanceof PassiveEntity) continue;
            if (!hostile.getValue() && entity instanceof HostileEntity) continue;
            if (mc.player.distanceTo(entity) > range.getValue()) continue;
            if (!entity.isAlive()) continue;
            if (!entity.isAttackable()) continue;
            if (entity instanceof FireworkRocketEntity || entity instanceof FireballEntity || entity instanceof ExperienceOrbEntity || entity instanceof ExperienceBottleEntity || entity instanceof ItemEntity) continue;

            if (target == null) target = entity;
            else if (mc.player.distanceTo(entity) < mc.player.distanceTo(target)) target = entity;


        }
        return target;
    }

    public float getAttackSpeed() {
        // Get the ItemStack held in the player's main hand
        ItemStack heldItem = mc.player.getMainHandStack();

        // Get the player's attribute instance for attack speed
        for (AttributeModifiersComponent.Entry equipmentSlot :  heldItem.getItem().getAttributeModifiers().modifiers()) {
            Command.sendClientSideMessage(equipmentSlot.toString());
            Command.sendClientSideMessage(String.valueOf(equipmentSlot.attribute()));
        }
        // Return default attack speed if no modifier found
        return 1;
    }

    @Subscribe
    public void inComingPackets(PacketEvent.Send event) {
        if (!rotationSystem.getCurrentAction().equals(RotationSystem.ACTION.Rotating) || rotaionsMode.getValue().equalsIgnoreCase("None")) return;
        if (event.getPacket() instanceof PlayerMoveC2SPacket) {
            if (!AreteClient.packetManager.shouldModifyPacket(this)) {
                Command.sendClientSideMessage("Not fixing packet");
                rotationSystem.setCurrentAction(RotationSystem.ACTION.Nothing);
                return;
            }
            ((PlayerMoveC2SPacket) event.getPacket()).yaw = rotationSystem.getYaw();
            ((PlayerMoveC2SPacket) event.getPacket()).pitch = rotationSystem.getPitch();
            //
            rotationSystem.setCurrentAction(RotationSystem.ACTION.Nothing);



        }
        //Command.sendClientSideMessage(event.getPacket());
    }

    @Override
    public void onEnable() {
        rotationSystem = new RotationSystem();
    }

    @Override
    public void onDisable() {
        rotationSystem.stop();

    }
}
