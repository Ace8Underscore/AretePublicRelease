package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;

public class MouseButtonLocationEvent extends Event {

    long window;
    int mouseX;
    int mouseY;
    int mods;

    public MouseButtonLocationEvent(long window, int mouseX, int mouseY, int mods) {

        this.window = window;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.mods = mods;
    }

}
