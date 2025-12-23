package com.cousinware.arete.module.hud;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RenderOverlayEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.hudgui.HudInterface;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.string.RevealingString;
import com.cousinware.arete.utils.rendering.nvg.string.coloredstring.ColoredString;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.ModeSetting;

public class Watermark extends Module implements HudInterface {

    ModeSetting textMode = new ModeSetting();
    BoolSetting metaData = new BoolSetting();
    BoolSetting revealingString = new BoolSetting();

    RevealingString message = new RevealingString();
    int width = 0;
    int height = 0;

    public Watermark() {
        super("WaterMark", Category.Hud, -1, "Watamak");
        textMode.setValue("WeWide").setModes("WeWide", "Classic", "Secret").setName("Mode").build(this);
        metaData.setValue(true).setName("MetaData").build(this);
        metaData.setToggleAction(() -> {
            message = new RevealingString();
        });
        revealingString.setName("Reveal").setValue(true).build(this);
        setDrawn(false);
    }

    @Override
    public void render(RenderOverlayEvent context) {
        drawOutline(this, context.getContext());

        switch (textMode.getValue()) {
            case "WeWide" -> message.setFullString("We Wide Inc.");
            case "Secret" -> message.setFullString("CousinWare");
            default -> message.setFullString("Arete");
        }

        if (metaData.getValue()) message.appendToFullString(" - " + AreteClient.VERSION);

        ColoredString coloredString = ColoredString.of(AreteClient.getClientColor(), revealingString.getValue() ? message.getTextAndTick() : message.getFullString());
        width = (int) AreteClient.fontManager.getStringsWidth(coloredString);

        NVGContext.render(nvg -> {
            AreteClient.fontManager.renderTextMultiColor(context.getContext(), (int) getXSetting().getValue().floatValue(), (int) getYSetting().getValue().floatValue(), true, getTextOrdering(this), coloredString);

        });

        height = (int) AreteClient.fontManager.getFontHeight(AreteClient.fontManager.selectedFont);
    }

    @Override
    public int[] hitBox() {
        return new int[]{width, height};
    }
}
