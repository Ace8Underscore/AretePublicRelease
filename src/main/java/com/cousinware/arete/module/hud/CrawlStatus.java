package com.cousinware.arete.module.hud;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RenderOverlayEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.ColorUtils;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.hudgui.HudInterface;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.string.coloredstring.ColoredString;
import com.cousinware.arete.utils.settings.BoolSettingContainer;
import com.cousinware.arete.utils.settings.ModeSetting;
import net.minecraft.entity.EntityPose;

public class CrawlStatus extends Module implements HudInterface {

    int width = 0;
    int height = 0;

    BoolSettingContainer colored = new BoolSettingContainer();
    ModeSetting standingColor = new ModeSetting();
    ModeSetting crawlingColor = new ModeSetting();

    public CrawlStatus() {
        super("CrawlStatus", Category.Hud, -1);
        colored.setName("Color").setValue(true).build(this);
        standingColor.setName("Stand").setValue(ColorUtils.Colors.Green.name()).setModes(ColorUtils.colors()).build(this, colored, "StandingColorColored");
        crawlingColor.setName("Crawl").setValue(ColorUtils.Colors.Red.name()).setModes(ColorUtils.colors()).build(this, colored, "CrawlingColorColored");

    }

    public void onEnable() {

    }

    @Override
    public void render(RenderOverlayEvent context) {
        drawOutline(this, context.getContext());
        String message;
        if (mc.player.isCrawling() || mc.player.isSwimming() || mc.player.isInPose(EntityPose.SWIMMING) || mc.player.isInPose(EntityPose.GLIDING))
            message = "Crawling";
        else message = "Standing";

        width = (int) AreteClient.fontManager.getStringsWidth(new ColoredString(AreteClient.getClientColor(), message));
        ColoredString coloredString;
        if (colored.getValue())
            coloredString = ColoredString.of(message.equalsIgnoreCase("standing") ? ColorUtils.stringToColor(standingColor.getValue()) : ColorUtils.stringToColor(crawlingColor.getValue()), message);
        else coloredString = ColoredString.of(AreteClient.getClientColor(), message);
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
