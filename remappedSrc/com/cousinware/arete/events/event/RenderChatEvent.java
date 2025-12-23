package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;
import net.minecraft.client.gui.DrawContext;

public class RenderChatEvent extends Event {

    String text;
    DrawContext context;
    int mouseX;
    int mouseY;
    float delta;

    public RenderChatEvent(String text, DrawContext context, int mouseX, int mouseY, float delta) {

        this.text = text;
        this.context = context;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.delta = delta;
    }

    public DrawContext getContext() {
        return context;
    }

    public String getText() {
        return text;
    }
}
