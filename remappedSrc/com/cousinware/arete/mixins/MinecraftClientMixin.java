package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.DisconnectEvent;
import com.cousinware.arete.events.event.TickEvent;
import com.cousinware.arete.utils.rendering.font.nvg.NVGContext;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {


    @Shadow
    public static MinecraftClient getInstance() {
        return null;
    }

    @Shadow public abstract void tick();

    @Inject(method = "tick", at = @At("HEAD"))
    public void onUpdate(CallbackInfo ci) {
        AreteClient.packetManager.onPreUpdate();
        AreteClient.moduleManager.processTicks();
        TickEvent tickEvent = new TickEvent();
        AreteClient.eventBus.post(tickEvent);
    }

    @ModifyArg(method = "updateWindowTitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setTitle(Ljava/lang/String;)V"))
    public String updateWindowTitleInvoke$setTitle(String title) throws InterruptedException {
        return AreteClient.title;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void onPostUpdate(CallbackInfo ci) {
        AreteClient.moduleManager.processPostTicks();
        AreteClient.rotationManager.onPostUpdate();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initTail(CallbackInfo info) {
        NVGContext.init();
        //AreteClient.fontManager.init();
    }

    @Inject( method = "onDisconnected",at = @At(value = "HEAD"))
    private void onDisconnect(CallbackInfo info) {
        DisconnectEvent event = new DisconnectEvent(MinecraftClient.getInstance());
        AreteClient.eventBus.post(event);
    }

}
