package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;

@AllArgsConstructor
@Getter
@Setter
public class PlayerLeaveServerEvent extends Event {

    PlayerRemoveS2CPacket packet;
    String name;

}
