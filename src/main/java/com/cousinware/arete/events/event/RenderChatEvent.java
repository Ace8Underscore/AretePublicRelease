package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;

@AllArgsConstructor
@Getter
@Setter
public class RenderChatEvent extends Event {

    String text;
    DrawContext context;
    int mouseX;
    int mouseY;
    float delta;

}
