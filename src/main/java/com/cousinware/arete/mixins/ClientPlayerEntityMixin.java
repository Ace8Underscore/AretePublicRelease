package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.BlockPushPlayerEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    public void cancelBlockPushVelocity(double x, double z, CallbackInfo ci) {
        BlockPushPlayerEvent blockPushPlayerEvent = new BlockPushPlayerEvent(x, z);
        AreteClient.eventBus.post(blockPushPlayerEvent);

        if (blockPushPlayerEvent.isCancelled()) {
            ci.cancel();
        }
    }
}
