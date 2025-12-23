package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.movement.Velocity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "isPushedByFluids", at = @At("RETURN"), cancellable = true)
    public void isPushedByLiquid(CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftClient.getInstance().player == null) return;
        if (MinecraftClient.getInstance().player.input == null) return;
        if (AreteClient.moduleManager.getModuleByName("Velocity").isEnabled() && Velocity.pushSetting.getValue() && Velocity.waterPush.getValue()) {
            if (MinecraftClient.getInstance().player.input.getMovementInput().x == 0 && MinecraftClient.getInstance().player.input.getMovementInput().y == 0) {
                cir.setReturnValue(false);
            }
        }
    }


}
