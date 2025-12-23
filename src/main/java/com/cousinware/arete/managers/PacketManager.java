package com.cousinware.arete.managers;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.MinecraftInterface;
import net.minecraft.network.packet.Packet;

import java.util.ArrayList;

public class PacketManager implements MinecraftInterface {

    ArrayList<Packet<?>> quePackets = new ArrayList<>();
    ArrayList<DelayedPacket> delayedPackets = new ArrayList<>();
    ArrayList<DelayedPacket> scheduledRemove = new ArrayList<>();

    public PacketManager() {
        AreteClient.eventBus.register(this);
    }

    public void onPreUpdate() {
        for (DelayedPacket delayedPacket : delayedPackets) {
            if (delayedPacket.tick()) scheduledRemove.add(delayedPacket);
        }

        //remove packets from delayedPacket List
        for (DelayedPacket removePacket : scheduledRemove) {
            delayedPackets.remove(removePacket);
        }
        scheduledRemove.clear();
    }

    public boolean shouldModifyPacket(Module module) {

        return true;
    }


    public void queuePacket(Packet<?> packet) {
        quePackets.add(packet);
    }

    public void queuePacketDelay(int delay, Packet<?> packet) {
        delayedPackets.add(new DelayedPacket(delay, packet));
    }


    public static class DelayedPacket {

        int delay;
        int timeTicked = 0;
        Packet<?> packet;

        public DelayedPacket(int delay, Packet<?> packet) {
            this.delay = delay;
            this.packet = packet;
        }

        public boolean tick() {
            if (timeTicked > delay) {
                mc.getNetworkHandler().sendPacket(packet);
                return true;
            }
            timeTicked++;
            return false;
        }
    }

}
