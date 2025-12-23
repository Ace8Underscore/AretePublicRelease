package com.cousinware.arete.utils.rendering;

import com.cousinware.arete.events.event.RenderWorldEvent;
import com.cousinware.arete.module.Client.Rendering;
import com.cousinware.arete.utils.MinecraftInterface;
import com.cousinware.arete.utils.Timer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.OptionalDouble;

import static net.minecraft.client.gl.RenderPipelines.*;
import static net.minecraft.client.render.RenderPhase.*;

public class Renderer3D implements MinecraftInterface {
    @Getter
    @Setter
    public static double partial;

    private static final Timer timer = new Timer(250);


    //CODE CLEAN UP TIME!!!!

    public static RenderLayer LINES_NO_DEPTH;


    public static RenderLayer SOLID_NO_DEPTH;

    public static void loadRenderLayers() {
        //RenderPipeline.Snippet RENDERTYPE_LINES_SNIPPET = RenderPipeline.builder(MATRICES_COLOR_FOG_SNIPPET).withVertexShader("core/rendertype_lines").withFragmentShader("core/rendertype_lines").withUniform("LineWidth", UniformType.FLOAT).withUniform("ScreenSize", UniformType.VEC2).withBlend(BlendFunction.TRANSLUCENT).withCull(false).withVertexFormat(VertexFormats.POSITION_COLOR_NORMAL, VertexFormat.DrawMode.LINES).withDepthWrite(false).buildSnippet();

        RenderPipeline SECOND_BLOCK_OUTLINE_TEST = RenderPipelines.register(RenderPipeline.builder(RENDERTYPE_LINES_SNIPPET).withLocation("pipeline/secondary_block_outline").withDepthTestFunction(Rendering.legitRendering.getValue() ? DepthTestFunction.LEQUAL_DEPTH_TEST : DepthTestFunction.NO_DEPTH_TEST).withDepthBias(-1, 1).withCull(false).build());

        LINES_NO_DEPTH = RenderLayer.of("lines_no_depth",
                1536,
                false,
                true,
                SECOND_BLOCK_OUTLINE_TEST,
                RenderLayer.MultiPhaseParameters.builder()
                        .lineWidth(new RenderPhase.LineWidth(OptionalDouble.empty())).lineWidth(new LineWidth(OptionalDouble.of(Rendering.outlineLineThickness.getValue()))).layering(VIEW_OFFSET_Z_LAYERING_FORWARD).target(ITEM_ENTITY_TARGET).build(false));


//        LINES_NO_DEPTH = RenderLayer.of(
//                "lines_no_depth",
//                VertexFormats.LINES,
//                VertexFormat.DrawMode.LINES,
//                1536,
//                RenderLayer.MultiPhaseParameters.builder()
//                        .program(LINES_PROGRAM)
//                        .lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(Rendering.outlineLineThickness.getValue())))
//                        .layering(VIEW_OFFSET_Z_LAYERING)
//                        .transparency(TRANSLUCENT_TRANSPARENCY)
//                        .depthTest(Rendering.legitRendering.getValue() ? LEQUAL_DEPTH_TEST : ALWAYS_DEPTH_TEST)
//                        .target(ITEM_ENTITY_TARGET)
//                        .writeMaskState(ALL_MASK)
//                        .cull(DISABLE_CULLING)
//                        .build(false)
//        );

        RenderPipeline FILLED_BOX = register(RenderPipeline.builder(POSITION_COLOR_SNIPPET).withLocation("pipeline/debug_filled_box").withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.TRIANGLE_STRIP).withDepthTestFunction(Rendering.legitRendering.getValue() ? DepthTestFunction.LEQUAL_DEPTH_TEST : DepthTestFunction.NO_DEPTH_TEST).withDepthBias(-1, 1).build());
        SOLID_NO_DEPTH = RenderLayer.of("solid_no_depth",
                1536,
                true,
                true,
                FILLED_BOX,
                RenderLayer.MultiPhaseParameters.builder().build(false));
//
//        SOLID_NO_DEPTH = RenderLayer.of("solid_no_depth",
//                VertexFormats.POSITION_COLOR,
//                VertexFormat.DrawMode.TRIANGLE_STRIP,
//                4194304,
//                false,
//                false,
//                RenderLayer.MultiPhaseParameters.builder()
//                        .program(POSITION_COLOR_PROGRAM) // colors?
//                        .depthTest(Rendering.legitRendering.getValue() ? LEQUAL_DEPTH_TEST : ALWAYS_DEPTH_TEST) // see through walls
//                        .transparency(TRANSLUCENT_TRANSPARENCY) // enables alpha
//                        .layering(VIEW_OFFSET_Z_LAYERING)// no z plane fighting
//                        .writeMaskState(ALL_MASK) // colors again
//                        .build(false)
//        );

    }

    public static void drawBoxOutline(Box bb, RenderWorldEvent event, Color color) {
        drawBoxOutline(bb, event, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static void drawSolidBox(Box bb, RenderWorldEvent event, Color color) {
        drawBox(bb, event, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }




    public static void drawBoxOutline(Box bb, RenderWorldEvent event, int r, int g, int b, int a) {

        Vec3d cameraPos = event.getCamera().getPos();
        double offsetX = bb.minX - cameraPos.getX();
        double offsetY = bb.minY - cameraPos.getY();
        double offsetZ = bb.minZ - cameraPos.getZ();

        // The shape should be defined LOCALLY (relative to its own origin)
        // We create a new box that is shifted to be at (0,0,0)
        VoxelShape shape = VoxelShapes.cuboid(bb.offset(-bb.minX, -bb.minY, -bb.minZ));

        event.getMatrixStack().push();

        // Translate the matrix to the block's relative position
        event.getMatrixStack().translate(offsetX, offsetY, offsetZ);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers();
        VertexConsumer vertexConsumers = immediate.getBuffer(LINES_NO_DEPTH);

        Color color = new Color(r, g, b, a);

        VertexRendering.drawOutline(event.getMatrixStack(), vertexConsumers, shape, 0, 0, 0, color.getRGB());

        event.getMatrixStack().pop();
        immediate.draw(LINES_NO_DEPTH);
    }

    public static void drawBox(Box bb, RenderWorldEvent event, int r, int g, int b, int a) {
        Vec3d cameraPos = event.getCamera().getPos();
        double offsetX = bb.minX - cameraPos.getX();
        double offsetY = bb.minY - cameraPos.getY();
        double offsetZ = bb.minZ - cameraPos.getZ();

        // The shape should be defined LOCALLY (relative to its own origin)
        // We create a new box that is shifted to be at (0,0,0)
        VoxelShape shape = VoxelShapes.cuboid(bb.offset(-bb.minX, -bb.minY, -bb.minZ));

        event.getMatrixStack().push();
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        //VertexConsumerProvider.Immediate immediate = new CustomVertexConsumerProvider(new BufferAllocator(786432), );



//shaders?? https://github.com/0x3C50/Renderer/blob/master/renderer/src/main/java/me/x150/renderer/shader/Shaders.java
        VertexConsumer vertexConsumers = immediate.getBuffer(SOLID_NO_DEPTH);
        float fadedAlphaNegate = 0;
        if (Rendering.glow.getValue()) {
            for (int i = 10; i >= 1; i--) {
                float fadedAlpha = a * 0.20f / i;
                Box glowBox = bb.expand(((double) i / 2) * 0.01);
                VertexRendering.drawFilledBox(
                        event.getMatrixStack(),
                        vertexConsumers,
                        glowBox.minX - cameraPos.getX(),
                        glowBox.minY - cameraPos.getY(),
                        glowBox.minZ - cameraPos.getZ(),
                        glowBox.maxX - cameraPos.getX(),
                        glowBox.maxY - cameraPos.getY(),
                        glowBox.maxZ - cameraPos.getZ(),
                        r / 255f, g / 255f, b / 255f, fadedAlpha / 255f
                );
                fadedAlphaNegate = fadedAlpha;
            }


        }


        VertexRendering.drawFilledBox(
                event.getMatrixStack(),
                vertexConsumers,
                bb.minX - cameraPos.getX(),
                bb.minY - cameraPos.getY(),
                bb.minZ - cameraPos.getZ(),
                bb.maxX - cameraPos.getX(),
                bb.maxY - cameraPos.getY(),
                bb.maxZ - cameraPos.getZ(),
                r / 255f, g / 255f, b / 255f, (a - fadedAlphaNegate) / 255f
        );
        event.getMatrixStack().pop();
        immediate.draw(SOLID_NO_DEPTH);
    }



    private static void stop(MatrixStack matrixStack) {
        matrixStack.pop();
    }


    //UTILS


    public static Box getBB(BlockPos pos) {
        if (mc.world.getBlockState(pos).getCollisionShape(mc.world, pos).isEmpty()) return new Box(pos);
        return mc.world.getBlockState(pos).getCollisionShape(mc.world, pos).getBoundingBox();
    }

    public static Box getBB(BlockPos pos1, BlockPos pos2) {
        int x1 = Math.min(pos1.getX(), pos2.getX());
        int x2 = Math.max(pos1.getX(), pos2.getX()) + 1;
        int y1 = Math.min(pos1.getY(), pos2.getY());
        int y2 = Math.max(pos1.getY(), pos2.getY()) + 1;
        int z1 = Math.min(pos1.getZ(), pos2.getZ());
        int z2 = Math.max(pos1.getZ(), pos2.getZ()) + 1;

        return new Box(x1, y1, z1, x2, y2, z2);
    }


//    @Deprecated
//    public static void renderFilledBox(Box box, RenderWorldEvent event, float r, float g, float b, float a) {
//
//
//        MatrixStack.Entry entry = event.getMatrixStack().peek();
//        Matrix4f matrix = entry.getPositionMatrix();
//        start(box, event.getMatrixStack(), event.getCamera());
//        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
//
//        buffer.color(r, g, b, a);
//        float minX = (float) box.minX;
//        float minY = (float) box.minY;
//        float minZ = (float) box.minZ;
//        float maxX = (float) box.maxX;
//        float maxY = (float) box.maxY;
//        float maxZ = (float) box.maxZ;
//
//        // Bottom face
//        addQuad(buffer, matrix, minX, minY, minZ, maxX, minY, minZ, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a);
//
//        // Top face
//        addQuad(buffer, matrix, minX, maxY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
//
//        // Front face
//        addQuad(buffer, matrix, minX, minY, maxZ, maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
//
//        // Back face
//        addQuad(buffer, matrix, minX, minY, minZ, maxX, minY, minZ, maxX, maxY, minZ, minX, maxY, minZ, r, g, b, a);
//
//        // Left face
//        addQuad(buffer, matrix, minX, minY, minZ, minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a);
//
//        // Right face
//        addQuad(buffer, matrix, maxX, minY, minZ, maxX, minY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, r, g, b, a);
//
//
//        //buffer.end();
//
//        BufferRenderer.drawWithGlobalProgram(buffer.end());
//        stop(event.getMatrixStack());
//
//
//    }

    @Deprecated
    private static void addQuad(BufferBuilder buffer, Matrix4f matrix,
                                float x1, float y1, float z1,
                                float x2, float y2, float z2,
                                float x3, float y3, float z3,
                                float x4, float y4, float z4,
                                float r, float g, float b, float a) {
        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a);
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a);
        buffer.vertex(matrix, x3, y3, z3).color(r, g, b, a);
        buffer.vertex(matrix, x4, y4, z4).color(r, g, b, a);
    }


}
