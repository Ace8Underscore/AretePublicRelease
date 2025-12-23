package com.cousinware.arete.module.misc;

import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.game.InventoryUtils;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.IntSetting;
import com.cousinware.arete.utils.settings.StringSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.item.Item;
import net.minecraft.network.packet.c2s.play.RenameItemC2SPacket;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

public class AutoRename extends Module {

    BoolSetting shulkersOnly = new BoolSetting();
    StringSetting renameText = new StringSetting();
    IntSetting delay = new IntSetting();

    int tickTimer = 0;

    public AutoRename() {
        super("AutoRename", Category.Misc, -1);
        shulkersOnly.setName("ShulkersOnly").setValue(true).build(this);
        renameText.setValue("EMP").setName("Text").build(this);
        delay.setValue(2).setName("Delay").setMin(1).setMax(10).build(this);
    }

    public void onUpdate() {
        tickTimer++;
        if (!(tickTimer >= delay.getValue())) return;
        if (!(mc.currentScreen instanceof AnvilScreen)) return;
        tickTimer = 0;
        if (!mc.player.currentScreenHandler.getSlot(0).getStack().isEmpty()) {
            mc.player.networkHandler.sendPacket(new RenameItemC2SPacket(renameText.getValue()));
            ((AnvilScreenHandler) mc.player.currentScreenHandler).setNewItemName(renameText.getValue());
            return;
        }
        if (mc.player.currentScreenHandler.getSlot(0).getStack().isEmpty()) {
            int targetSlot = findItem();
            if (targetSlot == -1) return; //No item found to rename in inventory
            //Shift click item into rename slot
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, targetSlot, 0, SlotActionType.QUICK_MOVE, mc.player);

        }

    }

    public int findItem() {
        for (int i = 3; i < 39; i++) {
            ScreenHandler screenHandler = mc.player.currentScreenHandler;
            if (shulkersOnly.getValue()) {
                if (isShulker(screenHandler.getSlot(i).getStack().getItem()) && !screenHandler.getSlot(i).getStack().getName().equals(Text.of(renameText.getValue())))
                    return i;
            } else if (!screenHandler.getSlot(i).getStack().isEmpty() && !screenHandler.getSlot(i).getStack().getName().equals(Text.of(renameText.getValue())))
                return i;

        }
        return -1;
    }

    public boolean isShulker(Item item) {
        Item[] shulkers = InventoryUtils.getShulkers();
        for (int i = 0; i < shulkers.length; i++) {
            if (shulkers[i].equals(item)) return true;
        }
        return false;
    }

    @Subscribe
    public void packetEvent(PacketEvent.Send event) {
        if (event.getPacket() instanceof RenameItemC2SPacket) {
            if (((RenameItemC2SPacket) event.getPacket()).getName().equalsIgnoreCase(renameText.getValue())) {
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 2, 0, SlotActionType.QUICK_MOVE, mc.player);
            }
        }
    }
}
