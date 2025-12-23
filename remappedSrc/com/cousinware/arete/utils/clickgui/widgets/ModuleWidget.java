package com.cousinware.arete.utils.guis.clickgui.defaultguis.widgets;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.MouseButtonEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.Timer;
import com.cousinware.arete.utils.guis.clickgui.Widget;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.maingui.AreteGui;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.maingui.Frame;
import com.cousinware.arete.utils.settings.Setting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;

public class ModuleWidget extends Widget {

    public static long nvgContext;
    public static int font;

    Frame parent;

    public Module module;

    boolean opened = false;
    int x;

    Timer timer;

    public int centeredX;
    int offSetY;
    int offSetY2;

    int y = 0;

    ArrayList<Setting> settings = new ArrayList<>();



    ArrayList<Widget> settingWidgets = new ArrayList<>();

    public ModuleWidget(Frame parent, Module module, int x, int offSetY) {
        this.timer = new Timer(25, module.getName());
        this.parent = parent;
        this.module = module;
        this.x = x;
        this.offSetY = offSetY;
        AreteClient.eventBus.register(this);

    }

    @Override
    public void render(DrawContext context) {
        x = parent.x;
        centeredX = parent.x + (parent.width / 2);
        y = parent.y + offSetY;
        y+= offSetY2;

        int barOffset = 0;
        //if (parent.moduleWidgets.get(parent.moduleWidgets.size() - 1) == this) barOffset = 2;
        Color gray = AreteGui.gray;

        if (parent.moduleWidgets.get(0) == this) context.fill(x, y, x + parent.width, y + 2, gray.getRGB());

        Color color = AreteGui.color;



        if (module.isEnabled()) context.fill(x + 2, y + 2, x + parent.width - 2, y + parent.height - 1, color.brighter().getRGB());
        else context.fill(x + 2, y + 2, x + parent.width - 2, y + parent.height - 1, color.darker().getRGB());


        //leftSide Fill
        context.fill(x, y + 2, x + 2, y + parent.height + barOffset - 1, gray.getRGB());



        //rightSide Fill
        context.fill(x + parent.width - 2, y + 2, x + parent.width, y + parent.height + barOffset - 1, gray.getRGB());

        //Middle Bottom Fill
        context.fill(x, y + parent.height - 1, x + parent.width, y + parent.height + 2, gray.getRGB());


        //gray box
        //context.fill(x, y, x + parent.width, y + parent.height, gray.getRGB());

        //context.drawVerticalLine(x, y, y + parent.height + 1, color.getRGB());

        //top side
        //context.drawHorizontalLine(x, x + parent.width, y, color.getRGB());

        //right side
        //context.drawVerticalLine(x + parent.width, y, y + parent.height + 1, color.getRGB());

        //bottom side
        //if (!opened) context.drawHorizontalLine(x, x + parent.width, y + parent.height, color.getRGB());
        //!GOOD BELOW
        //context.drawTextWithShadow(AreteClient.fontManager.getFont(), module.getName(), x + 4, y + 2, Color.WHITE.getRGB());

        //AreteClient.fontManager.renderText(context, module.getName(), x + 4, y + 4);

        AreteClient.fontManager.drawText(context, module.getName(), x + 4, y + 4);

        //!!


        //!!

//        NanoVG.nnvgFontSize(FontManager.nvgContext, 12);
//        nvgTextAlign(FontManager.nvgContext, NVG_ALIGN_CENTER | NVG_ALIGN_TOP);
//        //nvgFillColor(nvgContext, new NVGColor(1, 1, 1, 1, 1).r(100).a(100).g(100).b(100));
//        NanoVG.nvgText(FontManager.nvgContext, x + 4, y + 2, module.getName());

        //

        if (!opened)AreteClient.fontManager.drawCenteredText(context, AreteClient.fontManager.selectedFont, "...", x + parent.width - 10, y - parent.height + (parent.height / 2) + 7);
        else AreteClient.fontManager.drawCenteredText(context, AreteClient.fontManager.selectedFont, "  .", x + parent.width - 10, y - parent.height + (parent.height / 2) + 7);

    }


    public boolean isOpened() {
        return opened;
    }

    public void modY(int y) {
        this.offSetY2 = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void addSetting(Setting setting) {
        settings.add(setting);
    }

    public void addSettingWidget(Widget widget) {
        settingWidgets.add(widget);
    }

    public ArrayList<Widget> getSeetingWidget() { return this.settingWidgets;}


    public ArrayList<Setting> getSettings() {
        return settings;
    }

    //    public void doClick(int button, int action) {
//        double mouseX = AreteGui.mouseX;
//        double mouseY = AreteGui.mouseY;
//        if (isCollided((int) mouseX, (int) mouseY)) {
//            if (button == 0 && action == 1) module.toggle();
//            if (button == 1 && action == 1) {
//                opened = !opened;
//                System.out.println(opened);
//            }
//        }
//    }

    @Subscribe
    public void onMousePress(MouseButtonEvent event) {
        if (MinecraftClient.getInstance().currentScreen != AreteClient.areteGui) return;
        if (!timer.canTick()) return;

        double mouseX = AreteGui.mouseX;
        double mouseY = AreteGui.mouseY;

        if (isCollided((int) mouseX, (int) mouseY)) {
            if (event.getButton() == 0 && event.getAction() == 1) module.toggle();
            if (event.getButton() == 1 && event.getAction() == 1) {
                opened = !opened;

            }
        }
    }

        public boolean isCollided(int mouseX, int mouseY) {
            return (mouseX <= x + parent.width && mouseX >= x) && (mouseY <= y + parent.height - 1 && mouseY >= y);
        }
    }
