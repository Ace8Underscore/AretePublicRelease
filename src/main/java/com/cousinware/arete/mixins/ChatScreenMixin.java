package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.PlayerSendMessageEvent;
import com.cousinware.arete.utils.MinecraftInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin implements MinecraftInterface {

    @Shadow
    protected TextFieldWidget chatField;

    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    public void messageSent(String chatText, boolean addToHistory, CallbackInfo ci) {
        PlayerSendMessageEvent event = new PlayerSendMessageEvent(chatText, addToHistory);
        AreteClient.eventBus.post(event);


        if (event.isCancelled()) {
            ci.cancel();
            //Closes the chat screen after sending a message
            MinecraftClient.getInstance().setScreen(null);
        }
    }


}
