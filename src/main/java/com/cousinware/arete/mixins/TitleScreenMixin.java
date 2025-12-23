package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.GameStartEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    boolean opened = false;

    @Inject(method = "init", at = @At("RETURN"))
    public void TitleScreenInit(CallbackInfo ci) {
        if (!opened) {
            GameStartEvent event = new GameStartEvent();
            AreteClient.eventBus.post(event);
            opened = true;
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void userData(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {

    }



}
