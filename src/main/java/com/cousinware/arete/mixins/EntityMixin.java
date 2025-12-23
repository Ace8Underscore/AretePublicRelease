package com.cousinware.arete.mixins;


import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.movement.ElytraFly;
import com.cousinware.arete.module.movement.Velocity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)

public abstract class EntityMixin {


    @Shadow
    private int id;

    @Shadow
    public abstract boolean isPlayer();

    @ModifyVariable(method = "changeLookDirection", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
    private float cancelPitchChange(float value) {
        if (AreteClient.moduleManager.getModuleByName("ElytraFly").isEnabled() && ElytraFly.modeSetting.getValue().equals("Forever") && ElytraFly.ignoreMousePitchInputForever.getValue())
            return 0;
        return value;
    }

    @Redirect(method = "movementInputToVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;cos(F)F"))
    private static float setFloatG(float value) {
        if (AreteClient.rotationManager.simulation && AreteClient.rotationManager.currentlyRotating() && AreteClient.rotationManager.simulationYaw != -720) {
            return MathHelper.cos(AreteClient.rotationManager.simulationYaw * 0.017453292F);
        }
        return MathHelper.cos(MinecraftClient.getInstance().player == null ? 1 : MinecraftClient.getInstance().player.getYaw() * 0.017453292F);

    }

    @Redirect(method = "movementInputToVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;sin(F)F"))
    private static float setFloatF(float value) {
        if (AreteClient.rotationManager.simulation && AreteClient.rotationManager.currentlyRotating() && AreteClient.rotationManager.simulationYaw != -720) {
            return MathHelper.sin(AreteClient.rotationManager.simulationYaw * 0.017453292F);
        }
        return MathHelper.sin(MinecraftClient.getInstance().player == null ? 1 : MinecraftClient.getInstance().player.getYaw() * 0.017453292F);
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    private void doEntityVelocity(Entity entity, CallbackInfo ci) {
        if (entity instanceof PlayerEntity || (Object) this instanceof PlayerEntity) {
            if (!MinecraftClient.getInstance().isInSingleplayer() && AreteClient.moduleManager.getModuleByName("Velocity").isEnabled() && Velocity.pushSetting.getValue() && Velocity.entityPush.getValue()) {
                ci.cancel();
            }
        }
    }

}
