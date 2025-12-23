package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.KeyPressedEvent;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(method = "onKey", at = @At("HEAD"))
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        KeyPressedEvent keyPressedEvent = new KeyPressedEvent(window, key, scancode, action, modifiers);
        AreteClient.eventBus.post(keyPressedEvent);
    }
}
