package com.cousinware.arete.managers;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.events.event.TickEvent;
import com.cousinware.arete.utils.MinecraftInterface;
import com.cousinware.arete.utils.rotations.RotationSystem2;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Random;

@Setter@Getter
public class RotationManager2 implements MinecraftInterface {

    public ArrayList<RotationSystem2> rotationSystems = new ArrayList<>();
    public float simulationYaw = 0;
    public boolean simulation = false;

    public RotationManager2() {

        AreteClient.eventBus.register(this);
    }

    @Subscribe
    public void onTick(TickEvent event) {
        for (RotationSystem2 rotSys : rotationSystems) {
            if (rotSys.getAction().equals(RotationSystem2.ACTION.Rotating)) {
                // if any rotation system is needing to rotate we do this line of code below which sends rotation packets every tick
                //mc.player.setYaw(mc.player.getBodyYaw() + .0005f);
                break;
            }
        }
        if (mc.player == null || mc.world == null || mc.currentScreen != null) return;
        mc.player.setYaw(mc.player.getBodyYaw() + .0005f);
    }

    public boolean currentlyRotating() {
        for (RotationSystem2 rotSys : rotationSystems) {
            if (rotSys.getAction().equals(RotationSystem2.ACTION.Rotating)) {
                return true;
            }
        }
        return false;
    }



    public double getYaw(Vec3d pos, boolean packetDifference) {
        Random random = new Random();

        return (packetDifference ? random.nextDouble(0, 1) : 0) + mc.player.getYaw() + MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(pos.getZ() - mc.player.getZ(), pos.getX() - mc.player.getX())) - 90f - mc.player.getYaw());
    }

    public double getPitch(Vec3d pos, boolean packetDifference) {
        Random random = new Random();
        double diffX = pos.getX() - mc.player.getX();
        double diffY = pos.getY() - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double diffZ = pos.getZ() - mc.player.getZ();

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        return (packetDifference ? random.nextDouble(0, 1) : 0) + mc.player.getPitch() + MathHelper.wrapDegrees((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)) - mc.player.getPitch());
    }

    public RotationManager2.Rotaion genRotation(Vec3d pos) {
        return new RotationManager2.Rotaion((float) getYaw(pos, true), (float) getPitch(pos, true));
    }


    @Getter
    public static class Rotaion {

        float yaw;
        float pitch;
        BlockHitResult hitResult;

        public Rotaion(float yaw, float pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public Rotaion(float yaw, float pitch, BlockHitResult hitResult) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.hitResult = hitResult;
        }

    }

}

