package com.cousinware.arete.module.hud;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RenderOverlayEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.hudgui.HudInterface;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.string.coloredstring.ColoredString;
import com.cousinware.arete.utils.settings.ModeSetting;

import java.awt.*;

public class Speed extends Module implements HudInterface {

    int width = 0;
    ModeSetting speedMode = new ModeSetting();
    ModeSetting calcMode = new ModeSetting();

    double lastTickVelocity = 0;
    double lastAccel = 0;


    public Speed() {
        super("Speed", Category.Hud, -1, "Shows Player Current Speed");
        speedMode.setName("Unit").setValue("bp/s").setModes("bp/s", "km/h").build(this);
        calcMode.setName("Mode").setValue("2D").setModes("3D", "2D").setDescription("Chooses if Y axis will be calculated in speed").build(this);
        setDrawn(false);

    }

    @Override
    public void render(RenderOverlayEvent context) {
        drawOutline(this, context.getContext());
        double distTraveledLastTickX = mc.player.getX() - mc.player.lastX;
        double distTraveledLastTickZ = mc.player.getZ() - mc.player.lastZ;
        double distTraveledLastTickY = mc.player.getY() - mc.player.lastY;
        double sped = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;

        if (calcMode.getValue().equalsIgnoreCase("3D")) sped += distTraveledLastTickY * distTraveledLastTickY;

        sped = simplifySpeed(convert(sped));

        ColoredString speed = ColoredString.of(AreteClient.getClientColor(), String.valueOf(sped));
        ColoredString suffix = ColoredString.of(Color.GRAY, speedMode.getValue().equalsIgnoreCase("bp/s") ? "bp/s" : "km/h");


        NVGContext.render(vg -> {
            AreteClient.fontManager.renderTextMultiColor(context.getContext(), (int) getXSetting().getValue().floatValue(), (int) getYSetting().getValue().floatValue(), true, getTextOrdering(this), speed, suffix);
        });
        width = (int) AreteClient.fontManager.getStringsWidth(speed, suffix);


        lastTickVelocity = sped;

    }

    public double convert(double input) {
        if (speedMode.getValue().equals("bp/s")) return Math.sqrt(input) * 20;
        return Math.sqrt((float) input) * 71.2729367892;
    }

    public double simplifySpeed(double input) {
        return (double) Math.round(10.0 * input) / 10.0;

    }

    @Override
    public int[] hitBox() {
        return new int[]{width, (int) AreteClient.fontManager.getFontHeight(AreteClient.fontManager.selectedFont)};
    }
}
