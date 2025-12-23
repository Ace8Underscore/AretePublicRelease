package com.cousinware.arete.managers;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.module.Client.Core;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.string.coloredstring.ColoredString;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.nanovg.NVGColor;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.lwjgl.nanovg.NanoVG.*;

@Getter
public class FontManager {

    public static int font;
    @Getter
    public static float fontSize = 9f;
    @Setter
    public String altFont = "";
    public String selectedFont = "verdana";
    private final ArrayList<String> loadedFontNames = new ArrayList<>();
    NVGColor nvgColor;


    public FontManager() {
    }

    public void addLoadedFont(String fontName) {
        this.loadedFontNames.add(fontName);
    }

    public ArrayList<String> getLoadedFontNames() {
        return this.loadedFontNames;
    }


    @Deprecated
    public int getWidth(String text) {
        TextRenderer font = Core.customFont.getValue() ? AreteClient.tr[0] : MinecraftClient.getInstance().textRenderer;
        return font.getWidth(text);
    }

    public void addFont(String fileLocation, String name) {
        nvgCreateFont(NVGContext.context, name.split("\\.")[0], fileLocation);
        addLoadedFont(name.split("\\.")[0]);
    }

    public void addFont(ByteBuffer byteBuffer, String name) {
        nvgCreateFontMem(NVGContext.context, name.split("\\.")[0], byteBuffer, false);
        addLoadedFont(name.split("\\.")[0]);
    }

    public TextRenderer getFont() {
        return Core.customFont.getValue() ? AreteClient.tr[0] : MinecraftClient.getInstance().textRenderer;
    }

    public void renderTextMultiColor(DrawContext context, int x, int y, boolean shadow, Module.TextOrdering textOrdering, ColoredString... strings) {
        if (textOrdering.equals(Module.TextOrdering.Left)) renderTextMultiColor(context, x, y, shadow, strings);
        else if (textOrdering.equals(Module.TextOrdering.Centered))
            renderTextMultiColorCenteredString(context, x, y, shadow, strings);
        else if (textOrdering.equals(Module.TextOrdering.Right))
            renderTextMultiColorLeftString(context, x, y, shadow, strings);

    }

    public void renderTextMultiColorCenteredString(DrawContext context, int x, int y, boolean shadow, ColoredString... strings) {
        renderTextMultiColorCustom((int) (x - (getStringsWidth(strings) / 2)), y, shadow, strings);
    }

    public void renderTextMultiColorLeftString(DrawContext context, int x, int y, boolean shadow, ColoredString... strings) {
        renderTextMultiColorCustom((int) (x - getStringsWidth(strings)), y, shadow, strings);
    }

    public void renderTextMultiColor(DrawContext context, int x, int y, boolean shadow, ColoredString... strings) {
        renderTextMultiColorCustom(x, y, shadow, strings);
    }

