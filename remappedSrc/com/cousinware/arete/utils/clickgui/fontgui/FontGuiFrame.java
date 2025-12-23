package com.cousinware.arete.utils.guis.clickgui.defaultguis.fontgui;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.maingui.AreteGui;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.widgets.FontWidget;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.widgets.SliderSettingWidget;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class FontGuiFrame {

    String title;
    public int x;
    public int y;
    public int width = 90;
    public int height = 14;
    int lastMouseX = 0;
    int lastMouseY = 0;
    boolean fling = false;
    boolean isDragging;
    boolean open = true;
    FontWidget fontWidget;
    SliderSettingWidget sliderSettingWidget;

    public FontGuiFrame(String title, int x,  int y) {
        this.title = title;
        this.x = x;
        this.y = y;
        fontWidget = new FontWidget(this, x + 50, y + 50);

    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        height = 128;
        width = 256;
        drawCategoryBox(context);
        if (isDragging) doDragging(mouseX, mouseY);
        fontWidget.render(context);
    }

    public void drawCategoryBox(DrawContext context) {

        Color color = AreteGui.color;
        //left side
        //context.drawVerticalLine(x, y, y + height, color.getRGB());

        //top side
        //context.drawHorizontalLine(x, x + width, y, color.getRGB());

        int fontY = (int) AreteClient.fontManager.getFontHeight(AreteClient.fontManager.selectedFont);
        context.getMatrices().push();
        //3D    context.fill(x + 1, y + 1, x + width + 1, y + height + 1, color.darker().getRGB());
        context.fill(x, y, x + width, y + height, color.brighter().getRGB());
        Color gray = AreteGui.gray;
        //context.fill(x, y, x + width, y + height, gray.getRGB());
        AreteClient.fontManager.drawText(context, title, x + (width / 2) - 28, y + (fontY / 4) + 1);
        //context.drawTextWithShadow(Core.customFont.getValue() ? AreteClient.tr[0] : MinecraftClient.getInstance().textRenderer, category.name(), x + 6, y + (fontY / 4), Color.WHITE.getRGB());
        //context.drawText(Core.customFont.getValue() ? AreteClient.tr[0] : MinecraftClient.getInstance().textRenderer, category.name(), x + (width / 2), y + (height / 2) - (fontY / 2), color.getRGB(), false);
        //context.fill(x + 2, y + height, x + width - 2, y + height + 2, gray.getRGB());
        context.getMatrices().pop();
        //right side
        //context.drawVerticalLine(x + width, y, y + height, color.getRGB());

        //bottom side
        //context.drawHorizontalLine(x, x + width, y + height, color.getRGB());
    }

    public void doDragging(int mouseX, int mouseY) {
        if (mouseX != lastMouseX) x+= mouseX - lastMouseX;
        if (mouseY != lastMouseY) y+= mouseY - lastMouseY;


        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    public void mouseClicked(double mouseX, double mouseY, int button, int action) {

        boolean collided = isCollided((int) mouseX, (int) mouseY);
        if (action == 0 && isDragging && collided) {
            //if (Math.abs(lastMouseX - mouseX) > 0 || Math.abs(lastMouseY - mouseY) > 0) {
            fling = true;
//                System.out.println("added vel");
//                double xVel = (lastMouseX * 1.25) - (mouseX * 1.25);
//                if ((lastMouseX * 1.25) >= (mouseX * 1.24)) xVel *= 3;
//                else xVel *= -3;
//                velocity.applyVelocity(xVel, lastMouseY - mouseY);
//            System.out.println("Velocity added to " + xVel);
//            System.out.println("Velocity added to " + (lastMouseY - mouseY));
            //}
        }


        this.lastMouseX = (int) mouseX;
        this.lastMouseY = (int) mouseY;
        if (collided && button == 0) {
            if (action == 1) isDragging = true;
            if (action == 0) isDragging = false;

        }

        if (button == 1) open = !open;
    }



    public boolean isCollided(int mouseX, int mouseY) {
        return (mouseX <= x + width && mouseX >= x) && (mouseY <= y + (height / 10) + 2 && mouseY >= y);
    }
}
