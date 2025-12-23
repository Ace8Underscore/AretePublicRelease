package com.cousinware.arete.utils.rendering.font.nvg;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;

import java.util.function.Consumer;

import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class NVGContext{

    public static long context = 0;
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void init() {
        context = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
        if (context == NULL) {
            //AreteClient..LOGGER.error("Couldn't initialize NanoVG", new RuntimeException());
        }
        //Fusion.LOGGER.info("NanoVG Initialized");
    }

    public static void render(Consumer<Long> drawCall) {
        float contentscale = (float) mc.getWindow().getScaleFactor();
        float width  = (int)(mc.getWindow().getFramebufferWidth() / contentscale);
        float height = (int)(mc.getWindow().getFramebufferHeight() / contentscale);
        if (mc.options.hudHidden || mc.world == null) return;
        nvgBeginFrame(context, width, height, contentscale);
        drawCall.accept(context);
        nvgEndFrame(context);
        restoreState();
    }


    public static void restoreState() {
        GlStateManager._disableCull();
        GlStateManager._disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
    }

}