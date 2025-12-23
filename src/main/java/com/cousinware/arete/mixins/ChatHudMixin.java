package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RecieveMessageEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {


    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/MessageIndicator;indicatorColor()I"))
    public int renderChatColor(MessageIndicator instance) {

        //TODO SYNC WITH CLIENT COLOR
        return new Color(47, 50, 159, 255).brighter().brighter().getRGB();
    }


    @Shadow
    private List<ChatHudLine> messages;

    @Shadow
    @Final
    private MinecraftClient client;


    @Shadow
    protected abstract void addMessage(Text message, @Nullable MessageSignatureData signatureData, @Nullable MessageIndicator indicator);

    @Unique
    private boolean skipOnAddMessage;


    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", cancellable = true)
    private void onAddMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        if (skipOnAddMessage) return;

        RecieveMessageEvent event = new RecieveMessageEvent(message, signatureData, indicator);
        AreteClient.eventBus.post(event);

        if (event.isCancelled()) ci.cancel();
        if (event.isModified()) {
            ci.cancel();

            skipOnAddMessage = true;
            addMessage(event.getText(), signatureData, event.getIndicator());
            skipOnAddMessage = false;
        }
    }
}

