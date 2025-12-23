package com.cousinware.arete.mixins;


import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)

public abstract class EntityMixin {


    @Shadow public abstract boolean equals(Object o);

    @Shadow public abstract float getYaw();

    @Redirect(method = "movementInputToVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;cos(F)F"))
    private static float setFloatG(float value) {


        if (AreteClient.rotationManager2.simulation && AreteClient.rotationManager2.currentlyRotating()) {
            return MathHelper.cos(AreteClient.rotationManager2.simulationYaw * 0.017453292F);
        }
        return MathHelper.cos(MinecraftClient.getInstance().player == null ? 1 : MinecraftClient.getInstance().player.getYaw() * 0.017453292F);

    }

    @Redirect(method = "movementInputToVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;sin(F)F"))
    private static float setFloatF(float value) {


        if (AreteClient.rotationManager2.simulation && AreteClient.rotationManager2.currentlyRotating()) {
            return MathHelper.sin(AreteClient.rotationManager2.simulationYaw * 0.017453292F);
        }
        return MathHelper.sin(MinecraftClient.getInstance().player == null ? 1 : MinecraftClient.getInstance().player.getYaw() * 0.017453292F);
    }
}
