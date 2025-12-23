package com.cousinware.arete.module.misc;

import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.IntSetting;

public class ExtraTab extends Module {

    public static IntSetting playersShown = new IntSetting();

    public ExtraTab() {
        super("ExtraTab", Category.Misc, 15818280);
        playersShown.setMin(1).setValue(100).setMax(500).setName("Size").build(this);
    }
}
