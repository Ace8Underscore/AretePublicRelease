package com.cousinware.arete.module.misc;

import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.IntSetting;

public class ExtraTab extends Module {

    public static IntSetting playersShown = new IntSetting();
    public static IntSetting rowLength = new IntSetting();
    public static BoolSetting showPing = new BoolSetting();
    public static BoolSetting magic = new BoolSetting();

    public ExtraTab() {
        super("ExtraTab", Category.Misc, 15818280);
        playersShown.setMin(1).setValue(100).setMax(500).setName("Size").build(this);
        rowLength.setMin(1).setValue(20).setMax(100).setName("RowSize").build(this).setShown(true);
        showPing.setValue(true).setName("ShowPing").build(this);
        magic.setValue(true).setName("HackerMan Visual").build(this);

    }

}
