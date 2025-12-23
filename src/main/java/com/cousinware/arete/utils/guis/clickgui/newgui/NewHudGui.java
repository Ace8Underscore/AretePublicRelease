package com.cousinware.arete.utils.guis.clickgui.newgui;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.MouseButtonEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.MinecraftInterface;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.NVGWrapper;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;

public class NewHudGui extends Screen implements MinecraftInterface {

    public static int windowWidth = 0;
    public static int windowHeight = 0;
    public static double mouseX = 0;
    public static double mouseY = 0;
    Frame frame;

    public NewHudGui() {
        super(Text.of("NewHudGui"));
        AreteClient.eventBus.register(this);
        frame = new Frame(Module.Category.Hud, 100, 10);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        NewHudGui.mouseX = mouseX;
        NewHudGui.mouseY = mouseY;


        return super.isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        frame.charTyped(chr, modifiers);
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        frame.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Subscribe
    public void onMousePress(MouseButtonEvent event) {
        if (MinecraftClient.getInstance().currentScreen != AreteClient.newHudGui) return;
        frame.mouseClicked(mouseX, mouseY, event.getButton(), event.getAction());


    }


    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        //set const variables
        windowWidth = mc.getWindow().getScaledWidth();
        windowHeight = mc.getWindow().getScaledHeight();

        //render background
        NewHudGui.mouseX = mouseX;
        NewHudGui.mouseY = mouseY;
        NVGContext.render(ctx -> {
            NVGWrapper.drawRect(ctx, 0, 0, windowWidth, windowHeight, new Color(10, 12, 16, 100));
        });

        frame.render(mouseX, mouseY);

        //render descriptions

        frame.moduleComponents.forEach(moduleComponent -> {
            if (moduleComponent.isCollided(mouseX, mouseY) && !moduleComponent.description.isEmpty()) {
                moduleComponent.renderDescription(moduleComponent.getModule().getDescription(), mouseX, mouseY);
            }
            moduleComponent.getSettingComponent().forEach(component -> {
                if (component.isVisible() && component.parent.isOpened() && !component.description.isEmpty() && component.isCollided(mouseX, mouseY)) {
                    component.renderDescription(component.description, mouseX, mouseY);
                }
            });
        });

    }

    @Override
    public void close() {
        super.close();

    }
}
