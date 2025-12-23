package com.cousinware.arete.managers;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.module.Client.Core;
import com.cousinware.arete.utils.rendering.font.nvg.NVGContext;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.nanovg.NVGColor;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.nanovg.NanoVG.*;

public class FontManager {

    public static int font;
    public static float fontSize = 9.5f;
    public String selectedFont = "opensans-regular";
    private ArrayList<String> loadedFontNames = new ArrayList<>();
    NVGColor nvgColor;




    public FontManager() {
    }

    public void addLoadedFont(String fontName) {
        this.loadedFontNames.add(fontName);
    }

    public ArrayList<String> getLoadedFontNames() {
        return this.loadedFontNames;
    }


    public int getWidth(String text) {
        TextRenderer font = Core.customFont.getValue() ? AreteClient.tr[0] : MinecraftClient.getInstance().textRenderer;
        return font.getWidth(text);
    }

    public void addFont(String fileLocation, String name) {
        nvgCreateFont(NVGContext.context, name.split("\\.")[0], fileLocation);
        addLoadedFont(name.split("\\.")[0]);
    }

    public TextRenderer getFont() {
        return Core.customFont.getValue() ? AreteClient.tr[0] : MinecraftClient.getInstance().textRenderer;
    }

    public void renderTextWithFont(String font ,String text, int x, int y) {
        long vg = NVGContext.context;


        nvgFontFace(vg, font);
        nvgText(vg, x, y, text);

    }

    public void drawLeftStringText(DrawContext context, String font, String text, int x, int y) {
        drawText(context, text, (int) (x - AreteClient.fontManager.getStringWidth(text, font)), y);
    }

    public void drawText(DrawContext context, String text, int x, int y) {

        if (Core.customFont.getValue()) drawTextCustom(text, x, y);
        else renderTextMc(context, text, x, y);

    }

    public void setSelectedFont(String s) {
        if (loadedFontNames.contains(s)) {
            this.selectedFont = s;
            Command.sendClientSideMessage("Changed Font to " + s);
        } else {
            Command.sendClientSideMessage("Not a Font!");
        }
    }

    public float getStringWidth(String s, String selectedFont) {
        float[] bounds = new float[4]; // Left, top, width, height
        nvgFontSize(NVGContext.context, fontSize);
        nvgFontFace(NVGContext.context, selectedFont);
        nvgTextBounds(NVGContext.context, 0, 0, s, bounds);

        return bounds[2];
    }

    public float getFontHeight(String selectedFont) {
        float[] bounds = new float[4]; // Left, top, width, height
        nvgFontSize(NVGContext.context, fontSize);
        nvgFontFace(NVGContext.context, selectedFont);
        nvgTextBounds(NVGContext.context, 0, 0, "Meow", bounds);

        return bounds[3];
    }

    protected void drawTextCustom(String text,int x, int y) {
        long vg = NVGContext.context;
        //we set the font just incase some other func messes with it
        nvgFontFace(vg, selectedFont);
        nvgText(vg, x, y, text);

    }

    //The below method will start the rendering process this would usually go at the beginning of your master render loop
    public void start() {
        long vg = NVGContext.context;

        nvgBeginFrame(vg, MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight(), (float)MinecraftClient.getInstance().getWindow().getScaleFactor());
        nvgFontFace(vg, selectedFont);
        nvgFontSize(vg, fontSize);

        nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
    }
    //The below method will stop the rendering process this would usually go at the end of your master render loop

    public void close() {
        long vg = NVGContext.context;
        nvgEndFrame(vg);
        nvgClosePath(vg);
        restoreState();


    }

    public static void restoreState() {
        GlStateManager._disableCull();
        GlStateManager._disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
    }

    public void drawCenteredText(DrawContext drawContext, String font,String text, int centerX, int y) {
        if (Core.customFont.getValue())renderTextWithFont(font, text, (int) (centerX - getStringWidth(text, font) / 2), y);
        else renderTextMc(drawContext, text,(centerX - MinecraftClient.getInstance().textRenderer.getWidth(text) / 2), y );
    }


    private void renderTextMc(DrawContext context, String text,int x, int y) {
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, x, y, Color.WHITE.getRGB());
    }

}
