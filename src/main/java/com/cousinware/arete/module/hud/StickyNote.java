package com.cousinware.arete.module.hud;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RenderOverlayEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.hudgui.HudInterface;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.string.coloredstring.ColoredString;
import com.cousinware.arete.utils.settings.StringSetting;

import java.awt.*;

public class StickyNote extends Module implements HudInterface {

    StringSetting message = new StringSetting();
    int width = 0;
    int height = 0;

    public StickyNote() {
        super("StickyNote", Category.Hud, -1, "Custom Messages on Screen!");
        message.setValue("Edit Me!").setName("Text").build(this);
        setDrawn(false);


    }

    @Override
    public void render(RenderOverlayEvent context) {
        drawOutline(this, context.getContext());
        ColoredString coloredString = ColoredString.of(AreteClient.getClientColor(), message.getValue());
        Color blue = new Color(31, 241, 255, 255);
        NVGContext.render(vg -> {

            AreteClient.fontManager.renderTextMultiColor(context.getContext(), (int) getXSetting().getValue().floatValue(), (int) getYSetting().getValue().floatValue(), true, getTextOrdering(this), coloredString);
        });
        width = (int) AreteClient.fontManager.getStringsWidth(coloredString);
        height = (int) AreteClient.fontManager.getFontHeight(AreteClient.fontManager.selectedFont);
    }

    @Override
    public int[] hitBox() {
        return new int[]{width, height};
    }


}
