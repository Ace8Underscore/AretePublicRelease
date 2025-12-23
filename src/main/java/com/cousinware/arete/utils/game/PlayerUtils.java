package com.cousinware.arete.utils.game;

import com.cousinware.arete.utils.MinecraftInterface;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class PlayerUtils implements MinecraftInterface {

    @Nullable
    public static BlockPos getLookingPos() {
        return BlockPos.ofFloored(mc.crosshairTarget.getPos());
    }


    @Nullable
    @Deprecated
    public static ServerWorld getServerWorld() {
        for (ServerWorld world : mc.getServer().getWorlds()) {
            if (world.getDimension().equals(mc.world.getDimension())) return world.toServerWorld();
        }
        return null;
    }

    public static void jump() {
        //so for some reason you need to send a packet to verify jump...
        mc.getNetworkHandler().sendPacket(new PlayerInputC2SPacket(new PlayerInput(true, false, false, false, true, false, false)));
        mc.player.setVelocity(mc.player.getVelocity().x, 0.42f, mc.player.getVelocity().z);
    }

    public static double getSpeed(SpeedType speedType) {
        double distTraveledLastTickX = mc.player.getX() - mc.player.lastX;
        double distTraveledLastTickZ = mc.player.getZ() - mc.player.lastZ;
        double sped = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
        if (speedType.equals(SpeedType.BPS)) return simplifySpeed(Math.sqrt(sped) * 20);
        return simplifySpeed(Math.sqrt((float) sped) * 71.2729367892);
    }

    private static double simplifySpeed(double input) {
        return (double) Math.round(10.0 * input) / 10.0;

    }

    public static Direction getLoookingDirection() {
        return mc.player.getFacing();
    }


    public enum SpeedType {
        KMPH,
        BPS

    }
}
