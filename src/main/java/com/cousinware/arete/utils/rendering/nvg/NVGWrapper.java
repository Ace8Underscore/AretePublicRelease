package com.cousinware.arete.utils.rendering.nvg;

import com.cousinware.arete.managers.AssetManager;
import com.cousinware.arete.utils.ColorUtils;
import lombok.experimental.UtilityClass;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;
import java.nio.ByteBuffer;

import static org.lwjgl.nanovg.NanoVG.*;

@UtilityClass
public class NVGWrapper {

    public void drawRect(long context, float x, float y, float w, float h, Color color) {
        nvgBeginPath(context);
        nvgRect(context, x, y, w, h);
        NVGColor nvgColor = nvgColor(color);
        nvgFillColor(context, nvgColor);
        nvgFill(context);
        nvgClosePath(context);
        nvgColor.free();
    }

    public static void drawDiagonalLine(long context, float x1, float y1, float x2, float y2, int thickness, Color color) {
        float slope = (y2 - y1) / (x2 - x1);
        float length = (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

        //System.out.println(slope);

        for (int i = (int) Math.min(x1, x2); i < Math.max(x1, x2); i++) {
            //does y1 equal b?
            float b = y1 - (slope * x1);
            float y = (slope * i) + b;
            float x = (y - b) / slope;


            drawVerticalLine(context, y, 1, x, color);
        }


    }

    public static void drawRoundedRect(long context, float x, float y, float w, float h, float radius, Color color) {
        nvgBeginPath(context);
        nvgRoundedRect(context, x, y, w, h, radius);
        NVGColor nvgColor = nvgColor(color);
        nvgFillColor(context, nvgColor);
        nvgFill(context);
        nvgClosePath(context);
        nvgColor.free();
    }

    public static void drawHorizontalLine(long context, float x, float width, float y, Color color) {
        nvgBeginPath(context);
        nvgRect(context, x, y, width, 1);
        NVGColor nvgColor = nvgColor(color);
        nvgFillColor(context, nvgColor);
        nvgFill(context);
        nvgClosePath(context);
        nvgColor.free();
    }

    public static void drawHorizontalLine(long context, float x, float width, float y, float thickness, Color color) {
        nvgBeginPath(context);
        nvgRect(context, x, y, width, 1);
        NVGColor nvgColor = nvgColor(color);
        nvgStrokeWidth(context, thickness);
        nvgStrokeColor(context, nvgColor);
        nvgStroke(context);
        nvgClosePath(context);
        nvgColor.free();
    }

    public static void drawHorizontalLineGlow(long context, float x, float width, float y, float thickness, float maxThickness, Color color) {
        nvgBeginPath(context);
        nvgRect(context, x, y, width, 1);
        NVGColor nvgColor = nvgColor(color);
        NVGColor nvgColorGlow = nvgColor(ColorUtils.convertAlpha(color, 15));

        for (float i = maxThickness; i > thickness; i -= .10f) {
            nvgStrokeWidth(context, i);
            nvgStrokeColor(context, nvgColorGlow);
            nvgStroke(context);
        }

        //reg thickness etc
        nvgStrokeWidth(context, thickness);
        nvgStrokeColor(context, nvgColor);
        nvgStroke(context);
        nvgClosePath(context);
        nvgColor.free();
    }

    public static void drawVerticalLine(long context, float y, float height, float x, Color color) {
        nvgBeginPath(context);
        nvgRect(context, x, y, 1, height);

        NVGColor nvgColor = nvgColor(color);
        nvgFillColor(context, nvgColor);
        nvgFill(context);
        nvgClosePath(context);
        nvgColor.free();
    }

    public static void drawVerticalLine(long context, float y, float height, float x, float thickness, Color color) {
        nvgBeginPath(context);
        nvgRect(context, x, y, 1, height);
        NVGColor nvgColor = nvgColor(color);
        nvgStrokeWidth(context, thickness);
        nvgStrokeColor(context, nvgColor);
        nvgStroke(context);
        nvgClosePath(context);
        nvgColor.free();
    }

    public static void drawVerticalLineGlow(long context, float y, float height, float x, float thickness, float maxThickness, Color color) {
        nvgBeginPath(context);
        nvgRect(context, x, y, 1, height);
        NVGColor nvgColor = nvgColor(color);
        NVGColor nvgColorGlow = nvgColor(ColorUtils.convertAlpha(color, 12));

        for (float i = maxThickness; i > thickness; i -= .15f) {
            nvgStrokeWidth(context, i);
            nvgStrokeColor(context, nvgColorGlow);
            nvgStroke(context);
        }
        nvgStrokeWidth(context, thickness);
        nvgStrokeColor(context, nvgColor);
        nvgStroke(context);
        nvgClosePath(context);
        nvgColor.free();
    }

    public static void drawVerticalLineGradiant(long context, float y, float height, float x, float thickness, Color color, Color color2) {
        nvgBeginPath(context);
        nvgRect(context, x, y, 1, height);
        NVGColor nvgColor = nvgColor(color);
        NVGColor nvgColor2 = nvgColor(color2);
        NVGPaint paint2 = NVGPaint.calloc();
        NVGPaint paint = NanoVG.nvgLinearGradient(context, x, y, x + thickness, height,
                nvgColor,
                nvgColor2, paint2);
        nvgFillPaint(context, paint);
        nvgFill(context);
        nvgClosePath(context);
        nvgColor.free();
        nvgColor2.free();
    }



    public static void drawDropShadow(long context, float x, float y, float width, float height, float radius, float blur, float spread, Color shadowColor, int alpha) {
        try (NVGPaint paint = NVGPaint.calloc()) {
            nvgBoxGradient(context, x - spread, y - spread, width + spread * 2, height + spread * 2, radius, blur, nvgColor(new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), alpha)), nvgColor(new Color(0x00000000, true)), paint);
            nvgBeginPath(context);
            nvgRect(context, x - 50, y - 50, width + 100, height + 100);
            nvgPathWinding(context, NVG_HOLE);
            nvgFillPaint(context, paint);
            nvgFill(context);
            nvgClosePath(context);
        }
    }

    public void drawImage(long context, AssetManager.Asset asset, float x, float y, float w, float h, float alpha) {

        int[] imgWidth = new int[]{1};
        int[] imgHeight = new int[]{1};

        nvgImageSize(context, asset.getImage(), imgWidth, imgHeight);

        try (NVGPaint paint = NVGPaint.calloc()) {
            NVGPaint paint1 = nvgImagePattern(context, x, y, w, h, 0, asset.getImage(), alpha / 255, paint);
            nvgBeginPath(context);
            nvgRect(context, x, y, w, h);
            nvgFillPaint(context, paint);
            nvgFill(context);
            nvgClosePath(context);

        }

    }

    public static int loadImageFromInputStream(long context, ByteBuffer buffer) {

        int imageHandle = 0;
        try {
            // Load image using NanoVG
            imageHandle = NanoVG.nvgCreateImageMem(context, 0, buffer);
            if (imageHandle == 0) {
                throw new RuntimeException("Failed to load image: " + buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageHandle;

    }

    public static NVGColor nvgColor(Color color) {
        NVGColor nvgColor = NVGColor.calloc();
        nvgColor.r(color.getRed() / 255.0f);
        nvgColor.g(color.getGreen() / 255.0f);
        nvgColor.b(color.getBlue() / 255.0f);
        nvgColor.a(color.getAlpha() / 255.0f);
        return nvgColor;
    }



/* exmaple code
    public static void drawVerticalLineGradiant(long context, float y, float height, float x, float thickness, Color color, Color color2) {
        nvgBeginPath(context);
        nvgRect(context, x, y, thickness, height);
        NVGColor nvgColor = nvgColor(color);
        NVGColor nvgColor2 = nvgColor(color2);
        NVGPaint paint2 = NVGPaint.calloc();
        NVGPaint paint = NanoVG.nvgLinearGradient(context, x, y, x + thickness, y,
                nvgColor,
                nvgColor2, paint2);
        nvgFillPaint(context, paint);
        nvgFill(context);
        nvgClosePath(context);
        nvgColor.free();
    } */
}
