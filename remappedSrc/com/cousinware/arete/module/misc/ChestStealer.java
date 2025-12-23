package com.cousinware.arete.module.misc;

import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.IntSetting;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

import java.util.PriorityQueue;
import java.util.Queue;

public class ChestStealer extends Module {

    IntSetting delaySetting = new IntSetting();
    IntSetting stealPerTick = new IntSetting();
    BoolSetting closeOnEmpty = new BoolSetting();

    public ChestStealer() {
        super("ChestStealer", Category.Misc, -1, "Steal Stuff From Chest");
        delaySetting.setName("Delay").setMin(0).setMax(10).setValue(8).build(this);
        stealPerTick.setName("StealPerTick").setMin(1).setMax(10).setValue(2).build(this);
        closeOnEmpty.setName("CloseOnEmpty").setValue(true).build(this);
    }

    Queue<Integer> stealPos = new PriorityQueue<>();
    int delay = 0;

    public void onUpdate() {
        delay++;
        if (mc.currentScreen == null)  {
            stealPos.clear();
            return;
        }

        if (stealPos.isEmpty()) getStealIndex();

        if (delay >= delaySetting.getValue()) {
            delay = 0;
            //Command.sendClientSideMessage("stealing");
            for (int i = 0; i < stealPerTick.getValue(); i++) {
                if (stealPos.isEmpty()) return;
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, stealPos.poll(), 0, SlotActionType.QUICK_MOVE, mc.player);
            }
        }



    }

    public void getStealIndex() {
        String screenName = mc.currentScreen.getTitle().getString();
        if (screenName.equalsIgnoreCase("chest") || screenName.equalsIgnoreCase("Shulker") || screenName.equalsIgnoreCase("barrel") || screenName.equalsIgnoreCase("enderchest")) {
                        for (int i = 0; i < mc.player.currentScreenHandler.slots.size() - 36; i++) {
                if (!mc.player.currentScreenHandler.slots.get(i).getStack().getItem().equals(Items.AIR)) {
                stealPos.add(i);
                }
            }
            if (stealPos.isEmpty() && closeOnEmpty.getValue()) {
                mc.player.closeHandledScreen();
               // mc.currentScreen = null;
            }

        }
    }
}
