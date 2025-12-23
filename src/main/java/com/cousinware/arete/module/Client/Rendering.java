package com.cousinware.arete.module.Client;

import com.cousinware.arete.command.Command;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.rendering.Renderer3D;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.DoubleSetting;

public class Rendering extends Module {
    public static DoubleSetting outlineLineThickness = new DoubleSetting();
    public static BoolSetting legitRendering = new BoolSetting();
    public static BoolSetting glow = new BoolSetting();

    public Rendering() {
        super("Rendering", Category.Client, -1, "Controls rendering options for client");
        outlineLineThickness.setName("LineThickness").setValue(1.5).setMin(1).setMax(5).setDescription("Changes the Thickness of lines drawn by the client").build(this).setModifyAction(() -> {
            if (mc.world != null) Renderer3D.loadRenderLayers();
        });
        legitRendering.setValue(false).setName("LegitRender").setDescription("Doesnt render through walls").build(this).setToggleAction(() -> {
            if (mc.world != null) Renderer3D.loadRenderLayers();
        });
        legitRendering.setEnableAction(() -> {
            if (glow.getValue()) glow.setValue(false);
        });
        glow.setName("Glow").setValue(true).build(this).setToggleAction(() -> {
            if (mc.world != null) Renderer3D.loadRenderLayers();
        });
        glow.setEnableAction(() -> {
            if (legitRendering.getValue()) {
                glow.setValue(false);
                Command.sendClientSideMessage("Glow does not work with Legit Rendering", false);
            }

        });
    }
}
