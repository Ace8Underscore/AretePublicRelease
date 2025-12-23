package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.GameStartEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Inject(method = "init", at = @At("HEAD"))
    public void TitleScreenInit(CallbackInfo ci) {
        GameStartEvent event = new GameStartEvent();
        AreteClient.eventBus.post(event);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void userData(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
//        context.getMatrices().push();
//        context.getMatrices().scale(.65f, .65f, .65f);
//        context.drawTextWithShadow(AreteClient.fontManager.getFont(), "Arete Account: " + AreteClient.username, 0, 0, Color.GREEN.getRGB());
//        context.drawTextWithShadow(AreteClient.fontManager.getFont(), "Dev", 0, 10, AreteClient.username.equalsIgnoreCase("Stepcousin") ? Color.GREEN.getRGB() : Color.RED.getRGB());
//
//        context.getMatrices().pop();
    }


}
