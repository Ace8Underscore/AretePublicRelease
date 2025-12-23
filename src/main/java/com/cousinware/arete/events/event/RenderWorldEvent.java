package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

@AllArgsConstructor
@Getter
@Setter
public class RenderWorldEvent extends Event {
    MatrixStack matrixStack;
    Matrix4f matrix4f;
    Matrix4f positionStack;
    Camera camera;
}
