package com.cousinware.arete.mixins;

import net.minecraft.client.gl.GlDebug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlDebug.class)
public abstract class GlDebugMixin {

    @Inject(at = @At(value = "HEAD"), method = "onDebugMessage", cancellable = true)
    private void suppressMessage(int source, int type, int id, int severity, int messageLength, long message, long l, CallbackInfo ci) {

        //ummm just ignore this 1282 is a common error and usually doesnt mean anything bad sooo bye bye...

        if (id == 1280 || id == 1281 || id == 1282 || id == 2) {
            ci.cancel();
        }
    }
}
