package com.cousinware.arete.mixins;

import com.cousinware.arete.utils.texture.Capes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {


    //what did bose do to cause this?


    @Redirect(method = "getSkinTextures", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/PlayerListEntry;getSkinTextures()Lnet/minecraft/client/util/SkinTextures;"))
    public SkinTextures getSkinTexture(PlayerListEntry playerListEntry) {
        if (MinecraftClient.getInstance().player.getName().getString().equalsIgnoreCase(playerListEntry.getProfile().getName())) {
            return new SkinTextures(playerListEntry.getSkinTextures().texture(), playerListEntry.getSkinTextures().textureUrl(), Capes.getCurrentCapeLocation() == null ? playerListEntry.getSkinTextures().capeTexture() : Capes.getCurrentCapeLocation(), Capes.getCurrentCapeLocation() == null ? playerListEntry.getSkinTextures().elytraTexture() : Capes.getCurrentCapeLocation(), playerListEntry.getSkinTextures().model(), playerListEntry.hasPublicKey());
        }
        return playerListEntry.getSkinTextures();
    }
}
