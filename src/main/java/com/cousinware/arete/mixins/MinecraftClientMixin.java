package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.DisconnectEvent;
import com.cousinware.arete.events.event.TickEvent;
import com.cousinware.arete.managers.AssetManager;
import com.cousinware.arete.module.Client.Core;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.texture.Capes;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {


    @Inject(method = "tick", at = @At("HEAD"))
    public void onUpdate(CallbackInfo ci) {
        AreteClient.packetManager.onPreUpdate();
        AreteClient.moduleManager.processTicks();
        TickEvent tickEvent = new TickEvent();
        AreteClient.eventBus.post(tickEvent);
    }

    @ModifyArg(method = "updateWindowTitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setTitle(Ljava/lang/String;)V"))
    public String updateWindowTitleInvoke$setTitle(String title) throws InterruptedException {
        if (Core.customTitle.getValue()) {
            return AreteClient.title;
        }
        return title;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void onPostUpdate(CallbackInfo ci) {
        AreteClient.moduleManager.processPostTicks();
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initTail(CallbackInfo info) {

        //load NVG and Assets
        NVGContext.init();
        AreteClient.gameManager.loadFonts();
        AreteClient.capes = new Capes();
        AreteClient.assetManager = new AssetManager();
    }

    @Inject(method = "onDisconnected", at = @At(value = "HEAD"))
    private void onDisconnect(CallbackInfo info) {
        DisconnectEvent event = new DisconnectEvent(MinecraftClient.getInstance());
        AreteClient.eventBus.post(event);
    }


    @Inject(method = "onResolutionChanged", at = @At(value = "TAIL"))
    private void onResolutionChanged(CallbackInfo info) {
        int newWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
        int newHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();

        ArrayList<Module> hudModules = AreteClient.moduleManager.getHudModules();

        hudModules.forEach(module -> {
            module.resizeHud(newWidth, newHeight);
        });
    }


}
