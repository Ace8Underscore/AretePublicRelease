package com.cousinware.arete.module.hud;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RenderOverlayEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.hudgui.HudInterface;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.string.coloredstring.ColoredString;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class ActiveModuleList extends Module implements HudInterface {

    ArrayList<ColoredString[]> moduleArrayList = new ArrayList<>();
    ArrayList<ColoredString[]> sortedList = new ArrayList<>();
    int finalYOffset = 0;
    int finalXOffset = 0;
    boolean reversed = false;

    public ActiveModuleList() {
        super("ModuleList", Category.Hud, -1, "List out the active modules");
        setDrawn(false);
    }

    public void onUpdate() {
        moduleArrayList = new ArrayList<>();
        sortedList = new ArrayList<>();


        for (Module module : AreteClient.moduleManager.getModules()) {
            if (!module.isEnabled()) continue;
            if (!module.isDrawn()) continue;
            ColoredString[] coloredString = new ColoredString[module.getHudInfo() != null ? 4 : 1];
            coloredString[0] = ColoredString.of(AreteClient.getClientColor(), module.getName());
            if (module.getHudInfo() != null) {
                coloredString[1] = ColoredString.of(Color.GRAY, "[");
                coloredString[2] = module.getHudInfo();
                coloredString[3] = ColoredString.of(Color.GRAY, "]");
            }

            moduleArrayList.add(coloredString);

        }

        sortedList = sort(moduleArrayList);
        if (sortedList.isEmpty()) return;
        finalXOffset = (int) AreteClient.fontManager.getStringsWidth(sortedList.getLast());

        reversed = getYSetting().getValue() < mc.getWindow().getScaledHeight() / 2;
        if (reversed) {
            Collections.reverse(sortedList);

        }

    }

    public ArrayList<ColoredString[]> sort(ArrayList<ColoredString[]> moduleArrayList) {
        ArrayList<ColoredString[]> modList = new ArrayList<>();
        int initialSize = moduleArrayList.size();
        while (modList.size() != initialSize) {
            ColoredString[] smallestCS = null;
            for (int i = 0; i < moduleArrayList.size(); i++) {
                if (smallestCS == null) smallestCS = moduleArrayList.getFirst();
                else {
                    if (AreteClient.fontManager.getStringsWidth(moduleArrayList.get(i)) < AreteClient.fontManager.getStringsWidth(smallestCS)) {
                        smallestCS = moduleArrayList.get(i);
                    }
                }

            }
            modList.add(smallestCS);
            moduleArrayList.remove(smallestCS);

        }
        return modList;
    }

    @Override
    public void render(RenderOverlayEvent context) {
        drawOutline(this, context.getContext());

        if (sortedList.isEmpty()) return;

        AtomicInteger offset = new AtomicInteger(0);
        for (ColoredString[] coloredStrings : sortedList) {
            int y = (int) (offset.getAcquire() * AreteClient.fontManager.getFontHeight(AreteClient.fontManager.selectedFont));
            NVGContext.render(nvg -> {
                AreteClient.fontManager.renderTextMultiColor(context.getContext(), (int) getXSetting().getValue().floatValue(), (int) (getYSetting().getValue().floatValue() + y), true, getTextOrdering(this), coloredStrings);
            });
            offset.getAndIncrement();

        }


        finalYOffset = (int) (offset.get() * AreteClient.fontManager.getFontHeight(AreteClient.fontManager.selectedFont));


    }

    @Override
    public int[] hitBox() {
        return new int[]{finalXOffset, finalYOffset};
    }
}
