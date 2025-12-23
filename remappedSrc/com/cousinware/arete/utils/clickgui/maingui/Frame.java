package com.cousinware.arete.utils.guis.clickgui.defaultguis.maingui;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.MouseButtonLocationEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.guis.clickgui.Widget;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.widgets.*;
import com.cousinware.arete.utils.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;

public class Frame {

    Module.Category category;

    ArrayList<Module> modules = new ArrayList<>();
    public ArrayList<ModuleWidget> moduleWidgets = new ArrayList<>();

    boolean isDragging;
    boolean open = true;
    public int x;
    public int y;

    public int width = 90;
    public int height = 14;
    int lastMouseX = 0;
    int lastMouseY = 0;
    public Velocity velocity;
    boolean  fling = false;


    public MouseButtonLocationEvent lastMouseButton = new MouseButtonLocationEvent(1, 1, 1, 1);


    public Frame(Module.Category category, int x, int y) {
        velocity = new Velocity(Math.random() * 10, Math.random() * 10);
        this.category = category;
        this.x = x;
        this.y = y;
        for (Module module : AreteClient.moduleManager.getModules()) {
            if (module.getCategory().equals(category)) {
                modules.add(module);
            }
        }
        int yOffset = height;
        for (Module module : modules) {
            ModuleWidget moduleWidget = new ModuleWidget(this, module, x, yOffset);
            moduleWidgets.add(moduleWidget);
            yOffset+= height;
        }

        //gen setting for each module
        for (ModuleWidget moduleWidget : moduleWidgets) {
            for (Setting setting : AreteClient.settingManager.getRegisteredSettings()) {
                if (moduleWidget.module.getName().equalsIgnoreCase(setting.getParent().getName())) {
                    moduleWidget.addSetting(setting);
                }

            }
        }

        // gen some settings

        for (ModuleWidget moduleWidget : moduleWidgets) {
            int yOffsetSetting = 0;
            if (!moduleWidget.getSettings().isEmpty()) {
                for (Setting setting : moduleWidget.getSettings()) {
                    if (setting instanceof BoolSetting) {
                        // draw Setting
                        BoolSettingWidget boolSettingWidget = new BoolSettingWidget(moduleWidget, (BoolSetting) setting, yOffsetSetting);
                        moduleWidget.addSettingWidget(boolSettingWidget);
                    }

                    if (setting instanceof IntSetting) {
                        SliderSettingWidget sliderSettingWidget = new SliderSettingWidget(moduleWidget, (IntSetting) setting, yOffsetSetting);
                        moduleWidget.addSettingWidget(sliderSettingWidget);
                    }

                    if (setting instanceof DoubleSetting) {
                        SliderSettingWidget sliderSettingWidget = new SliderSettingWidget(moduleWidget, (DoubleSetting) setting, yOffsetSetting);
                        moduleWidget.addSettingWidget(sliderSettingWidget);
                    }

                    if (setting instanceof ModeSetting) {
                        ModeSettingWidget modeSettingWidget = new ModeSettingWidget(moduleWidget, (ModeSetting) setting, yOffsetSetting);
                        moduleWidget.addSettingWidget(modeSettingWidget);
                    }
                    //after we make each setting we add to setting offset
                    yOffsetSetting++;
                }
            }
            if (moduleWidget.module.prioritySetting) {
                moduleWidget.addSettingWidget(new PriorityWidget(moduleWidget, new IntSetting().setName("Priority").setMin(0).setMax(10).setValue((int)moduleWidget.module.getPriority()).build(moduleWidget.module), yOffsetSetting));
                yOffsetSetting++;
            }
            moduleWidget.addSettingWidget(new BindWidget(moduleWidget, yOffsetSetting));
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        context.getMatrices().push();

        //nvgRoundedRect(MinecraftClient.getInstance().getRenderTime(), 1, 1, 10, 10, 10);
        //TODO below line makes font LOOK HELLA BETTER
        //context.getMatrices().scale(1.25f, 1.25f, 1.25f);
        if (isDragging) doDragging(mouseX, mouseY);
        drawCategoryBox(context);
        drawModules(context);
        genSetting(context);
        doVelocity();
        context.getMatrices().pop();



    }

    public void addVelocity(int mouseX, int mouseY) {
        velocity.setVelocity(lastMouseX - mouseX, lastMouseY - mouseY);
    }

    public void genSetting(DrawContext context) {

    }

    public void doVelocity() {
        int maxY = MinecraftClient.getInstance().currentScreen.height;
        int maxX = MinecraftClient.getInstance().currentScreen.width;
        int minY = 0;
        int minX = 0;
        if (x + width >= maxX || x <= minX) {
            velocity.flipXVel(false);
        }
        if (y + height >= maxY || y <= minY) {
            velocity.flipYVel(false);
        }
        velocity.tick();
        this.x += velocity.getxV();
        this.y += velocity.getyV();

    }



    public void drawModules(DrawContext context) {
        int modOffSet = 0;
        for (ModuleWidget moduleWidget : moduleWidgets) {
            int settingOffset = 0;
            moduleWidget.modY(modOffSet);
            moduleWidget.render(context);

            if (!moduleWidget.isOpened()) continue;
            //if (!moduleWidget.getSettings().isEmpty()) {
                for (Widget widget : moduleWidget.getSeetingWidget()) {

                   // System.out.println(widget.getClass() + " " + moduleWidget.module.getName());
                    widget.render(context);
                    settingOffset+= height;
                    modOffSet+= height;
                }
            //}

        }
    }

    public void doDragging(int mouseX, int mouseY) {
        velocity.setVelocity(0, 0);
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
        AreteClient.fontManager.drawText(context, category.name(), x + 3, y + (fontY / 4) + 1);
        //context.drawTextWithShadow(Core.customFont.getValue() ? AreteClient.tr[0] : MinecraftClient.getInstance().textRenderer, category.name(), x + 6, y + (fontY / 4), Color.WHITE.getRGB());
        //context.drawText(Core.customFont.getValue() ? AreteClient.tr[0] : MinecraftClient.getInstance().textRenderer, category.name(), x + (width / 2), y + (height / 2) - (fontY / 2), color.getRGB(), false);
        //context.fill(x + 2, y + height, x + width - 2, y + height + 2, gray.getRGB());
        context.getMatrices().pop();
        //right side
        //context.drawVerticalLine(x + width, y, y + height, color.getRGB());

        //bottom side
        //context.drawHorizontalLine(x, x + width, y + height, color.getRGB());
    }

    public boolean isCollided(int mouseX, int mouseY) {
        return (mouseX <= x + width && mouseX >= x) && (mouseY <= y + height && mouseY >= y);
    }
}
