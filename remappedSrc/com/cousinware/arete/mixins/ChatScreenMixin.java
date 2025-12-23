package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.PlayerSendMessageEvent;
import com.cousinware.arete.events.event.PlayerTypeInChatFieldEvent;
import com.cousinware.arete.events.event.RenderChatEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Shadow private ChatInputSuggestor chatInputSuggestor;

    @Shadow protected TextFieldWidget chatField;

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

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        PlayerTypeInChatFieldEvent playerTypeInChatFieldEvent = new PlayerTypeInChatFieldEvent(this.chatField.getText() ,keyCode, scanCode, modifiers);
        AreteClient.eventBus.post(playerTypeInChatFieldEvent);

    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void renderChat(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        RenderChatEvent event = new RenderChatEvent(this.chatField.getText(),context, mouseX, mouseY, delta);
        AreteClient.eventBus.post(event);
    }



}
