package com.cousinware.arete.module.player;

import com.cousinware.arete.command.Command;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class Step extends Module {

    public Step() {
        super("Step", Category.Player, -1, "description");
        enablePriority(2);
    }

    int delay = -1;
    int startingY;

    public void onUpdate() {
        delay++;

        if (delay == 1) mc.player.jump();

        if (delay == 11) {
            mc.player.setVelocityClient(0, 0, 0);
            mc.player.setVelocity(0, 0, 0);
            mc.player.jump();



            System.out.println("OFF");
        }

        if (delay == 20) {
            this.disable();
            System.out.println("OFF");

        }
    }

    public void onEnable() {
        startingY = mc.player.getBlockY() + 1;
        System.out.println("ON");
        delay = -1;
    }

    @Subscribe
    public void out(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket) {
            if (delay == 9 || delay == 10) {
                Command.sendClientSideMessage(String.valueOf(((PlayerMoveC2SPacket) event.getPacket()).isOnGround()));
                ((PlayerMoveC2SPacket) event.getPacket()).y = Math.floor(startingY);
                mc.player.setPos(mc.player.getX(), Math.floor(startingY), mc.player.getZ());
                mc.world.setBlockState(mc.player.getBlockPos().down(), Blocks.STONE.getDefaultState());
                ((PlayerMoveC2SPacket) event.getPacket()).onGround = true;
            }

            System.out.println(((PlayerMoveC2SPacket) event.getPacket()).onGround + "     " + ((PlayerMoveC2SPacket) event.getPacket()).y);
        }
    }


    @Subscribe
    public void in(PacketEvent.Receive event) {
        if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
            if (delay >= 10 ) {
                event.setCancelled(true);
            }

        }
    }
}
