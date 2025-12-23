package com.cousinware.arete.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

public class InventoryUtils {

    public static MinecraftClient mc = MinecraftClient.getInstance();


    //if item is not found we return -1, if its found we return the item
    public static int getItemInHotBar(Item items) {

        for (int i = 0; i < 8; i++) {
            if (mc.player.getInventory().getStack(i).getItem().equals(items)) return i;
        }
        return -1;

    }

    public static int getFirstBlockInHotBar() {
        if (mc.player.getInventory().getMainHandStack().getItem() instanceof BlockItem) return mc.player.getInventory().selectedSlot;
        for (int i = 0; i < 8; i++) {
            if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem) return i;
        }
        return -1;

    }

    public static int swapToItem(int slot, boolean packet) {
        if (packet) {
            mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
        } else {

        }
        return mc.player.getInventory().selectedSlot;
    }

}
