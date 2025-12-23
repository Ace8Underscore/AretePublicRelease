package com.cousinware.arete.module.render;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RenderOverlayEvent;
import com.cousinware.arete.module.Client.Core;
import com.cousinware.arete.module.Module;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.nanovg.NVGColor;

import java.awt.*;
import java.util.Comparator;

public class ModuleList extends Module {

    public ModuleList() {
        super("ArrayList", Category.Render, -1);
    }


    int offset = 0;
    @Subscribe
    public void render(RenderOverlayEvent event) {
        offset = 0;
        AreteClient.moduleManager.getModules()
                .stream()
                .filter(Module::isEnabled)
                .filter(Module::isDrawn)
                .sorted(Comparator.comparing(module -> mc.textRenderer.getWidth(module.getName() + module.getHudInfo()) * (-1)))
                .forEach(module -> {
                    AreteClient.fontManager.drawText(event.getContext(), module.getName() + module.getHudInfo(), 1, offset * 10);
                    offset++;
                });

    }
}
