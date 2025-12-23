package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.player.AntiVoid;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockMixin {

    @Inject(method = "blocksMovement", at = @At("HEAD"), cancellable = true)
    public void blocksMoveWeb(CallbackInfoReturnable<Boolean> cir) {
        if (AreteClient.moduleManager.getModuleByName("Avoid").isEnabled() && AntiVoid.web.getValue()) cir.setReturnValue(false);
    }
}
