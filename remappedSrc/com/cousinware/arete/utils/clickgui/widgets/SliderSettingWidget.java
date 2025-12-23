package com.cousinware.arete.utils.guis.clickgui.defaultguis.widgets;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.MouseButtonEvent;
import com.cousinware.arete.utils.Timer;
import com.cousinware.arete.utils.guis.clickgui.Widget;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.maingui.AreteGui;
import com.cousinware.arete.utils.settings.DoubleSetting;
import com.cousinware.arete.utils.settings.IntSetting;
import com.cousinware.arete.utils.settings.Setting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class SliderSettingWidget extends Widget {

    ModuleWidget moduleWidget;
    Setting setting;
    int x;
    int y;
    double barLengthXMod;
    double barLengthX;
    double mouseX = 0;
    double mouseY = 0;
    boolean dragging = false;
    double max;
    double min;
    double value;
    int yOffset;
    Timer timer;

    public SliderSettingWidget(ModuleWidget moduleWidget, IntSetting setting, int yOffset) {
        timer = new Timer(5);
        this.moduleWidget = moduleWidget;
        this.setting = setting;
        this.yOffset = yOffset * moduleWidget.parent.height;
        barLengthXMod = (x + moduleWidget.parent.width - 4) - x - 4;
        AreteClient.eventBus.register(this);
        max = setting.getMax();
        min = setting.getMin();
        this.value = setting.getValue();
    }

    public SliderSettingWidget(ModuleWidget moduleWidget, DoubleSetting setting, int yOffset) {
        timer = new Timer(5);
        this.moduleWidget = moduleWidget;
        this.setting = setting;
        this.yOffset = yOffset * moduleWidget.parent.height;
        barLengthXMod = (x + moduleWidget.parent.width - 4) - x - 4;
        AreteClient.eventBus.register(this);
        max = setting.getMax();
        min = setting.getMin();
        this.value = setting.getValue();
    }

    @Subscribe
    public void onMousePress(MouseButtonEvent event) {
        if (MinecraftClient.getInstance().currentScreen != AreteClient.areteGui) return;
        mouseX = AreteGui.mouseX;
        mouseY = AreteGui.mouseY;


        if (isCollided((int) mouseX, (int) mouseY) && moduleWidget.isOpened() && event.getButton() == 0) {
            if (event.getAction() == 1) dragging = true;
            if (event.getAction() == 0) dragging = false;

            if (dragging) {
            }
        }
        else {
            dragging = false;
        }
    }

    @Override
    public void render(DrawContext drawContext) {
        //if (dragging) setting.setValue(t * setting.getMax());
        //System.out.println((int) (t * setting.getMax()));

        this.y = moduleWidget.getY() + moduleWidget.parent.height + yOffset;
        this.x = moduleWidget.getX() + 4;
        //TODO FIX SO SETTINGS MOVE SMOOOTH
        if (setting instanceof IntSetting) barLengthX = (( (int) setting.getValue() / max) * barLengthXMod);
        else  barLengthX = (((double) setting.getValue() / max) * barLengthXMod);
        drawGrayBox(drawContext);


        //System.out.println(dragging);
        if (dragging) {
            double distanceFromFarX = AreteGui.mouseX - x;
            //double distanceFromFarX = (x + moduleWidget.parent.width - 8) - mouseX;
            int barLeng = (x + moduleWidget.parent.width - 8) - x;
            double factor = distanceFromFarX / barLeng;
            double value = factor * (setting instanceof IntSetting ? (int) max : max);
            //System.out.println(value);



            if (value > max) value = max;
            else if (value < min) value = min;

            if (setting instanceof IntSetting) {
                setting.setValue((int)value);
            } else if (setting instanceof DoubleSetting) {
                double dubVal = Math.round(value * 10.0) / 10.0;
                setting.setValue(dubVal);
            }
        }
       // System.out.println(dragging);
        drawContext.fill(x , y + 2, (int) (x + 4 + barLengthX) - 4, y + moduleWidget.parent.height - 2, AreteGui.color.brighter().getRGB());
        drawContext.fill((int) (x + 4 + barLengthX) - 4 , y + 2, (int) (x + moduleWidget.parent.width - 8), y + moduleWidget.parent.height - 2, AreteGui.color.darker().getRGB());

        //drawContext.drawTextWithShadow(AreteClient.fontManager.getFont(), setting.getName(), x + 2, y + 1, Color.WHITE.getRGB());

        AreteClient.fontManager.drawText(drawContext, setting.getName(), x + 2, y + 3);

        //AreteClient.fontManager.renderText(drawContext, setting.getValue().toString(), x + 20, y + 3);
        AreteClient.fontManager.drawLeftStringText(drawContext, AreteClient.fontManager.selectedFont, String.valueOf(setting.getValue()), (x + moduleWidget.parent.width - 9), y + 3);

        //FontUtils.drawLeftStringWithShadow(drawContext, String.valueOf(setting.getValue()), (x + moduleWidget.parent.width - 9), y + 1, Color.WHITE.getRGB());
        //drawContext.drawTextWithShadow(AreteClient.fontManager.getFont(), String.valueOf(setting.getValue()), (x + moduleWidget.parent.width - 8), y + 2, Color.WHITE.getRGB());


    }

    public void drawGrayBox(DrawContext drawContext) {

        //leftSide Fill
        drawContext.fill(x - 4, y + 2, x, y + moduleWidget.parent.height - 2, AreteGui.gray.getRGB());



        //rightSide Fill
        drawContext.fill(x - 4 + moduleWidget.parent.width - 4, y + 2, x + moduleWidget.parent.width -4 , y + moduleWidget.parent.height - 2, AreteGui.gray.getRGB());

        drawContext.fill(x - 4 , y + moduleWidget.parent.height - 2, x + moduleWidget.parent.width - 4, y + moduleWidget.parent.height + 2, AreteGui.gray.getRGB());

    }

    public boolean isCollided(int mouseX, int mouseY) {
        return (mouseX  <= x + moduleWidget.parent.width - 8 && mouseX >= x) && (mouseY<= y + moduleWidget.parent.height - 1 && mouseY >= y);
    }


}
