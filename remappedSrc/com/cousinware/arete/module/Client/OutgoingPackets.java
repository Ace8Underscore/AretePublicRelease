package com.cousinware.arete.module.Client;

import com.cousinware.arete.command.Command;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class OutgoingPackets extends Module {

    public OutgoingPackets() {
        super("OutgoingPackets", Category.Client, -1);
    }

    @Subscribe
    public static void packetListener(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerInteractBlockC2SPacket) {
            BlockPos pos = ((PlayerInteractBlockC2SPacket) event.getPacket()).getBlockHitResult().getBlockPos();
            Vec3d vec3d = ((PlayerInteractBlockC2SPacket) event.getPacket()).getBlockHitResult().getPos();
            Direction side = ((PlayerInteractBlockC2SPacket) event.getPacket()).getBlockHitResult().getSide();
            boolean insideBlock = ((PlayerInteractBlockC2SPacket) event.getPacket()).getBlockHitResult().isInsideBlock();
        Command.sendClientSideMessage(pos + "   " + side + "    " +  "    " + insideBlock + "   " + vec3d);
        }
        if (event.getPacket() instanceof HandSwingC2SPacket) {
            //Command.sendClientSideMessage(String.valueOf(((HandSwingC2SPacket) event.getPacket()).getHand()));
        }
        //Command.sendClientSideMessage(event.getPacket());
    }

    public void onEnable() {
    }

    public void onDisable() {
    }


}


