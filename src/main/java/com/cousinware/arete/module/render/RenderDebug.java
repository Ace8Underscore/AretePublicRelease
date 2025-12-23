package com.cousinware.arete.module.render;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.managers.Render3DManager;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.ColorUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.HashSet;

public class RenderDebug extends Module {

    HashSet<BlockPos> renderPos = new HashSet<>();

    public RenderDebug() {
        super("RenderDebug", Category.Render, -1, "Dev");
    }

    public void onUpdate() {
        renderPos.add(mc.player.getBlockPos());

        for (BlockPos pos : renderPos) {
            AreteClient.render3DManager.postRender(Render3DManager.RenderMode.BlockOutline, new Box(pos), pos.toCenterPos(), ColorUtils.convertAlpha(AreteClient.getClientColor(), 255), "RenderDebugOutline" + pos, 1000f);
            AreteClient.render3DManager.postRender(Render3DManager.RenderMode.SolidBlock, new Box(pos), pos.toCenterPos(), ColorUtils.convertAlpha(AreteClient.getClientColor(), 125), "RenderDebugSolid" + pos, 1000f);

        }
    }
}
