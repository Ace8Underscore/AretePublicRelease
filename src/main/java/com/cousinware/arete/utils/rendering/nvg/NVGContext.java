package com.cousinware.arete.utils.rendering.nvg;

import com.cousinware.arete.client.AreteClient;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GlBackend;
import net.minecraft.client.texture.GlTexture;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.function.Consumer;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_ANTIALIAS;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_STENCIL_STROKES;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class NVGContext {

    public static long context = 0;
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void init() {
        //context = NanoVGGL3.nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
        //Bind framebuffer to fix Nanovg not rendering
        //bindFrameBuffer();

        context = NanoVGGL3.nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
        if (context == NULL) {
            AreteClient.LOGGER.error("Couldn't initialize NanoVG");
        } else {
            AreteClient.LOGGER.info("NVG LOADED");
        }
    }

    public static void bindFrameBuffer() {
        //Timer timer = new Timer();

        Framebuffer framebuffer = mc.getFramebuffer();
        GpuTexture gpuTexture = framebuffer.getColorAttachment();
        GpuTexture gpuTexture2 = framebuffer.getDepthAttachment();
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER,
                ((GlTexture) gpuTexture).getOrCreateFramebuffer(((GlBackend) RenderSystem.getDevice()).getFramebufferManager(), gpuTexture2));
        GlStateManager._viewport(0, 0, gpuTexture.getWidth(0), gpuTexture.getHeight(0));


        //System.out.println(timer.timePassed(true) + "ms");

    }

    public static void render(Consumer<Long> drawCall) {
        RenderSystem.assertOnRenderThread();
        float contentscale = (float) mc.getWindow().getScaleFactor();
        float width = (int) (mc.getWindow().getFramebufferWidth() / contentscale);
        float height = (int) (mc.getWindow().getFramebufferHeight() / contentscale);
        bindFrameBuffer();
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        GL11.glEnable(GL_DEPTH_TEST);

        nvgBeginFrame(context, width, height, contentscale);
        drawCall.accept(context);
        nvgEndFrame(context);
        restoreState2();


    }

    public static void renderText(Consumer<Long> drawCall) {
        float contentscale = (float) mc.getWindow().getScaleFactor();
        float width = (int) (mc.getWindow().getFramebufferWidth() / contentscale);
        float height = (int) (mc.getWindow().getFramebufferHeight() / contentscale);
        nvgSave(context);
        nvgBeginFrame(context, width, height, contentscale);

        drawCall.accept(context);
        nvgEndFrame(context);

    }

    public static NVGColor nvgColor(Color color) {
        NVGColor nvgColor = NVGColor.calloc();
        nvgColor.r(color.getRed() / 255.0f);
        nvgColor.g(color.getGreen() / 255.0f);
        nvgColor.b(color.getBlue() / 255.0f);
        nvgColor.a(color.getAlpha() / 255.0f);
        return nvgColor;
    }

    private static void setup() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);

    }

    private static void restoreState2() {
        GlStateManager._disableCull();
        GlStateManager._disableDepthTest();
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);

    }


    public static void restoreState() {
        GlStateManager._disableCull();
        GlStateManager._disableDepthTest();
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);


    }

}