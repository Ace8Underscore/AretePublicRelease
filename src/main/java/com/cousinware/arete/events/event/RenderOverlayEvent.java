package com.cousinware.arete.events.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;

@AllArgsConstructor
@Getter
@Setter
public class RenderOverlayEvent {
    DrawContext context;


}
