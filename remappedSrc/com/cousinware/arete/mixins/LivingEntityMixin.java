package com.cousinware.arete.mixins;


import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

//    @Inject(method = "getPi", at = @At("RETURN"), cancellable = true)
//    public void getYaw(float tickDelta, CallbackInfoReturnable<Float> cir) {
//        cir.setReturnValue(AreteClient.rotationManager.currentFakeYaw == -1 ? AreteClient.rotationManager.currentFakeYaw : tickDelta == 1.0F ? MinecraftClient.getInstance().player.headYaw : MathHelper.lerp(tickDelta, MinecraftClient.getInstance().player.prevHeadYaw, MinecraftClient.getInstance().player.headYaw));
//    }

}
