package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

@Getter
@Setter
@AllArgsConstructor
public class SoundEvent extends Event {
    Identifier soundEvent;
    Vec3d pos;
}
