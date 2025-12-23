package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;

public class RecieveMessageEvent extends Event {

    Text text;
    private boolean modified;
    MessageSignatureData messageSignatureData;
    MessageIndicator indicator;

    public RecieveMessageEvent(Text text, MessageSignatureData signature, MessageIndicator indicator) {
        this.text = text;
        this.messageSignatureData = signature;
        this.indicator = indicator;
        this.modified = false;
    }

    public Text getText() {
        return text;
    }

    public void setMessage(Text message) {
        this.text = message;
        this.modified = true;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public MessageSignatureData getMessageSignatureData() {
        return messageSignatureData;
    }

    public boolean isModified() {
        return modified;
    }

    public MessageIndicator getIndicator() {
        return indicator;
    }
}