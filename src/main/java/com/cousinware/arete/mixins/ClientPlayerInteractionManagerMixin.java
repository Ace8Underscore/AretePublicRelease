package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.PlayerPlaceBlockEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(method = "interactBlockInternal", at = @At(value = "HEAD"))
    public void onBlockPlace(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        Item heldItem = player.getStackInHand(hand).getItem();

        PlayerPlaceBlockEvent playerPlaceBlockEvent = new PlayerPlaceBlockEvent(player, hand, hitResult, heldItem.getDefaultStack());
        AreteClient.eventBus.post(playerPlaceBlockEvent);

    }
}
