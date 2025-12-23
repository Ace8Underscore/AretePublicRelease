package com.cousinware.arete.utils.guis.clickgui.defaultguis.widgets;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.events.event.MouseButtonEvent;
import com.cousinware.arete.utils.Timer;
import com.cousinware.arete.utils.guis.clickgui.Widget;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.fontgui.FontGui;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.fontgui.FontGuiFrame;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.maingui.AreteGui;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class FontWidget extends Widget {

    FontGuiFrame frame;
    String font;
    int x;
    int y;
    Timer timer;
    int index = 0;

    public FontWidget(FontGuiFrame frame, int x, int y) {
        timer = new Timer(5);
        this.frame = frame;
        this.x = x;
        this.y = y;
        AreteClient.eventBus.register(this);

    }


    @Subscribe
    public void onMousePress(MouseButtonEvent event) {
        if (MinecraftClient.getInstance().currentScreen != AreteClient.fontGui || event.getAction() == 0) return;
        if (!timer.canTick()) return;
        double mouseX = AreteGui.mouseX;
        double mouseY = AreteGui.mouseY;

        if (isCollidingRightButton()) {
            if (index == AreteClient.fontManager.getLoadedFontNames().size() - 1) index = 0;
            else index++;
            font = AreteClient.fontManager.getLoadedFontNames().get(index);
        }

        if (isCollidingLeftButton()) {
            if (index == 0) index = AreteClient.fontManager.getLoadedFontNames().size() - 1;
            else  index--;
            font = AreteClient.fontManager.getLoadedFontNames().get(index);
            //increment
        }

        if (isCollidingSetFontButton()) {
            AreteClient.fontManager.setSelectedFont(AreteClient.fontManager.getLoadedFontNames().get(index));
            Command.sendClientSideMessage("set font to " + AreteClient.fontManager.getLoadedFontNames().get(index));
        }

    }




    @Override
    public void render(DrawContext drawContext) {
        font = AreteClient.fontManager.getLoadedFontNames().get(index);
        this.x = frame.x;
        this.y = frame.y;

        renderFontExample(drawContext);
        renderRightButton(drawContext);
        renderLeftButton(drawContext);
        renderFontName(drawContext);
        renderSetFontButton(drawContext);

    }

    public void renderSetFontButton(DrawContext drawContext) {
    drawContext.fill(x + (frame.width / 2) - 25, y + (frame.height / 2) + 35 - 5, x + (frame.width / 2) + 25, y + (frame.height / 2) + 35 + 13, AreteGui.color.getRGB());
    drawContext.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, "SetFont", x + (frame.width / 2), y + (frame.height / 2) + 35, Color.WHITE.getRGB());


    }

    public void renderFontName(DrawContext drawContext) {
        drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, index + "/" + AreteClient.fontManager.getLoadedFontNames().size(), x, y, Color.WHITE.getRGB());
        drawContext.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,  AreteClient.fontManager.getLoadedFontNames().get(index), x + (frame.width / 2) , y + (frame.height / 2) - 25, Color.WHITE.getRGB());
    }

    public void renderRightButton(DrawContext drawContext) {
        drawContext.fill(x + frame.width - 23, y + (frame.height / 2) - 4, x + frame.width - 7, y + (frame.height / 2) + 13, AreteGui.color.getRGB());
        AreteClient.fontManager.drawCenteredText(drawContext, font, ">", x + frame.width - 15, y + (frame.height / 2));
    }

    public void renderLeftButton(DrawContext drawContext) {
        drawContext.fill(x + 7, y + (frame.height / 2) - 4, x + 23, y + (frame.height / 2) + 13, AreteGui.color.getRGB());
        AreteClient.fontManager.drawCenteredText(drawContext, font, "<", x + 15, y + (frame.height / 2));
    }



    public void renderFontExample(DrawContext drawContext) {
        //AreteClient.fontManager.renderTextWithFont(font, "Arete Client Has The Best Font Manager", x, y);
        AreteClient.fontManager.drawCenteredText(drawContext, font, "Arete Client Has The Best Font Manager", x + (frame.width / 2), y + (frame.height / 2));
        //drawContext.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, "Arete Client Has The Best Font Manager", x + (frame.width / 2), y + (frame.height / 2) - 10, Color.WHITE.getRGB());
    }

    public boolean isCollidingLeftButton() {
        return (FontGui.mouseX > x + 7 && FontGui.mouseX < x + 23) && (FontGui.mouseY > y + (frame.height / 2) - 4 && FontGui.mouseY < y + (frame.height / 2) + 13);
    }

    public boolean isCollidingRightButton() {
        return (FontGui.mouseX > x + frame.width - 23 && FontGui.mouseX < x + frame.width - 7) && (FontGui.mouseY > y + (frame.height / 2) - 4 && FontGui.mouseY < y + (frame.height / 2) + 13);
    }

    public boolean isCollidingSetFontButton() {
        return (FontGui.mouseX > x + (frame.width / 2) - 25 && FontGui.mouseX < x + (frame.width / 2) + 25) && (FontGui.mouseY > y + (frame.height / 2) + 35 - 5 && FontGui.mouseY < y + (frame.height / 2) + 35 + 13 + 3);
    }


}
