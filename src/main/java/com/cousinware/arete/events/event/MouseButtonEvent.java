package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@AllArgsConstructor
@Getter
@Setter
public class MouseButtonEvent extends Event {

    long window;
    int button;
    int action;
    int mods;
    CallbackInfo ci;

    public boolean isRightClickDown(boolean checkForShiftHeld) {
        if (checkForShiftHeld) return button == 1 && action == 1 && mods == 1;
        return button == 1 && action == 1;
    }

    public boolean isRightClickReleased(boolean checkForShiftHeld) {
        if (checkForShiftHeld) return button == 1 && action == 0 && mods == 1;
        return button == 1 && action == 0;
    }

    public boolean isLeftClickDown(boolean checkForShiftHeld) {
        if (checkForShiftHeld) return button == 0 && action == 1 && mods == 1;
        return button == 0 && action == 1;
    }

    public boolean isLeftClickReleased(boolean checkForShiftHeld) {
        if (checkForShiftHeld) return button == 0 && action == 0 && mods == 1;
        return button == 0 && action == 0;
    }


}
