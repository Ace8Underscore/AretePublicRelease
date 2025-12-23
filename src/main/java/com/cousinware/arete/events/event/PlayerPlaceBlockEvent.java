package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

@Getter
@Setter
@AllArgsConstructor
public class PlayerPlaceBlockEvent extends Event {
    ClientPlayerEntity player;
    Hand hand;
    BlockHitResult hitResult;
    ItemStack item;
}
