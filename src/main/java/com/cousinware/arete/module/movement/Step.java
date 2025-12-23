package com.cousinware.arete.module.movement;

import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.managers.RotationManager;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.game.InventoryUtils;
import com.cousinware.arete.utils.rotations.Rotation;
import com.cousinware.arete.utils.rotations.RotationSystem;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

public class Step extends Module implements Rotation {

    ModeSetting stepMode = new ModeSetting();
    RotationSystem rotationSystem;
    InventoryUtils.HotBarTask task;

    public Step() {
        super("Step", Category.Movement, -1);
        stepMode.setName("Mode").setModes("EnderPearl").setValue("EnderPearl").build(this);
        setSaveToConfig(false);
    }

    @Override
    public void onUpdate() {
        if (!rotationSystem.getAction().equals(RotationSystem.ACTION.Rotating)) {
            task = new InventoryUtils.HotBarTask(true, Items.ENDER_PEARL);
            rotationSystem.setRotaion(new RotationManager.Rotation(mc.player.getYaw(), 0));
            rotationSystem.rotateYaw(mc.player.getYaw(), true);

        } else {
            //task.useItem();
            mc.getNetworkHandler().sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, mc.world.pendingUpdateManager.incrementSequence().getSequence(), mc.player.getYaw(), 0));
            task.swapBack(true);
            this.disable();
        }
    }

    @Override
    @Subscribe
    public void packetListener(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet) {
            rotationSystem.setAction(RotationSystem.ACTION.Rotating);
            rotationSystem.modifyPacket(packet);
        }
    }

    public void onEnable() {
        rotationSystem = new RotationSystem();
        task = null;
    }

    public void onDisable() {
        rotationSystem.forceStop();
    }

}
