package com.cousinware.arete.events.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Setter
@Getter
@AllArgsConstructor
public class MouseMoveEvent {

    int mouseX;
    int mouseY;
    CallbackInfo callbackInfo;
}
