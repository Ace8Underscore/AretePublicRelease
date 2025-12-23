package com.cousinware.arete.module.Client;

import com.cousinware.arete.command.Command;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.IntSetting;
import com.cousinware.arete.utils.settings.ModeSetting;

public class Test extends Module {

    IntSetting settingInt = new IntSetting();
    BoolSetting settingBool = new BoolSetting();
    ModeSetting modeSetting = new ModeSetting();

    public Test() {
        super("Test", Category.Client, -1, "test");

        settingInt.setMin(0).setValue(5).setMax(10).setName("Penis").build(this);
        settingBool.setName("Flag").setValue(false).build(this);
        modeSetting.setModes("Ace", "Sago", "Rat").setValue("Ace").setName("benis").build(this);
    }

    @Override
    public void onUpdate() {

        Command.sendClientSideMessage("1");
        int i = mc.textRenderer.fontHeight;
    }

    @Override
    public void onEnable() {
        Command.sendClientSideMessage("On");

    }

    @Override
    public void onDisable() {
        Command.sendClientSideMessage("Off");
    }
}
