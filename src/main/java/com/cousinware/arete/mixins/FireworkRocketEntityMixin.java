package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"))
    public Vec3d modifyFireworkPitch(LivingEntity instance) {
        if (instance.isGliding() && AreteClient.rotationManager.currentlyRotating() && AreteClient.rotationManager.simulationPitch != -180) {
            return instance.getRotationVector(AreteClient.rotationManager.simulationPitch, instance.getHeadYaw());
        }
        return instance.getRotationVector();
    }
}
