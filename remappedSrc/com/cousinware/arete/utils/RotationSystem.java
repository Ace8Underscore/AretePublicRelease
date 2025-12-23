package com.cousinware.arete.utils;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.managers.RotationManager;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;

public class RotationSystem {

    ACTION currentAction = ACTION.Nothing;

    boolean moveFix = false;

    @Getter
    RotationManager.Rotaion rotaion = null;


    public RotationSystem() {
        AreteClient.eventBus.register(this);
    }

    public void tickRotation() {
        if (currentAction == ACTION.Rotating && moveFix) {
            AreteClient.rotationManager.fixRotationMovement(rotaion.getYaw(), rotaion.getPitch());
        } else if (currentAction == ACTION.DoingAction) {

        } else {

        }
    }

    public void updateMoveFix(ModeSetting rotaionsMode) {
        if (rotaionsMode.getValue().equalsIgnoreCase("new")) this.setMoveFix(true);
        else this.setMoveFix(false);
        AreteClient.rotationManager.movePacketFlow = true;

    }

    public void setMoveFix(boolean moveFix) {
        this.moveFix = moveFix;
    }

    public void updateRotation(RotationManager.Rotaion rotaion) {
        this.rotaion = rotaion;
        tickRotation();
        AreteClient.rotationManager.movePacketFlow = true;
    }

    public void stop() {
        currentAction = ACTION.Nothing;
        AreteClient.rotationManager.movePacketFlow = false;
        //TODO Testing
        //AreteClient.eventBus.unregister(this);
    }

    public void setCurrentAction(ACTION currentAction) {
        this.currentAction = currentAction;
    }

    public ACTION getCurrentAction() {
        return currentAction;
    }

    public float getYaw() {
        return this.rotaion.getYaw();
    }

    public float getPitch() {
        return this.rotaion.getPitch();
    }

    @Subscribe
    public void inComingPackets(PacketEvent.Send event) {

    }

    public enum ACTION {
        Rotating,
        DoingAction,
        Nothing
    }
}