    public void renderTextMultiColorMc(DrawContext context, int x, int y, ColoredString... strings) {
        String message = "";

        float xOffset = 0;

        for (ColoredString data : strings) {

            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, data.getText(), (int) (x + xOffset), y, data.getColor().getRGB());
            xOffset += MinecraftClient.getInstance().textRenderer.getWidth(data.getText());
        }

        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, message, x, y, Color.WHITE.getRGB());

    }


    public void renderTextMultiColorCustom(int x, int y, boolean shadow, ColoredString... strings) {
        long vg = NVGContext.context;
        float xOffset = 0;


        for (int i = 0; i < strings.length; i++) {
            ColoredString data = strings[i];

            //shadows
            if (shadow) {
                nvgFontFace(vg, selectedFont);
                NVGColor nvgColor = NVGContext.nvgColor(Color.BLACK);
                nvgFillColor(vg, nvgColor);
                nvgFontSize(vg, fontSize);
                nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
                nvgText(vg, x + xOffset + .55f, y + .55f, data.getText());

            }

            //useful stuff


            nvgFontFace(vg, selectedFont);
            NVGColor nvgColor = NVGContext.nvgColor(data.getColor());
            nvgFillColor(vg, nvgColor);
            nvgFontSize(vg, fontSize);
            nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
            nvgText(vg, x + xOffset, y, data.getText());

            String s = data.getText().endsWith(" ") ? " " : "";
            xOffset += getStringWidth(data.getText() + s, selectedFont);


        }
    }

    public void drawLeftStringFont(DrawContext context, String font, String text, int x, int y, Color color, boolean shadow) {
        renderTextWithFont(font, text, (int) (x - AreteClient.fontManager.getStringWidth(text, font)), y, color, shadow);
    }


    public void drawLeftStringText(DrawContext context, String font, String text, int x, int y, Color color, boolean shadow) {
        drawText(context, text, (int) (x - AreteClient.fontManager.getStringWidth(text, font)), y, color, shadow);
    }

    public void drawText(DrawContext context, String text, float x, float y, Color color, boolean shadow) {
        drawTextCustom(text, x, y, color, shadow);

    }

    public void setSelectedFont(String s) {
        if (loadedFontNames.contains(s)) {
            this.selectedFont = s;
            Command.sendClientSideMessage("Changed Font to " + s, true);
        } else {
            Command.sendClientSideMessage("Not a Font!", false);
        }
    }

    public float getStringsWidth(ColoredString... strings) {
        if (!Core.customFont.getValue())
            return MinecraftClient.getInstance().textRenderer.getWidth(ColoredString.getString(strings));
        float size = 0;
        float[] bounds = new float[4]; // Left, top, width, height
        for (ColoredString s : strings) {
            nvgFontSize(NVGContext.context, fontSize);
            nvgFontFace(NVGContext.context, selectedFont);
            int startIndex = s.getText().length() - 1;
            String val = s.getText().substring(Math.max(startIndex, 0)).equalsIgnoreCase(" ") ? " " : "";
            nvgTextBounds(NVGContext.context, 0, 0, s.getText() + val, bounds);
            size += bounds[2];
        }

        return size;
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

        return bounds[3] + 1;
    }

    public void renderTextWithFont(String font, String text, int x, int y, Color color, boolean shadow) {
        long vg = NVGContext.context;


        if (shadow) {
            nvgFontFace(vg, font);
            NVGColor nvgColor1 = NVGContext.nvgColor(Color.BLACK);
            nvgFillColor(vg, nvgColor1);
            nvgFontSize(vg, fontSize);
            nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
            nvgText(vg, x + .55f, y + .55f, text);
        }

        nvgFontFace(vg, font);
        NVGColor nvgColor = NVGContext.nvgColor(color);
        nvgFillColor(vg, nvgColor);
        nvgFontSize(vg, fontSize);
        nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
        nvgText(vg, x, y, text);


    }

    protected void drawTextCustom(String text, float x, float y, Color color, boolean shadow) {
        long vg = NVGContext.context;

        if (shadow) {
            nvgFontFace(vg, selectedFont);
            NVGColor nvgColor1 = NVGContext.nvgColor(Color.BLACK);
            nvgFillColor(vg, nvgColor1);
            nvgFontSize(vg, fontSize);
            nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
            nvgText(vg, x + 1, y + 1, text);
        }

        nvgFontFace(vg, selectedFont);
        NVGColor nvgColor = NVGContext.nvgColor(color);
        nvgFillColor(vg, nvgColor);
        nvgFontSize(vg, fontSize);
        nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
        nvgText(vg, x, y, text);


    }
    //The below method will stop the rendering process this would usually go at the end of your master render loop

    public void close() {
        long vg = NVGContext.context;
        nvgEndFrame(vg);
        NVGContext.restoreState();


    }

    //The below method will start the rendering process this would usually go at the beginning of your master render loop
    public void start() {
        try {
            if (!loadedFontNames.contains(selectedFont)) selectedFont = loadedFontNames.getFirst();
        } catch (Exception e) {
            AreteClient.LOGGER.debug("Failed to get first loaded font name");
        }

        long vg = NVGContext.context;
        nvgBeginFrame(vg, MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight(), (float) MinecraftClient.getInstance().getWindow().getScaleFactor());
        nvgFontFace(vg, selectedFont);
        nvgFontSize(vg, fontSize);

        nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);

    }

    public void drawCenteredText(DrawContext drawContext, String font, String text, int centerX, int y, Color color, boolean shadow) {
        renderTextWithFont(font, text, (int) (centerX - getStringWidth(text, font) / 2), y, color, shadow);
    }


    private void renderTextMc(DrawContext context, String text, int x, int y, Color color) {

        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, x, y, color.getRGB());
    }

}
