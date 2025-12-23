package com.cousinware.arete.module.world;

import com.cousinware.arete.command.Command;
import com.cousinware.arete.events.event.DisconnectEvent;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AntiPearlLoad extends Module {

    public static ModeSetting pearlMode = new ModeSetting();

    public AntiPearlLoad() {
        super("AntiPearlLoad", Category.World, -1);
        pearlMode.setName("Mode").setValue("Log").setModes("Log").build(this);
    }

    TeleportRequest teleportRequest = null;
    //ignore this teleport request when logging back in or will kick because client still needs to process teleport process since we stopped half way through it
    TeleportRequest prevTeleportRequest = null;

    Vec3d changeWorldPos = null;
    RegistryKey<World> changeWorldPrev = null;
    RegistryKey<World> changeWorldNow = null;

    boolean portalTp = false;

    //Verify Variables


    @Override
    public void onUpdate() {
        if (teleportRequest == null) {
        }

    }

    @Subscribe
    public void packetListenerLog1(PacketEvent.Receive event) {
        if (event.getPacket() instanceof PlayerRespawnS2CPacket packet) {
            RegistryKey<World> currentWorld = mc.world.getRegistryKey();
            RegistryKey<World> newWorld = packet.commonPlayerSpawnInfo().dimension();
            if (!currentWorld.equals(newWorld)) {
                Command.sendClientSideMessage("Changed world from " + currentWorld + " To " + newWorld, false);
                changeWorldPrev = currentWorld;
                changeWorldNow = newWorld;
                changeWorldPos = mc.player.getPos();
                portalTp = true;
            }

        }
    }

    @Subscribe
    public void packetListenerLog(PacketEvent.Receive event) {
        if (event.getPacket() instanceof PlayerPositionLookS2CPacket packet) {
            if (mc.player == null) return;
            changeWorldPos = mc.player.getPos();
            teleportRequest = new TeleportRequest(packet);
            //TODO check and see if player changed dimensions and coords align. going through portal auto logs player :()
            if (teleportRequest.getDistance() > 1000 && !isPortalTeleport(teleportRequest)) {
                if (mc.getNetworkHandler() == null) return;
                if (prevTeleportRequest != null && !teleportRequest.isSameRequest(prevTeleportRequest))mc.getNetworkHandler().getConnection().disconnect(Text.of("Logged Far Away Pearl Loaded"));
                else if (prevTeleportRequest == null) mc.getNetworkHandler().getConnection().disconnect(Text.of("Logged Far Away Pearl Loaded"));
                prevTeleportRequest = teleportRequest;
                teleportRequest = null;
                changeWorldPrev = null;
                changeWorldPos = null;
                portalTp = false;

            }
        }
    }

    //checks to see if the teleport was from traveling via portal
    public boolean isPortalTeleport(TeleportRequest teleportRequest) {
        if (changeWorldNow == null || changeWorldPrev == null || !portalTp) return false;
        if (changeWorldPrev.equals(World.NETHER)) {
            return (teleportRequest.teleportPos.multiply((double) 1 / 8, 1, (double) 1 / 8).distanceTo(changeWorldPos) < 500);
        } else if (changeWorldPrev.equals(World.OVERWORLD)) {
            return teleportRequest.teleportPos.multiply(8, 1, 8).distanceTo(changeWorldPos) < 500;
        }
        return false;
    }


    @Subscribe
    public void worldClose(DisconnectEvent event) {
        teleportRequest = null;
        portalTp = false;
    }


    @Getter
    @Setter
    private static class TeleportRequest {
        long requestTime = System.currentTimeMillis();
        Vec3d playerPos;
        Vec3d teleportPos;
        int teleportId;

        public TeleportRequest(PlayerPositionLookS2CPacket packet) {
            if (mc.player != null)playerPos = mc.player.getPos();
            //player is null so we logged in after a forced disconnect via module
            else
                playerPos = new Vec3d(packet.change().position().getX(), packet.change().position().getY(), packet.change().position().getZ());
            teleportPos = new Vec3d(packet.change().position().getX(), packet.change().position().getY(), packet.change().position().getZ());
            teleportId = packet.teleportId();

        }

        //we have this because requestTime and Ids will be different but player pos and teleport pos will be same upon reload
        public boolean isSameRequest(TeleportRequest teleportRequest) {
            return this.teleportPos.distanceTo(teleportRequest.teleportPos) < 3;
        }

        public double getDistance() {
            return playerPos.distanceTo(teleportPos);
        }

        public long timeSinceRequest() {
            return System.currentTimeMillis() - requestTime;
        }

    }

 }
