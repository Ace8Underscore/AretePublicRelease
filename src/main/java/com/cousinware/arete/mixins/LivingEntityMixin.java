package com.cousinware.arete.mixins;


import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.movement.ElytraFly;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Redirect(method = "calcGlidingVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getPitch()F"))
    private float travel(LivingEntity instance) {


        if (instance.isGliding() && AreteClient.moduleManager.getModuleByName("ElytraFly").isEnabled() && ElytraFly.modeSetting.getValue().equalsIgnoreCase("bounce") && ElytraFly.pitchSpoof.getValue()) {
            return ElytraFly.pitchSpoofAngle.getValue().floatValue();
        }

        if (instance.isGliding() && AreteClient.rotationManager.currentlyRotating() && AreteClient.rotationManager.simulationPitch != -180) {
            return AreteClient.rotationManager.simulationPitch;
        }
        return instance.getPitch();
    }



}
