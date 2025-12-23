package com.cousinware.arete.utils.guis.clickgui.defaultguis.widgets;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.MouseButtonEvent;
import com.cousinware.arete.utils.Timer;
import com.cousinware.arete.utils.guis.clickgui.Widget;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.maingui.AreteGui;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class ModeSettingWidget extends Widget {

    ModuleWidget moduleWidget;
    ModeSetting setting;
    int x;
    int y;
    int yOffset;
    Timer timer;


    public ModeSettingWidget(ModuleWidget moduleWidget, ModeSetting setting, int yOffset) {
        timer = new Timer(10);
        this.moduleWidget = moduleWidget;
        this.setting = setting;
        this.yOffset = yOffset * moduleWidget.parent.height;
        AreteClient.eventBus.register(this);

    }

    @Subscribe
    public void onMousePress(MouseButtonEvent event) {
        if (MinecraftClient.getInstance().currentScreen != AreteClient.areteGui) return;
        if (!timer.canTick()) return;
        double mouseX = AreteGui.mouseX;
        double mouseY = AreteGui.mouseY;

        if (isCollided((int) mouseX, (int) mouseY) && moduleWidget.isOpened() && event.getButton() == 0 && event.getAction() == 1) {
            setting.advance();

        }
    }

    @Override
    public void render(DrawContext drawContext) {
        Color color = AreteGui.color;
        x = moduleWidget.getX();
        y = moduleWidget.getY() + moduleWidget.parent.height + yOffset;
        drawGrayBox(drawContext);

        //drawContext.drawVerticalLine(x + 3, y, y + moduleWidget.parent.height, color.brighter().brighter().getRGB());

        //draw Box
        drawContext.fill(x + 4, y + 2, x + moduleWidget.parent.width - 4, y + moduleWidget.parent.height - 1, color.brighter().getRGB());

        //top side
        //drawContext.drawHorizontalLine(x, x + moduleWidget.parent.width, y, color.getRGB());

        //right side
        //drawContext.drawVerticalLine(x + moduleWidget.parent.width, y, y + moduleWidget.parent.height, color.getRGB());
        //drawContext.drawTextWithShadow(AreteClient.fontManager.getFont(), setting.getName(), x + 8, y + 2, Color.WHITE.getRGB());
        AreteClient.fontManager.drawText(drawContext, setting.getName(), x + 6, y + 4);
        AreteClient.fontManager.drawLeftStringText(drawContext, AreteClient.fontManager.selectedFont, setting.getValue(), x - 6 + moduleWidget.parent.width, y + 4);
        //FontUtils.drawLeftStringWithShadow(drawContext, setting.getValue(), x - 6 + moduleWidget.parent.width, y + 2, Color.WHITE.getRGB());
        //NanoVG.nvgText(MinecraftClient.getInstance().getRenderTime(), 100, 100, "Penis Over Here");
        //bottom side
        //drawContext.drawHorizontalLine(x, x + moduleWidget.parent.width, y + moduleWidget.parent.height, color.getRGB());
    }

    public boolean isCollided(int mouseX, int mouseY) {
        return (mouseX  <= x + moduleWidget.parent.width - 4 && mouseX >= x + 4) && (mouseY<= y + moduleWidget.parent.height - 1 && mouseY >= y);
    }

    public void drawGrayBox(DrawContext drawContext) {
        Color gray = AreteGui.gray;
        //if (moduleWidget.settingWidgets.get(0) == this && !moduleWidget.settingWidgets.get(moduleWidget.settingWidgets.size() - 1).equals(this)) drawContext.fill(x, y, x + moduleWidget.parent.width, y + 2, gray.getRGB());

        //leftSide Fill
        drawContext.fill(x, y + 2, x + 4, y + moduleWidget.parent.height - 1, gray.getRGB());



        //rightSide Fill
        drawContext.fill(x + moduleWidget.parent.width - 4, y + 2, x + moduleWidget.parent.width, y + moduleWidget.parent.height  - 1, gray.getRGB());

        //Middle Bottom Fill
        drawContext.fill(x, y + moduleWidget.parent.height - 1, x + moduleWidget.parent.width, y + moduleWidget.parent.height + 2, gray.getRGB());
    }
}
