package com.cousinware.arete.utils.rotations;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.managers.RotationManager;
import com.cousinware.arete.utils.MinecraftInterface;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

@Getter
@Setter
public class RotationSystem implements MinecraftInterface {

    RotationSystem.ACTION action = ACTION.Nothing;
    RotationManager.Rotation rotaion = null;
    boolean movementSimulation = false;

    public RotationSystem() {
        AreteClient.rotationManager.rotationSystems.add(this);
    }

    public void rotate(Vec3d vec3d) {
        rotaion = AreteClient.rotationManager.genRotation(vec3d);
        action = ACTION.Rotating;


    }

    public void setRotation(RotationManager.Rotation rotation, boolean movementSimulation) {
        rotaion = rotation;
        action = ACTION.Rotating;
        if (movementSimulation) {
            AreteClient.rotationManager.setSimulationYaw(rotaion.getYaw());
            AreteClient.rotationManager.setSimulation(true);
        }


    }

    public void rotate(Vec3d vec3d, boolean movementSimulation) {
        rotaion = AreteClient.rotationManager.genRotation(vec3d);
        action = ACTION.Rotating;
        //! on current grim you dont have to simulate movement when on elytra if rotating
        if (movementSimulation) {
            this.movementSimulation = true;
            AreteClient.rotationManager.setSimulationYaw(rotaion.getYaw());
            AreteClient.rotationManager.setSimulation(true);
        }

    }

    public void rotateYaw(float yaw, boolean movementSimulation) {
        action = ACTION.Rotating;
        //! on current grim you dont have to simulate movement when on elytra if rotating
        if (movementSimulation && !mc.player.isGliding()) {
            this.movementSimulation = true;
            AreteClient.rotationManager.setSimulationYaw(yaw);
            AreteClient.rotationManager.setSimulation(true);
        }

    }

    public void rotatePitch(float pitch, boolean movementSimulation) {
        action = ACTION.Rotating;
        if (movementSimulation) {
            this.movementSimulation = true;
            AreteClient.rotationManager.setSimulationPitch(pitch);
            AreteClient.rotationManager.setSimulation(true);
        }

    }

    public void modifyPacket(PlayerMoveC2SPacket packet) {
        if (rotaion == null) {
            if (AreteClient.rotationManager.simulationYaw != -720) {
                packet.yaw = AreteClient.rotationManager.simulationYaw;
            }
            if (AreteClient.rotationManager.simulationPitch != -180) {
                packet.pitch = AreteClient.rotationManager.simulationPitch;
            }
        } else {
            packet.yaw = rotaion.getYaw();
            packet.pitch = rotaion.getPitch();
        }
    }

    public void setAction(RotationSystem.ACTION action) {
        this.action = action;
        if (this.action == ACTION.Nothing && movementSimulation) {
            setMovementSimulation(false);
            AreteClient.rotationManager.simulation = false;
            AreteClient.rotationManager.simulationPitch = -180;
            AreteClient.rotationManager.simulationYaw = -720;
        }
    }

    public void safeStop() {
        if (this.action == ACTION.Nothing && movementSimulation) {
            setMovementSimulation(false);
            AreteClient.rotationManager.simulation = false;
            AreteClient.rotationManager.simulationPitch = -180;
            AreteClient.rotationManager.simulationYaw = -720;
        }
    }

    public void forceStop() {
        //if (this.action == ACTION.Nothing && movementSimulation) {
        setMovementSimulation(false);
        AreteClient.rotationManager.simulation = false;
        AreteClient.rotationManager.simulationPitch = -180;
        AreteClient.rotationManager.simulationYaw = -720;
        //}
    }


    public enum ACTION {
        Rotating,
        DoingAction,
        Nothing
    }
}