package com.cousinware.arete.module.render;

import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.BoolSetting;

public class NoRender extends Module {


    public static BoolSetting noChatWarning = new BoolSetting();
    public static BoolSetting mapDecorations = new BoolSetting();
    public static BoolSetting maps = new BoolSetting();

    public NoRender() {
        super("NoRender", Category.Render, 16136516);
        noChatWarning.setName("NoChatWarn").setValue(true).build(this);
        mapDecorations.setName("MapMarkers").setValue(true).build(this);
        maps.setName("Maps").setValue(false).build(this);
    }


}
