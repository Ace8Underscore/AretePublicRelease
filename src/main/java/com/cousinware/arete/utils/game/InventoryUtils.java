package com.cousinware.arete.utils.game;

import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

import java.util.LinkedList;
import java.util.Queue;

public class InventoryUtils {

    public static MinecraftClient mc = MinecraftClient.getInstance();


    //if item is not found we return -1, if its found we return the item
    public static int getItemInHotBar(Item items) {

        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem().equals(items)) return i;
        }
        return -1;

    }

    public static int getSequence() {
        return mc.world.pendingUpdateManager.incrementSequence().getSequence();
    }

    public static Item[] getShulkers() {
        return new Item[]{Items.SHULKER_BOX, Items.CYAN_SHULKER_BOX, Items.BROWN_SHULKER_BOX, Items.BLUE_SHULKER_BOX, Items.GRAY_SHULKER_BOX, Items.BLACK_SHULKER_BOX, Items.GREEN_SHULKER_BOX, Items.LIGHT_BLUE_SHULKER_BOX, Items.LIGHT_GRAY_SHULKER_BOX, Items.LIME_SHULKER_BOX, Items.MAGENTA_SHULKER_BOX, Items.ORANGE_SHULKER_BOX, Items.PINK_SHULKER_BOX, Items.PURPLE_SHULKER_BOX, Items.RED_SHULKER_BOX, Items.WHITE_SHULKER_BOX, Items.YELLOW_SHULKER_BOX};
    }

    public static int getFirstBlockInHotBar() {
        if (mc.player.getInventory().player.getMainHandStack().getItem() instanceof BlockItem)
            return mc.player.getInventory().getSelectedSlot();
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem) return i;
        }
        return -1;

    }

    public static int swapToItem(int slot, boolean packet) {
        if (packet) {
            mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
        } else {
            mc.player.getInventory().setSelectedSlot(slot);
        }
        return mc.player.getInventory().getSelectedSlot();
    }

    public static boolean doesInventoryContain(Item... item) {
        for (int i = 0; i < item.length; i++) {
            for (int j = 0; j < mc.player.getInventory().getMainStacks().size(); ++j) {
                if (item[i] == mc.player.getInventory().getMainStacks().get(j).getItem()) {
                    return true;
                }
            }
        }

        return false;
    }


    public static class HotBarTask {

        int ogSlot = -1;
        @Getter
        boolean itemNotFound = false;

        public HotBarTask(boolean packet, Item... item) {
            Item findItem = null;
            for (Item swapToItem : item) {
                int slot = getItemInHotBar(swapToItem);
                if (slot != -1) {
                    findItem = swapToItem;
                    break;
                }
            }
            //check to see if item not found
            ogSlot = mc.player.getInventory().getSelectedSlot();
            if (findItem == null) {
                itemNotFound = true;
                return;
            }
            swapToItem(getItemInHotBar(findItem), packet);
        }

        public void useItem() {
            if (itemNotFound) return;
            mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, mc.world.pendingUpdateManager.incrementSequence().getSequence(), mc.player.getYaw(), mc.player.getPitch()));
        }


        public void swapBack(boolean packet) {
            if (itemNotFound) return;
            swapToItem(mc.player.getInventory().getSelectedSlot(), packet);
        }

    }


    public static class InventoryTask {
        int currentSlot = -1;
        int itemInventorySlot = -1;
        public boolean itemNotFound = false;
        public boolean swap = true;
        Item[] item = null;
        Queue<Packet> queue = new LinkedList<>();

        public InventoryTask(Item... itemStack) {
            item = itemStack;
            itemInventorySlot = getSlotWithStack(itemStack);
            if (itemInventorySlot < 9 && itemInventorySlot != -1) itemInventorySlot += 36;

            currentSlot = mc.player.getInventory().getSelectedSlot();
            swap = shouldSwap();
        }


        public void useItem(float yaw, float pitch) {
            if (itemNotFound) return;

            mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, mc.world.pendingUpdateManager.incrementSequence().getSequence(), yaw, pitch));

        }

        public void useItemCustom(InvetoryAction action) {
            action.execute();
        }

        //moves item from inventory to current hotbar
        public void swapItem() {
            if (itemNotFound || (!swap)) return;
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, itemInventorySlot, currentSlot, SlotActionType.SWAP, mc.player);
        }

        public boolean shouldSwap() {
            for (int i = 0; i < item.length; i++) {
                if (mc.player.getMainHandStack().getItem().equals(item[i])) return false;
            }
            return true;
        }


        public int getSlotWithStack(Item[] item) {
            for (int i = 0; i < item.length; i++) {
                for (int j = 0; j < mc.player.getInventory().getMainStacks().size(); ++j) {
                    if (item[i] == mc.player.getInventory().getMainStacks().get(j).getItem()) {
                        return j;
                    }
                }
            }

            itemNotFound = true;
            return -1;
        }

        @FunctionalInterface
        public interface InvetoryAction {
            void execute();
        }

    }


}
