package com.cousinware.arete.module.world;

import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AirPlace extends Module {
    public AirPlace() {
        super("AirPlace", Category.World, -1);
    }

    @Subscribe
    public void sendPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {
            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
            packet.hand = Hand.OFF_HAND;
        }
    }

    @Subscribe
    public void sendPacketEnd(PacketEvent.Send.Post event) {
        if (event.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {
            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
        }
    }
}
