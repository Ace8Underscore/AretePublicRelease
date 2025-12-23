package com.cousinware.arete.events.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.BlockPos;

@Getter
@Setter
@AllArgsConstructor
public class PlayerPlaceSignEvent {
    BlockPos pos;
}
