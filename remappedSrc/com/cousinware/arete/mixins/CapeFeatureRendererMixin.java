package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Client.Core;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.io.IOException;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin {


    @ModifyExpressionValue(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/SkinTextures;capeTexture()Lnet/minecraft/util/Identifier;"))
    private @Nullable Identifier modifyCapeTexture(Identifier original, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k, float l) throws IOException {

        if (Core.capes.getValue()) {
            if (AreteClient.validIgns.contains(abstractClientPlayerEntity.getName().getString())) {

                //original = Identifier.of("arete", "assets/arete/capes/cape.png");
                //MinecraftClient.getInstance().getTextureManager().
                //InputStream in = null;// = location.;
                //MinecraftClient.getInstance().getTextureManager().registerTexture(location, new NativeImageBackedTexture(NativeImage.read(in)));
                return AreteClient.capes.getCurrentCapeLocation();
            }
        }
        return null;
    }

//    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getSkinTextures()Lnet/minecraft/client/util/SkinTextures;", ordinal = 0))
//    public SkinTextures modifySkin(AbstractClientPlayerEntity instance) {
//        if (AreteClient.validIgns.contains(instance.getName().getString())) return new SkinTextures(MinecraftClient.getInstance().player.getSkinTextures().texture(), MinecraftClient.getInstance().player.getSkinTextures().textureUrl(), capeLocation, capeLocation, MinecraftClient.getInstance().player.getSkinTextures().model(), MinecraftClient.getInstance().player.getSkinTextures().secure());
//        return instance.getSkinTextures();
//    }
}
