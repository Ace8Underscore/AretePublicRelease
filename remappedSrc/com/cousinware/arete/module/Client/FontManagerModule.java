package com.cousinware.arete.module.Client;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;

public class FontManagerModule extends Module {


    //IntSetting fontSize = new IntSetting();
    public FontManagerModule() {
        super("FontManager", Category.Client, -1, "Opens the FontManager");
    }

    public void onEnable() {


        mc.setScreen(AreteClient.fontGui);
        this.disable();
    }
}
