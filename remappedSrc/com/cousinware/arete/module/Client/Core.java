package com.cousinware.arete.module.Client;

import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.texture.Capes;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.IntSetting;
import com.cousinware.arete.utils.settings.ModeSetting;

public class Core extends Module {

    public static BoolSetting capes = new BoolSetting();
    public static ModeSetting cape = new ModeSetting();
    public static BoolSetting customFont = new BoolSetting();
    public static BoolSetting moveFix = new BoolSetting();
    public static BoolSetting t2 = new BoolSetting();
    public static BoolSetting t3 = new BoolSetting();
    public static IntSetting fontQuality = new IntSetting();

    public Core() {
        super("Core", Category.Client, -1);

        capes.setName("Capes").setValue(true).build(this);
        cape.setName("Cape").setValue(Capes.getCapesBeforeLaunch()[0]).setModes(Capes.getCapesBeforeLaunch()).build(this);
        customFont.setName("CustomFont").setValue(true).build(this);
        moveFix.setName("MoveFix").setValue(true).build(this);
        t2.setName("t2").setValue(true).build(this);
        t3.setName("t3").setValue(true).build(this);
        fontQuality.setName("FontQuality").setMin(0).setMax(256).setValue(16).build(this);
    }
}
