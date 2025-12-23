package com.cousinware.arete.module.render;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RenderOverlayEvent;
import com.cousinware.arete.module.Module;
import com.google.common.eventbus.Subscribe;

public class CustomMouse extends Module {

    public CustomMouse() {
        super("CustomMouse", Category.Render, -1);
    }

    @Subscribe
    public void render(RenderOverlayEvent event) {

        event.getContext().drawText(mc.textRenderer, "Gay", (int) mc.getWindow().getX() / 4, (int) mc.getWindow().getY() / 4, -1, true);
        AreteClient.fontManager.drawText(event.getContext(), "Hi", (int) (mc.mouse.getX() / 2) - 8, (int) (mc.mouse.getY() / 2) - 8);
    }
}
