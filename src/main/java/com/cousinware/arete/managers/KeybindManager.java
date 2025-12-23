package com.cousinware.arete.managers;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.KeyPressedEvent;
import com.cousinware.arete.events.event.MouseButtonEvent;
import com.cousinware.arete.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;

public class KeybindManager {

    MinecraftClient mc = MinecraftClient.getInstance();

    public KeybindManager() {
        AreteClient.eventBus.register(this);
    }

    @Subscribe
    public void onKeyPressed(KeyPressedEvent event) {
        if (mc.currentScreen != null) return;
        if (event.getAction() != 1) return;

        for (Module module : AreteClient.moduleManager.getModules()) {
            if (module.getKeybind() == event.getKey()) {
                module.toggle();
            }
        }
    }


    @Subscribe
    public void onMousePress(MouseButtonEvent event) {
        if (mc.currentScreen != null) return;
        if (event.getAction() != 1) return;

        for (Module module : AreteClient.moduleManager.getModules()) {
            if (module.getKeybind() == event.getButton()) {
                module.toggle();
            }
        }
    }


}
