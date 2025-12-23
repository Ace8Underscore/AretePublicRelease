package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.world.NoSound;
import net.minecraft.entity.mob.EndermanEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndermanEntity.class)
public class EndermanEntityMixin {

    @Inject(method = "playAngrySound", at = @At("HEAD"), cancellable = true)
    public void cancelAngrySound(CallbackInfo ci) {
        if (AreteClient.moduleManager.getModuleByName("NoSound").isEnabled() && NoSound.enderman.getValue()) {
            ci.cancel();
        }
    }
}
