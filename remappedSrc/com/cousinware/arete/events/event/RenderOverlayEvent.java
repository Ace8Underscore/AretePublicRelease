package com.cousinware.arete.events.event;

import net.minecraft.client.gui.DrawContext;

public class RenderOverlayEvent {

    DrawContext context;

    public RenderOverlayEvent(DrawContext context) {
        this.context = context;
    }

    public DrawContext getContext() {
        return context;
    }
}
