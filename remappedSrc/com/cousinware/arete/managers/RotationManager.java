package com.cousinware.arete.managers;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.utils.rotations.RotationSystem2;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Random;

public class RotationManager {

    public final MinecraftClient mc = MinecraftClient.getInstance();

    public ArrayList<RotationSystem2> rotationSystems = new ArrayList<>();


    static ArrayList<Rotaion> nextRotations = new ArrayList<>();
    //public  float yaw2 = MinecraftClient.getInstance().player == null ? 1 : MinecraftClient.getInstance().player.getYaw();

    public float currentFakeYaw = -1;
    public float currentFakePitch = -1;
    public boolean rotating;
    public boolean movePacketFlow = false;



    public RotationManager() {

        AreteClient.eventBus.register(this);
    }


    public void onPostUpdate() {
        //
        if (mc.player == null) return;
        if (movePacketFlow) mc.player.setYaw(mc.player.getBodyYaw() + .0005f);
        //This is ran so at then end of every tick and everything has been calcualted we set rotation back to real MC one so next tick if not needed to be modified its fixed to where player is looking
        if (mc.player == null) return;
        AreteClient.rotationManager.fixRotationMovement(mc.player.getYaw(), mc.player.getPitch());
        rotating = false;
    }


    public void modifyRotatePacketVec3d(Vec3d pos, boolean packetDifference) {
        Random random = new Random();
        double pitch = getPitch(pos, false) + (packetDifference ? random.nextInt(0, 4) : 0);
        double yaw = getYaw(pos, false) + (packetDifference ? random.nextInt(-4, 0) : 0);
        nextRotations.add(new Rotaion((float) yaw, (float) pitch));
    }

    public void fixRotationMovement(float currentFakeYaw, float currentFakePitch) {
        rotating = true;
        this.currentFakeYaw = currentFakeYaw;
        this.currentFakePitch = currentFakePitch;


    }

    public void rotateBody(float yaw, float pitch) {

        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
        mc.player.setBodyYaw(yaw);
        mc.player.setHeadYaw(yaw);
        mc.cameraEntity.setYaw(mc.player.prevYaw);
        mc.cameraEntity.setPitch(mc.player.prevPitch);
        //mc.player.turnHead(yaw, pitch);

    }


    public void rotateVed3d(Vec3d pos, boolean packetDifference) {
        Random random = new Random();
        double pitch = getPitch(pos, false) + (packetDifference ? random.nextInt(0, 4) : 0);
        double yaw = getYaw(pos, false) + (packetDifference ? random.nextInt(-4, 0) : 0);
        doRotate((float) yaw, (float) pitch);


    }



    //packetDifference adds a different yaw and pitch differing by +- 2 to stop flagging simulation
    public void rotateBlockPos(BlockPos pos, boolean packetDifference) {
        Random random = new Random();
        double pitch = getPitch(pos, false) + (packetDifference ? random.nextInt(0, 4) : 0);
        double yaw = getYaw(pos, false) + (packetDifference ? random.nextInt(-4, 0) : 0);

        doRotate((float) yaw, (float) pitch);
    }

    public void doRotate(float yaw, float pitch) {
        boolean debug = true;

        if (!debug)mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround((float) yaw, (float) pitch, mc.player.isOnGround()));
        else  {

            float yawsss = mc.player.getYaw();
            float pitchesss = mc.player.getPitch();

            mc.player.setYaw(yaw);
            mc.player.setPitch(pitch);
           // mc.player.bodyYaw = mc.player.headYaw;


        }
    }


    public double getYaw(BlockPos pos, boolean packetDifference) {
        Random random = new Random();
        return (packetDifference ? random.nextDouble(0, 1) : 0) + mc.player.getYaw() + MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(pos.getZ() + 0.5 - mc.player.getZ(), pos.getX() + 0.5 - mc.player.getX())) - 90f - mc.player.getYaw());
    }

    public double getPitch(BlockPos pos, boolean packetDifference) {
        Random random = new Random();
        double diffX = pos.getX() + 0.5 - mc.player.getX();
        double diffY = pos.getY() + 0.5 - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double diffZ = pos.getZ() + 0.5 - mc.player.getZ();

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        return (packetDifference ? random.nextDouble(0, 1) : 0) + mc.player.getPitch() + MathHelper.wrapDegrees((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)) - mc.player.getPitch());
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

    public Rotaion genRotation(Vec3d pos) {
        return new Rotaion((float) getYaw(pos, true), (float) getPitch(pos, true));
    }

    public Rotaion genRotation(BlockPos pos) {
        return new Rotaion((float) getYaw(pos, true), (float) getPitch(pos, true));
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


