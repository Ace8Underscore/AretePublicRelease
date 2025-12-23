package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;

public class KeyPressedEvent extends Event {

    long window;
    int key;
    int scancode;
    int action;
    int modifiers;

    public KeyPressedEvent(long window, int key, int scancode, int action, int modifiers) {
        this.window = window;
        this.key = key;
        this.scancode = scancode;
        this.action = action;
        this.modifiers = modifiers;
    }

    public int getKey() {
        return key;
    }

    public int getAction() {
        return action;
    }
}
