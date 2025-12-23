package com.cousinware.arete.module.hud;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RenderOverlayEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.hudgui.HudInterface;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.string.coloredstring.ColoredString;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.cousinware.arete.utils.settings.StringSetting;
import net.minecraft.client.MinecraftClient;

import java.awt.*;
import java.util.Calendar;

public class Welcomer extends Module implements HudInterface {

    ModeSetting nameMode;
    ModeSetting welcomeMode;
    StringSetting customMessage = new StringSetting();
    int width = 0;
    int height = 0;


    int uid = 0;

    public Welcomer() {
        super("Greeter", Category.Hud, -1, "");
        nameMode = new ModeSetting().setName("Alias").setValue("Name").setModes("Name", "UID").build(this);
        welcomeMode = new ModeSetting().setName("Mode").setValue("Festive").setModes("Festive", "Time", "Custom").build(this);
        welcomeMode.setChangeMode(() -> {
            customMessage.setShown(welcomeMode.getValue().equalsIgnoreCase("Custom"));
        });
        customMessage.setName("Text").setValue("Edit Me").build(this);
        setDrawn(false);
    }

    @Override
    public void render(RenderOverlayEvent context) {

        drawOutline(this, context.getContext());
        String timeMessage = "";
        long time = Calendar.getInstance().getTime().getHours();
        Color c = AreteClient.getClientColor();
        if (time >= 0 && time <= 11) timeMessage = "Good Morning ";
        if (time > 11 && time <= 18) timeMessage = "Good Afternoon ";
        if (time > 18 && time < 24) timeMessage = "Good Night ";
        ColoredString welcomeMessage = welcomeMode.getValue().equalsIgnoreCase("Festive") ? ColoredString.of(Color.ORANGE, "Happy Halloween ") : ColoredString.of(AreteClient.getClientColor(), timeMessage);
        if (welcomeMode.getValue().equalsIgnoreCase("custom"))
            welcomeMessage = ColoredString.of(AreteClient.getClientColor(), customMessage.getValue());
        Color mc = AreteClient.getClientColor();

        ColoredString nameMessage = nameMode.getValue().equalsIgnoreCase("Name") ? ColoredString.of(AreteClient.getClientColor(), MinecraftClient.getInstance().player.getName().getString()) : ColoredString.of(AreteClient.getClientColor(), "Uid " + "0");

        ColoredString finalWelcomeMessage = welcomeMessage;
        NVGContext.render(vg -> {
            AreteClient.fontManager.renderTextMultiColor(context.getContext(), (int) getXSetting().getValue().floatValue(), (int) getYSetting().getValue().floatValue(), true, getTextOrdering(this), finalWelcomeMessage, nameMessage);
        });

        width = (int) AreteClient.fontManager.getStringsWidth(welcomeMessage, nameMessage);
        height = (int) AreteClient.fontManager.getFontHeight(AreteClient.fontManager.selectedFont);
    }

    @Override
    public int[] hitBox() {
        return new int[]{width, height};
    }
}
