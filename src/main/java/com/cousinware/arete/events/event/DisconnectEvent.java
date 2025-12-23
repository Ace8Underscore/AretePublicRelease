package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;
import net.minecraft.client.MinecraftClient;

public class DisconnectEvent extends Event {

    MinecraftClient minecraftClient;

    public DisconnectEvent(MinecraftClient minecraftClient) {
        this.minecraftClient = minecraftClient;
    }

    public MinecraftClient getMc() {
        return minecraftClient;
    }
}
