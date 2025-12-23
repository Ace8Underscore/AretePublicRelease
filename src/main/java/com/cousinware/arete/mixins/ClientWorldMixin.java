package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.EntitySpawnEvent;
import com.cousinware.arete.events.event.PlayerEnterRenderEvent;
import com.cousinware.arete.events.event.PlayerLeaveRenderEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {

    @Inject(method = "addEntity", at = @At("TAIL"))
    public void addPlayerToWorld(Entity entity, CallbackInfo ci) {
        if (entity.isPlayer()) {
            PlayerEnterRenderEvent playerEnterRenderEvent = new PlayerEnterRenderEvent((PlayerEntity) entity);
            AreteClient.eventBus.post(playerEnterRenderEvent);
        } else {
            EntitySpawnEvent entitySpawnEvent = new EntitySpawnEvent(entity);
            AreteClient.eventBus.post(entitySpawnEvent);
        }
    }

    @Inject(method = "removeEntity", at = @At("HEAD"))
    public void removePlayerFromWorld(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci) {
        Entity entity = MinecraftClient.getInstance().world.getEntityById(entityId);
        if (entity instanceof PlayerEntity) {
            PlayerLeaveRenderEvent playerLeaveRenderEvent = new PlayerLeaveRenderEvent((PlayerEntity) entity);
            AreteClient.eventBus.post(playerLeaveRenderEvent);
        }
    }
}
