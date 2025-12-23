package com.cousinware.arete.events.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;

@Getter
@Setter
@AllArgsConstructor
public class EntitySpawnEvent {
    Entity entity;

}
