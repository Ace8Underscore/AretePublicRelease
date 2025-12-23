package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;

public class MouseButtonEvent extends Event {

    long window;
    int button;
    int action;
    int mods;

    public MouseButtonEvent(long window, int button, int action, int mods) {

        this.window = window;
        this.button = button;
        this.action = action;
        this.mods = mods;
    }

    public int getAction() {
        return action;
    }

    public int getButton() {
        return button;
    }
}
