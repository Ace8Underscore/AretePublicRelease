package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerEntity;

@Getter
@Setter
@AllArgsConstructor
public class PlayerEnterRenderEvent extends Event {

    private final PlayerEntity player;
}
