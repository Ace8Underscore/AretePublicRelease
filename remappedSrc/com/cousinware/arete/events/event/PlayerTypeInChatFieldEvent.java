package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;

public class PlayerTypeInChatFieldEvent extends Event {

    String text;
    int keyCode;
    int scanCode;
    int modifiers;

    public PlayerTypeInChatFieldEvent(String text ,int keyCode, int scanCode, int modifiers) {

        this.text = text;
        this.keyCode = keyCode;
        this.scanCode = scanCode;
        this.modifiers = modifiers;
    }

    public String getText() {
        return text;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public int getModifiers() {
        return modifiers;
    }

    public int getScanCode() {
        return scanCode;
    }
}
