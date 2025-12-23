package com.cousinware.arete.utils.guis.clickgui.defaultguis.maingui;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.MouseButtonEvent;
import com.cousinware.arete.module.Client.ClickGui;
import com.cousinware.arete.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;

public class AreteGui extends Screen {

    public static double mouseX = 0;
    public static double mouseY = 0;
    public static Color color = new Color(ClickGui.r.getValue(), ClickGui.g.getValue(), ClickGui.b.getValue(), ClickGui.a.getValue());
    public static Color gray = new Color(52, 58, 67, 180);

    public static ArrayList<Frame> frames = new ArrayList<>();

    public AreteGui() {
        super(Text.of("arete"));
        AreteClient.eventBus.register(this);


        int x = 100;
        for (Module.Category category : Module.Category.values()) {
            frames.add(new Frame(category, x, 10));
            x += 100;
        }
    }


    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        return super.isMouseOver(mouseX, mouseY);
    }

    @Subscribe
    public void onMousePress(MouseButtonEvent event) {
        if (MinecraftClient.getInstance().currentScreen != AreteClient.areteGui) return;
        for (Frame frame : frames) {
            frame.mouseClicked(mouseX, mouseY, event.getButton(), event.getAction());
        }

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        AreteClient.fontManager.start();
        Color gray1 = new Color(52, 58, 67, 100);
        color = new Color(ClickGui.r.getValue(), ClickGui.g.getValue(), ClickGui.b.getValue(), ClickGui.a.getValue());
        context.fill(0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), gray1.brighter().getRGB());

        this.mouseX = mouseX;
        this.mouseY = mouseY;
        //context.drawText(Core.customFont.getValue() ? AreteClient.tr[0] : MinecraftClient.getInstance().textRenderer, "penis", 1, 1, new Color(255, 0, 0, 255).getRGB(), true);

        for (Frame frame : frames) {
            frame.render(context, mouseX, mouseY);
        }

        super.render(context, mouseX, mouseY, delta);
        AreteClient.fontManager.close();
    }


    static {
        //AreteGui.color = -1;
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }



    public boolean shouldPause() {
        return false;
    }

    public void init() {

    }

}
