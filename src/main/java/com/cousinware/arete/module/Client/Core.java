package com.cousinware.arete.module.Client;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.BoolSettingContainer;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.cousinware.arete.utils.texture.Capes;

public class Core extends Module {

    public static BoolSettingContainer capes = new BoolSettingContainer();
    public static ModeSetting cape = new ModeSetting();
    public static BoolSetting customFont = new BoolSetting();
    public static BoolSetting moveFix = new BoolSetting();
    public static BoolSetting customTitle = new BoolSetting();
    public static BoolSetting bookEditor = new BoolSetting();


    public Core() {
        super("Core", Category.Client, -1);

        capes.setName("Capes").setValue(true).build(this);
        cape.setName("Cape").setValue(Capes.getCapesBeforeLaunch()[0]).setModes(Capes.getCapesBeforeLaunch()).build(this, capes, "CoreCapesOptions");
        customFont.setName("CustomFont").setValue(true).build(this).setDisableAction(() -> {
            AreteClient.fontManager.setAltFont(AreteClient.fontManager.selectedFont);
            AreteClient.fontManager.setSelectedFont("minecraft");
        });
        customFont.setEnableAction(() -> {
            AreteClient.fontManager.setSelectedFont(AreteClient.fontManager.altFont);
        });
        moveFix.setName("MoveFix").setValue(true).build(this);
        customTitle.setName("CustomTitle").setValue(false).setDescription("Changes Minecraft Title at Top Left").build(this);
        bookEditor.setName("BookEditorGUI").setValue(true).setDescription("Adds color and Formatting GUI to book n quil").build(this);
//        discordRPC.setDisableAction(() -> {
//            AreteClient.discordPresence.stop();
//        });
//        discordRPC.setEnableAction(() -> {
//            if (AreteClient.discordPresence == null && AreteClient.currentMember != null) AreteClient.startDiscord();
//        });
        setDrawn(false);
    }
}
