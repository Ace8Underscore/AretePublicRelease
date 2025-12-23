package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;

public class PlayerSendMessageEvent extends Event {

    String text;
    boolean addToHistory;


    public PlayerSendMessageEvent(String text, boolean addToHistory) {
        this.text = text;
        this.addToHistory = addToHistory;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setAddToHistory(boolean addToHistory) {
        this.addToHistory = addToHistory;
    }

    public boolean isAddToHistory() {
        return addToHistory;
    }
}
