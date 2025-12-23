package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.render.NoRender;
import net.minecraft.client.render.MapRenderState;
import net.minecraft.client.render.MapRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapRenderer.class)
public class MapRendererMixin {

    @Inject(method = "draw", at = @At("HEAD"))
    public void noRenderMapMarker(MapRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, boolean bl, int light, CallbackInfo ci) {
        if (NoRender.mapDecorations.getValue() && AreteClient.moduleManager.getModuleByName("NoRender").isEnabled()) {
            state.decorations.clear();
        }
    }

    @Inject(method = "draw", at = @At("HEAD"), cancellable = true)
    public void noRenderMap(MapRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, boolean bl, int light, CallbackInfo ci) {
        if (NoRender.maps.getValue() && AreteClient.moduleManager.getModuleByName("NoRender").isEnabled()) {
            ci.cancel();
        }
    }

}