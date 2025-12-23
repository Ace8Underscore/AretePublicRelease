package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.misc.MaceTweaks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.MaceItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MaceItem.class)
public class MaceItemMixin {

    @Inject(method = "shouldDealAdditionalDamage", at = @At("HEAD"), cancellable = true)
    private static void doExtraDamage(LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        if (AreteClient.moduleManager.getModuleByName("MaceTweaks").isEnabled() && MaceTweaks.bypassDelay.getValue())cir.setReturnValue(true);
    }

    @Inject(method = "getBonusAttackDamage", at = @At("HEAD"), cancellable = true)
    private void extraDamage(Entity target, float baseAttackDamage, DamageSource damageSource, CallbackInfoReturnable<Float> cir) {
        if (AreteClient.moduleManager.getModuleByName("MaceTweaks").isEnabled() && MaceTweaks.damageSpoof.getValue() > 10) cir.setReturnValue(Float.valueOf(MaceTweaks.damageSpoof.getValue()));
    }
}
