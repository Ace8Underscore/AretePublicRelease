package com.cousinware.arete.module.combat;

import com.cousinware.arete.module.Module;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;

import java.util.LinkedList;
import java.util.Queue;

public class Critcals extends Module {

    Queue<Packet> queue = new LinkedList<>();
    PlayerInteractEntityC2SPacket lastAttackPacket = null;
    boolean attacking = false;

    public Critcals() {
        super("Criticals", Category.Combat, -1);
    }

    public void onUpdate() {
        if (queue.isEmpty()) {
            attacking = false;
        } else {
            mc.player.networkHandler.sendPacket(queue.poll());
            attacking = true;
        }

    }

//    @Subscribe
//    public void packetSent(PacketEvent.Send event) {
//        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket packet) {
//
//            if (lastAttackPacket != null && lastAttackPacket.equals(packet)) {
//
//
//
//
//                //stop below code and allow attack to go through
//                mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
//                Command.sendClientSideMessage("Attack Packet Let through", false);
//                lastAttackPacket = null;
//                return;
//            }
//
//            if (packet.type.getType() == PlayerInteractEntityC2SPacket.InteractType.ATTACK && lastAttackPacket == null) {
//                queue.add(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getPos().add(0, 0, 0), true, false));
//                queue.add(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getPos().add(0, .20000000298023, 0), false, false));
//                queue.add(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getPos().add(0, .3176000081897, 0), false, false));
//                queue.add(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getPos().add(0, .3544480140121, 0), false, false));
//                queue.add(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getPos().add(0, .312159058895, 0), false, false));
//                queue.add(event.getPacket());
//                queue.add(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getPos().add(0, .19231588054777, 0), false, false));
//
//                lastAttackPacket = packet;
//                Command.sendClientSideMessage("Canceled Attack Packet", false);
//                event.setCancelled(true);
//            }
//        }
//
//        if (attacking && event.getPacket() instanceof PlayerMoveC2SPacket packet) {
//            if (packet.onGround) event.setCancelled(true);
//            else {
//                //mc.player.networkHandler.sendPacket(new PlayerInputC2SPacket(new PlayerInput(false, false, false, false, true, false, false)));
//            }
//
//        }
//    }
}
