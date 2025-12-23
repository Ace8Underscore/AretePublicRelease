package com.cousinware.arete.module.player;

import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.module.Module;
import com.google.common.eventbus.Subscribe;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class Freecam extends Module {

    public Freecam() {
        super("Freecam", Category.Player, -1);
    }

    Vec3d startingPos;

    PlayerEntity playerEntity;

    public void onUpdate() {
        mc.player.noClip = true;
        mc.cameraEntity.noClip = false;
        //TODO MAKE A NEW ENTITY SPECTATE AND LEAVE PLAYER ALONE

    }

    @Subscribe
    public void packetListener(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerInputC2SPacket || event.getPacket() instanceof PlayerMoveC2SPacket) {
            event.setCancelled(true);
            //Command.sendClientSideMessage(event.toString());
        }

    }

    public void onEnable() {
        mc.player.noClip = true;
        mc.cameraEntity.noClip = true;
        mc.player.setOnGround(false);
        if (!mc.player.isCreative() && !mc.player.isSpectator()) {
            mc.player.getAbilities().allowFlying = true;
            mc.player.getAbilities().creativeMode = false;
        }
        playerEntity = new OtherClientPlayerEntity(mc.world, new GameProfile(new UUID(1, 1), mc.player.getName().getString()));
        playerEntity.setPosition(mc.player.getPos());
        playerEntity.getInventory().clone(mc.player.getInventory());
        mc.player.setInvisible(true);
        mc.world.addEntity(playerEntity);


    }

    public void onDisable() {
        if (!mc.player.isCreative() && !mc.player.isSpectator()) {
            mc.player.getAbilities().flying = false;
            mc.player.getAbilities().allowFlying = false;
        }
        mc.player.noClip = false;
        mc.cameraEntity.noClip = false;
        mc.player.setInvisible(false);
        mc.player.setPosition(playerEntity.getPos());
        mc.world.removeEntity(playerEntity.getId(), Entity.RemovalReason.KILLED);
        playerEntity = null;
        mc.player.setVelocity(0, 0, 0);


    }



/**
    private void handleFreedcamMovement(PlayerEntity player) {
        // Get player input for movement controls
        double forwardMovement = player.input.movementForward;
        double sidewaysMovement = player.input.movementSideways;

        // Calculate player's movement vector
        Vec3d motion = new Vec3d(sidewaysMovement, 0, forwardMovement).normalize().multiply(0.2);

        // Apply motion to player
        player.move(MoverType.PLAYER, motion);
    } */
}
