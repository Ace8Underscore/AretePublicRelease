package com.cousinware.arete.events.event;


import com.cousinware.arete.events.Event;
import lombok.Getter;
import net.minecraft.network.packet.Packet;

@Getter
public class PacketEvent extends Event {

    private final Packet<?> packet;


    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public static class Receive extends PacketEvent {
        public Receive(Packet<?> packet) {
            super(packet);
        }

        public static class Post extends PacketEvent {
            public Post(Packet<?> packet) {
                super(packet);
            }
        }
    }

    public static class Send extends PacketEvent {
        public Send(Packet<?> packet) {
            super(packet);
        }

        public static class Post extends PacketEvent {
            public Post(Packet<?> packet) {
                super(packet);
            }
        }
    }
}