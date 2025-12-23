package com.cousinware.arete.mixins;

import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(ChatInputSuggestor.class)
public class ChatInputSuggestorMixin {

    @Shadow @Final private TextFieldWidget textField;

    @Shadow @Final private int color;

    @Shadow @Nullable private CompletableFuture<Suggestions> pendingSuggestions;

    @Inject(method = "refresh", at = @At("HEAD"), cancellable = true)
    public void renderMessage(CallbackInfo ci) {

    }
}
