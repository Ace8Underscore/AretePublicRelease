package com.cousinware.arete.mixins;


import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.LoadingIntoGameEvent;
import com.cousinware.arete.events.event.PlayerJoinServerEvent;
import com.cousinware.arete.events.event.PlayerLeaveServerEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.UUID;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Unique
    PlayerLeaveServerEvent pastLeaveEvent = null;

    @Inject(method = "handlePlayerListAction", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"))
    private void playerJoinServer(PlayerListS2CPacket.Action action, PlayerListS2CPacket.Entry receivedEntry, PlayerListEntry currentEntry, CallbackInfo ci) {
        String name = currentEntry.getProfile().getName();
        if (name != null) {
            PlayerJoinServerEvent event = new PlayerJoinServerEvent(action, receivedEntry, currentEntry, name);
            AreteClient.eventBus.post(event);
        }
    }

    @Inject(method = "onGameStateChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/WorldLoadingState;handleChunksComingPacket()V"))
    private void gameStateChange(GameStateChangeS2CPacket packet, CallbackInfo ci) {
        LoadingIntoGameEvent event = new LoadingIntoGameEvent();
        AreteClient.eventBus.post(event);
    }


    @Inject(method = "onPlayerRemove", at = @At(value = "HEAD"))
    private void playerLeaveServer(PlayerRemoveS2CPacket packet, CallbackInfo ci) {
        String name = getName(packet);
        if (name != null) {
            PlayerLeaveServerEvent event = new PlayerLeaveServerEvent(packet, name);
            if (pastLeaveEvent == null) {
                AreteClient.eventBus.post(event);
                pastLeaveEvent = event;
            }
            if (!pastLeaveEvent.getPacket().profileIds().getFirst().equals(event.getPacket().profileIds().getFirst())) {
                AreteClient.eventBus.post(event);
                pastLeaveEvent = event;
            }
        }
    }

    @Unique
    @Nullable
    public String getName(PlayerRemoveS2CPacket packet) {
        UUID uuid = packet.profileIds().getFirst();
        if (MinecraftClient.getInstance().player == null) return null;
        Iterator<PlayerListEntry> iterator = MinecraftClient.getInstance().player.networkHandler.getPlayerList().stream().iterator();
        //String name = MinecraftClient.getInstance().player.networkHandler.getPlayerListEntry(event.getPacket().profileIds().getFirst()).getProfile().getName();
        String name = "";
        while (iterator.hasNext()) {
            PlayerListEntry playerListEntry = iterator.next();
            if (playerListEntry.getProfile().getId().equals(uuid)) {
                name = String.valueOf(playerListEntry.getProfile().getName());
                return name;


            }
        }
        return null;
    }


}
