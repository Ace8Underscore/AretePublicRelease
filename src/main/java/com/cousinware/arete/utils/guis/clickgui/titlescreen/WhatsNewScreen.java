package com.cousinware.arete.utils.guis.clickgui.titlescreen;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.utils.MinecraftInterface;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.string.coloredstring.ColoredString;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.text.Text;

import java.awt.*;

public class WhatsNewScreen extends Screen implements MinecraftInterface {

    private static final Text TITLE_TEXT = Text.of("Whats New!");
    public static Screen parent;
    @Getter
    private final static WhatsNewScreen instance = new WhatsNewScreen();
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    DrawContext context;
    int ySpacing = 0;

    public WhatsNewScreen() {
        super(TITLE_TEXT);


    }

    protected void init() {
        this.layout.addHeader(TITLE_TEXT, this.textRenderer);
        DirectionalLayoutWidget directionalLayoutWidget = this.layout.addBody(DirectionalLayoutWidget.vertical()).spacing(8);
        directionalLayoutWidget.getMainPositioner().alignHorizontalCenter();
        this.layout.refreshPositions();
        this.layout.forEachChild(this::addDrawableChild);

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.context = context;
        ySpacing = 20;

        NVGContext.render(nvg -> {
            AreteClient.fontManager.renderTextMultiColorCustom(mc.getWindow().getScaledWidth() / 32, 40, true, new ColoredString(Color.ORANGE, "Arete Public Release"));
        });
        //AreteClient.fontManager.renderTextMultiColorCustom(mc.getWindow().getScaledWidth() / 32, 40, true, new ColoredString(Color.ORANGE, "We Wide Update!"));
        drawText(
                "Good To Know \n" +
                "ClickGUI keybind is Y \n" +
                "Command prefix is ,(comma) \n \n \n \n"




        );


    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void drawText(String message) {
        int x = mc.getWindow().getScaledWidth() / 32;

        String[] args = message.split("\n");

        for (String arg : args) {
            int y = 50 + ySpacing;
            NVGContext.render(nvg -> {
                AreteClient.fontManager.drawText(null, arg, x, y, Color.WHITE, true);
            });
            ySpacing += (int) AreteClient.fontManager.getFontHeight(AreteClient.fontManager.selectedFont);
        }


    }

    protected void initTabNavigation() {
        this.layout.refreshPositions();
    }

}
