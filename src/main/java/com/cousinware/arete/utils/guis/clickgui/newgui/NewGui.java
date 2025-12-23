package com.cousinware.arete.utils.guis.clickgui.newgui;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.MouseButtonEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.MinecraftInterface;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.NVGWrapper;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;

@Getter
public class NewGui extends Screen implements MinecraftInterface {

    public static int windowWidth = 0;
    public static int windowHeight = 0;
    public ArrayList<Frame> frames = new ArrayList<>();
    double mouseX = 0;
    double mouseY = 0;

    public NewGui() {
        super(Text.of("NewGui"));
        AreteClient.eventBus.register(this);
        frames = new ArrayList<>();


        float x = 100;
        for (Module.Category category : Module.Category.values()) {
            if (category.equals(Module.Category.Hud)) continue;
            Frame frame = new Frame(category, x, 10);
            frames.add(frame);
            x += frame.width + 10;
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;


        return super.isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        frames.forEach(frame -> frame.charTyped(chr, modifiers));
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        frames.forEach(frame -> frame.keyPressed(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Subscribe
    public void onMousePress(MouseButtonEvent event) {
        if (MinecraftClient.getInstance().currentScreen != AreteClient.newGui) return;

        for (Frame frame : frames) {
            frame.mouseClicked(mouseX, mouseY, event.getButton(), event.getAction());
        }

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
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        NVGContext.render(ctx -> {
            NVGWrapper.drawRect(ctx, 0, 0, windowWidth, windowHeight, new Color(10, 12, 16, 100));
        });

        frames.forEach(frame -> frame.render(mouseX, mouseY));

        //render descriptions
        frames.forEach(frame -> {
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
        });
    }

    @Override
    public void close() {
        super.close();

    }
}
