package com.cousinware.arete.utils.guis.clickgui.defaultguis.widgets;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.KeyPressedEvent;
import com.cousinware.arete.events.event.MouseButtonEvent;
import com.cousinware.arete.utils.Timer;
import com.cousinware.arete.utils.guis.clickgui.Widget;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.maingui.AreteGui;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;

import java.awt.*;

public class BindWidget extends Widget {

    ModuleWidget moduleWidget;
    boolean binding = false;
    int x;
    int y;
    int yOffset;
    Timer timer;


    public BindWidget(ModuleWidget moduleWidget, int yOffset) {
        timer = new Timer(5);
        this.moduleWidget = moduleWidget;
        this.yOffset = yOffset * moduleWidget.parent.height;
        AreteClient.eventBus.register(this);

    }

    @Subscribe
    public void onMousePress(MouseButtonEvent event) {
        if (MinecraftClient.getInstance().currentScreen != AreteClient.areteGui) return;
        if (!timer.canTick()) return;
        double mouseX = AreteGui.mouseX;
        double mouseY = AreteGui.mouseY;

        if (isCollided((int) mouseX, (int) mouseY) && moduleWidget.isOpened()) {
            if (event.getButton() == 0 && event.getAction() == 1)  {
                binding = true;
            }

        } else {
            binding = false;
        }
    }

    @Subscribe
    public void onMousePress(KeyPressedEvent event) {
        if (MinecraftClient.getInstance().currentScreen != AreteClient.areteGui) return;
        if (!timer.canTick()) return;
        double mouseX = AreteGui.mouseX;
        double mouseY = AreteGui.mouseY;

        if (binding) {
            System.out.println(event.getKey());
            if (event.getKey() == 261 || event.getKey() == 256 || event.getKey() == 259) moduleWidget.module.setKeybind(-9991);
            else moduleWidget.module.setKeybind(event.getKey());
            binding = false;
        }
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

    @Override
    public void render(DrawContext drawContext) {
        Color color = AreteGui.color;
        x = moduleWidget.getX();
        y = moduleWidget.getY() + moduleWidget.parent.height + yOffset;
        drawGrayBox(drawContext);

        //drawContext.drawVerticalLine(x + 3, y, y + moduleWidget.parent.height, color.brighter().brighter().getRGB());
        drawContext.fill(x + 4, y + 2, x + moduleWidget.parent.width - 4, y + moduleWidget.parent.height - 1, color.brighter().getRGB());
        //draw Box
        //top side
        //drawContext.drawHorizontalLine(x, x + moduleWidget.parent.width, y, color.getRGB());

        //right side
        //drawContext.drawVerticalLine(x + moduleWidget.parent.width, y, y + moduleWidget.parent.height, color.getRGB());

        String s;
        String translation = InputUtil.fromKeyCode(moduleWidget.module.getKeybind(), 0).getTranslationKey();

        try {
            if (translation.split("\\.")[2].equalsIgnoreCase("-9991")) s = "None";
            else s = translation.split("\\.")[2].toUpperCase();
        } catch (Exception e) {
            s = "None";
        }

        if (!binding) {
            AreteClient.fontManager.drawText(drawContext, "KeyBind", x + 6, y + 4);
            AreteClient.fontManager.drawLeftStringText(drawContext,AreteClient.fontManager.selectedFont, s, x + moduleWidget.parent.width - 6, y + 4);
        }
        else AreteClient.fontManager.drawText(drawContext, "Listening...", x + 6, y + 4);


       // if (!binding) drawContext.drawTextWithShadow(AreteClient.fontManager.getFont(), "KeyBind: " + s, x + 8, y + 1, Color.WHITE.getRGB());
        //else drawContext.drawTextWithShadow(AreteClient.fontManager.getFont(), "Listening...", x + 8, y + 1, Color.WHITE.getRGB());
        //bottom side
        //drawContext.drawHorizontalLine(x, x + moduleWidget.parent.width, y + moduleWidget.parent.height, color.getRGB());
    }

    public boolean isCollided(int mouseX, int mouseY) {
        return (mouseX  <= x + moduleWidget.parent.width - 4 && mouseX >= x + 4) && (mouseY<= y + moduleWidget.parent.height - 1 && mouseY >= y);
    }
}
