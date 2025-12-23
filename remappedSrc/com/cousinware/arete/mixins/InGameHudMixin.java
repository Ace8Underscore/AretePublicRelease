package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RenderOverlayEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("HEAD"))
    public void renderGameOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {

        //we start our rendering process so we dont have to do to each module
        AreteClient.fontManager.start();
        //each module that calls renderOverlayEvent will be ticked once
        RenderOverlayEvent renderOverlayEvent = new RenderOverlayEvent(context);
        AreteClient.eventBus.post(renderOverlayEvent);

        //Now after all of the renddering code has been sent to our bus we close it
        AreteClient.fontManager.close();
    }
}
