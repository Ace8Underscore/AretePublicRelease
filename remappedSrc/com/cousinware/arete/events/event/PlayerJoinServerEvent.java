package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;

@AllArgsConstructor
@Getter
@Setter
public class PlayerJoinServerEvent extends Event {

    net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Action action;
    PlayerListS2CPacket.Entry receivedEntry;
    PlayerListEntry currentEntry;
    String name;

}
