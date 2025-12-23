package com.cousinware.arete.module.render;

import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.DoubleSetting;

public class NoRender extends Module {

    public static BoolSetting noChatWarning = new BoolSetting();
    public static DoubleSetting wardenDistance = new DoubleSetting();
    public NoRender() {
        super("NoRender", Category.Render, 16136516);
        noChatWarning.setName("NoChatWarn").setValue(true).build(this);
        wardenDistance.setName("WardenDis").setMin(0).setValue(20).setMax(250).build(this);
    }

    public void onUpdate() {

    }


}
