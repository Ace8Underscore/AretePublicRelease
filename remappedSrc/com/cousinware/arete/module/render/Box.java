package com.cousinware.arete.module.render;

import com.cousinware.arete.events.event.RenderWorldEvent;
import com.cousinware.arete.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class Box extends Module {

    public Box() {
        super("Box", Category.Render, -1, "");
    }

    @Subscribe
    public void renderWorld(RenderWorldEvent event) {
        //WorldRenderer.drawBox(event);
        //MatrixStack
        //WorldRenderer.draw
    }
}
