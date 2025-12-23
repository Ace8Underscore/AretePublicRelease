package com.cousinware.arete.utils.guis.clickgui.defaultguis.fontgui;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.MouseButtonEvent;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.maingui.AreteGui;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class FontGui extends Screen {

    FontGuiFrame frame;
    public static int mouseX;
    public static int mouseY;

    public FontGui() {
        super(Text.of("arete"));
        AreteClient.eventBus.register(this);

        frame = new FontGuiFrame("FontManager", 100, 100);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        AreteClient.fontManager.start();
        FontGui.mouseX = mouseX;
        FontGui.mouseY = mouseY;
        //Color color = new Color(ClickGui.r.getValue(), ClickGui.g.getValue(), ClickGui.b.getValue(), ClickGui.a.getValue());
        context.fill(0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), AreteGui.gray.brighter().getRGB());
        frame.render(context, mouseX, mouseY);
        AreteClient.fontManager.close();
    }

    @Subscribe
    public void onMousePress(MouseButtonEvent event) {
        if (MinecraftClient.getInstance().currentScreen != AreteClient.fontGui) return;

        frame.mouseClicked(mouseX, mouseY, event.getButton(), event.getAction());


    }

}
