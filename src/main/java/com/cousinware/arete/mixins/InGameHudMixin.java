package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RenderOverlayEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LayeredDrawer;render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"))
    public void renderGameOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {

        if (MinecraftClient.isHudEnabled()) {
            RenderOverlayEvent renderOverlayEvent = new RenderOverlayEvent(context);
            AreteClient.eventBus.post(renderOverlayEvent);
        }
    }


    @Inject(method = "render", at = @At("TAIL"))
    public void renderNotifications(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {

    }
}
