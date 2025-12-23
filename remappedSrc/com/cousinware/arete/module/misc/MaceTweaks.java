package com.cousinware.arete.module.misc;

import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.IntSetting;

public class MaceTweaks extends Module {

    public static BoolSetting bypassDelay = new BoolSetting();
    public static IntSetting damageSpoof = new IntSetting();

    public MaceTweaks() {
        super("MaceTweaks", Category.Misc, 1);
        bypassDelay.setName("BypassDelay").setValue(false).build(this);
        damageSpoof.setName("DamageSpoof").setMin(0).setValue(10).setMax(100).build(this);
    }
}
