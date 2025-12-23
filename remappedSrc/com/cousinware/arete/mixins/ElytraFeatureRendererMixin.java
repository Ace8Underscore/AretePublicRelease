package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ElytraFeatureRenderer.class)
public class ElytraFeatureRendererMixin<T extends LivingEntity> {


    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getSkinTextures()Lnet/minecraft/client/util/SkinTextures;"))
    public SkinTextures modifyCapeTexture1(AbstractClientPlayerEntity instance) {
        return new SkinTextures(instance.getSkinTextures().texture(), instance.getSkinTextures().textureUrl(), AreteClient.capes.getCurrentCapeLocation(), AreteClient.capes.getCurrentCapeLocation(), instance.getSkinTextures().model(), instance.getSkinTextures().secure());
    }


}