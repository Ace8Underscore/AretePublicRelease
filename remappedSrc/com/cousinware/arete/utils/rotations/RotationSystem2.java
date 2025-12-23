package com.cousinware.arete.utils.rotations;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.managers.RotationManager2;
import com.cousinware.arete.utils.MinecraftInterface;
import com.cousinware.arete.utils.RotationSystem;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
@Getter@Setter
public class RotationSystem2 implements MinecraftInterface {

    RotationSystem2.ACTION action = ACTION.Nothing;
    RotationManager2.Rotaion rotaion = null;
    boolean movementSimulation = false;

    public RotationSystem2() {
        AreteClient.rotationManager2.rotationSystems.add(this);
    }

    public void rotate(Vec3d vec3d) {
        rotaion = AreteClient.rotationManager2.genRotation(vec3d);
        action = ACTION.Rotating;
        //mc.player.changeLookDirection(rotaion.getYaw(), rotaion.getYaw());


    }

    public void rotate(Vec3d vec3d, boolean movementSimulation) {
        rotaion = AreteClient.rotationManager2.genRotation(vec3d);
        action = ACTION.Rotating;
        //! on current grim you dont have to simulate movement when on elytra if rotating
        if (movementSimulation && !mc.player.isFallFlying()) {
            this.movementSimulation = true;
            AreteClient.rotationManager2.setSimulationYaw(rotaion.getYaw());
            AreteClient.rotationManager2.setSimulation(true);
        }

    }

    public void modifyPacket(PlayerMoveC2SPacket packet) {
        packet.yaw = rotaion.getYaw();
        packet.pitch = rotaion.getPitch();
    }

    public void setAction(RotationSystem2.ACTION action) {
        this.action = action;
        if (this.action == ACTION.Nothing && movementSimulation) {
            setMovementSimulation(false);
            AreteClient.rotationManager2.simulation = false;
        }
    }

    public void safeStop() {

    }




    public enum ACTION {
        Rotating,
        DoingAction,
        Nothing
    }
}
