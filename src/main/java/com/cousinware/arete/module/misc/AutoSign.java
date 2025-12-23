package com.cousinware.arete.module.misc;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.game.BlockInteractionHelper;
import com.cousinware.arete.utils.game.InventoryUtils;
import com.cousinware.arete.utils.game.PlayerUtils;
import com.cousinware.arete.utils.settings.BoolSettingContainer;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.cousinware.arete.utils.settings.StringSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import net.minecraft.network.packet.s2c.play.SignEditorOpenS2CPacket;
import net.minecraft.util.math.BlockPos;

public class AutoSign extends Module {


    BoolSettingContainer lineBoolContainer = new BoolSettingContainer();
    ModeSetting modeSetting = new ModeSetting();
    StringSetting line1 = new StringSetting();
    StringSetting line2 = new StringSetting();
    StringSetting line3 = new StringSetting();
    StringSetting line4 = new StringSetting();

    public AutoSign() {
        super("AutoSign", Category.Misc, -1, "Automatically places signs - Etbes Idea");
        modeSetting.setName("Mode").setModes("Auto", "Toggle").setValue("Auto").build(this);
        lineBoolContainer.setName("Text").setDescription("Chooses if you want to write text to signs").setValue(true).build(this);
        line1.setValue("Edit Me!").setName("L1").build(this, lineBoolContainer, "AutoSignLine1StringSetting");
        line2.setValue("Edit Me!").setName("L2").build(this, lineBoolContainer, "AutoSignLine2StringSetting");
        line3.setValue("Edit Me!").setName("L3").build(this, lineBoolContainer, "AutoSignLine3StringSetting");
        line4.setValue("Edit Me!").setName("L4").build(this, lineBoolContainer, "AutoSignLine4StringSetting");
        setSaveToConfig(false);

    }


    @Subscribe
    public void packetListener(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SignEditorOpenS2CPacket packet) {
            if (modeSetting.getValue().equalsIgnoreCase("Auto")) {
                AreteClient.packetManager.queuePacketDelay(10, new UpdateSignC2SPacket(packet.getPos(), true, line1.getValue(), line2.getValue(), line3.getValue(), line4.getValue()));
                event.setCancelled(true);
            }

        }
    }

    public void onEnable() {
        if (modeSetting.getValue().equalsIgnoreCase("Toggle")) {
            InventoryUtils.HotBarTask task = new InventoryUtils.HotBarTask(true, Items.ACACIA_SIGN, Items.BAMBOO_SIGN, Items.SPRUCE_SIGN, Items.BIRCH_SIGN, Items.CRIMSON_SIGN, Items.JUNGLE_SIGN, Items.WARPED_SIGN, Items.OAK_SIGN, Items.CHERRY_SIGN, Items.DARK_OAK_SIGN, Items.MANGROVE_SIGN);
            BlockPos pos = PlayerUtils.getLookingPos();
            BlockInteractionHelper.placeBlock(pos, pos.toCenterPos());
            if (lineBoolContainer.getValue())
                AreteClient.packetManager.queuePacketDelay(10, new UpdateSignC2SPacket(pos, true, line1.getValue(), line2.getValue(), line3.getValue(), line4.getValue()));
            task.swapBack(true);
            this.toggle();
        }
    }

}
