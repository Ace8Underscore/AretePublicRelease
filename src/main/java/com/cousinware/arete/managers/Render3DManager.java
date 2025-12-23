package com.cousinware.arete.managers;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.events.event.RenderWorldEvent;
import com.cousinware.arete.utils.MinecraftInterface;
import com.cousinware.arete.utils.Timer;
import com.cousinware.arete.utils.rendering.Renderer3D;
import lombok.Getter;
import lombok.Setter;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;

@Setter
@Getter
public class Render3DManager {
    ArrayList<Render> currentRender;
    ArrayList<Render> deadRenders;
    boolean debug = false;
    Timer debugTimer = new Timer(1000);
    Timer renderTimer = new Timer(10);
    Timer renderTime;

    public Render3DManager() {
        //load custom render layers
        Renderer3D.loadRenderLayers();
        currentRender = new ArrayList<>();
        deadRenders = new ArrayList<>();
    }

    public void render(RenderWorldEvent event) {
        if (currentRender == null || deadRenders == null) return;
        //!with 700 renders it is taking 10ms to do all ticks and etc
        //!problem with this is its 10ms every frame. so it can really mess up fps
        //! IE i avg 165 but with 750 alive renders im at 80 and frame time feels less
        //TODO split these up. RenderWorld only renders boxes etc where update event does the logic this could help
        renderTime = new Timer();
        for (Render current : currentRender) {
            current.tick(event);
        }
        long timePassed = renderTime.timePassed(true);


        for (Render dead : deadRenders) {
            currentRender.remove(dead);
        }

        if (debug && debugTimer.canTick()) {
            Command.sendClientSideMessage("AliveRenders " + AreteClient.render3DManager.getCurrentRender().size() + " || " + timePassed + " ms timing: ", false);
            Command.sendClientSideMessage("DeadRenders " + AreteClient.render3DManager.getDeadRenders().size() + " || " + timePassed + " ms timing: ", false);
        }
        deadRenders.clear();
    }


    public Render postRender(RenderMode renderMode, Entity entity, RenderWorldEvent event, Color color, String id, float animationSpeed) {
        Render render = new Render(renderMode, entity, event, color, id, animationSpeed);
        for (Render render1 : currentRender) {
            if (render1.getId().equalsIgnoreCase(id) && correctColor(render1.color, color) && render1.entity.getId() == entity.getId()) {
                if (!render1.isFadingIn()) {
                    render1.restartAlpha();
                    render1.fadingIn = false;
                    render1.fadingOut = false;
                    render1.delayFadeOutTimer.reset();
                }
                render1.setEntity(entity);
                return render1;
            }
        }

        currentRender.add(render);
        return render;

    }

    public Render postRender(RenderMode renderMode, Box bb, Vec3d pos, RenderWorldEvent event, Color color, String id, float animationSpeed) {

        Render render = new Render(renderMode, bb, pos, event, color, id, animationSpeed);
        for (Render render1 : currentRender) {
            if (render1.getId().equalsIgnoreCase(id) && render1.getBb().equals(bb) && correctColor(render1.color, color)) {
                if (!render1.isFadingIn()) {
                    render1.restartAlpha();
                    render1.fadingIn = false;
                    render1.fadingOut = false;
                    render1.delayFadeOutTimer.reset();
                }
                return render1;
            }
        }
        currentRender.add(render);
        return render;

    }

    public Render postRender(RenderMode renderMode, Box bb, Vec3d pos, Color color, String id, float animationSpeed) {

        Render render = new Render(renderMode, bb, pos, color, id, animationSpeed);
        for (Render render1 : currentRender) {
            if (render1.getId().equalsIgnoreCase(id) && render1.getBb().equals(bb) && correctColor(render1.color, color)) {
                if (!render1.isFadingIn()) {
                    render1.restartAlpha();
                    render1.fadingIn = false;
                    render1.fadingOut = false;
                    render1.delayFadeOutTimer.reset();

                }
                return render1;
            }
        }
        currentRender.add(render);
        return render;

    }

    public Render postRender(RenderMode renderMode, Box bb, Vec3d pos, MatrixStack matrixStack, Color color, boolean fading, String id) {
        Render render = new Render(renderMode, bb, pos, matrixStack, color, fading, id);
        for (Render render1 : currentRender) {
            if (render1.getId().equalsIgnoreCase(id) && render1.getBb().equals(bb) && correctColor(render1.color, color)) {
                if (!render1.isFadingIn()) {
                    render1.restartAlpha();
                    render1.fadingIn = false;
                    render1.fadingOut = false;
                    render1.delayFadeOutTimer.reset();
                }
                return render1;
            }
        }
        currentRender.add(render);
        return render;

    }

    private boolean correctColor(Color color1, Color color2) {
        return color1.getRed() == color2.getRed() && color1.getBlue() == color2.getBlue() && color1.getGreen() == color2.getGreen();
    }


    public enum RenderMode {
        SolidBlock,
        BlockOutline,
        Floor,
        Custom
    }

    @Setter
    @Getter
    public class Render implements MinecraftInterface {

        String id;
        long aliveTime = System.currentTimeMillis();
        private Color color;
        private MatrixStack matrixStack;
        private Camera camera;
        private Box bb;
        private Vec3d pos;
        private RenderMode renderMode;
        private Animation animation;
        private float animationSpeed = 500;
        private Entity entity = null;

        double startingAlpha;
        boolean fadingIn = true;
        boolean fadingOut = false;
        Timer delayFadeOutTimer = new Timer(100);
        boolean dead = false;

        public Render(RenderMode renderMode, Entity entity, RenderWorldEvent event, Color color, String id, float animationSpeed) {
            this.renderMode = renderMode;
            this.bb = entity.getBoundingBox();
            this.entity = entity;
            this.pos = entity.getSyncedPos();
            this.matrixStack = event.getMatrixStack();
            this.camera = event.getCamera();
            this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 5);
            this.id = id;
            startingAlpha = color.getAlpha();
            animation = getNewAnimation();
            this.animationSpeed = animationSpeed;
        }

        public Render(RenderMode renderMode, Box bb, Vec3d pos, RenderWorldEvent event, Color color, String id, float animationSpeed) {
            this.renderMode = renderMode;
            this.bb = bb;
            this.pos = pos;
            //this.matrixStack = event.getMatrixStack();
            //this.camera = event.getCamera();
            this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 5);
            this.id = id;
            startingAlpha = color.getAlpha();
            animation = getNewAnimation();
            this.animationSpeed = animationSpeed;
        }

        public Render(RenderMode renderMode, Box bb, Vec3d pos, Color color, String id, float animationSpeed) {
            this.renderMode = renderMode;
            this.bb = bb;
            this.pos = pos;
            this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 5);
            this.id = id;
            startingAlpha = color.getAlpha();
            animation = getNewAnimation();
            this.animationSpeed = animationSpeed;
        }

        public Render(RenderMode renderMode, Box bb, Vec3d pos, MatrixStack matrixStack, Color color, boolean fading, String id) {
            this.renderMode = renderMode;
            this.bb = bb;
            this.pos = pos;
            this.matrixStack = matrixStack;
            this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 5);
            this.id = id;
            startingAlpha = color.getAlpha();
            animation = getNewAnimation();

        }

        public void tick(RenderWorldEvent event) {
            if (dead) return;
            if (entity != null) {
                double interpolatedX = entity.lastX + (entity.getX() - entity.lastX) * Renderer3D.getPartial();
                double interpolatedY = entity.lastY + (entity.getY() - entity.lastY) * Renderer3D.getPartial();
                double interpolatedZ = entity.lastZ + (entity.getZ() - entity.lastZ) * Renderer3D.getPartial();
                //Box entityBoundingBox = entity.getBoundingBox()
                setBb(new Box(interpolatedX - entity.getBoundingBox().getLengthX() / 2, interpolatedY, interpolatedZ - entity.getBoundingBox().getLengthX() / 2, interpolatedX + entity.getBoundingBox().getLengthX() / 2, interpolatedY + entity.getBoundingBox().getLengthY(), interpolatedZ + entity.getBoundingBox().getLengthZ() / 2));

            }
            if (fadingIn) {
                double alpha = startingAlpha * animation.getAnimationFactor();
                updateColorAlpha(alpha < 1 ? 1 : alpha);
                if (this.color.getAlpha() >= startingAlpha) {
                    fadingIn = false;
                    delayFadeOutTimer.reset();
                }

            } else if (!fadingOut) {
                if (delayFadeOutTimer.canTick()) {
                    fadingOut = true;
                    restartAlpha();
                }
            }

            if (!fadingIn && fadingOut) {
                updateColorAlpha(startingAlpha - (startingAlpha * animation.getAnimationFactor()));
            }

            render(event, pos);
            if (color.getAlpha() != 0) {
                if (debug) {
                    //System.out.println("alpha: " + color.getAlpha());
                }
            }

            if (color.getAlpha() <= 0) kill();
        }

        private Animation getNewAnimation() {
            return new me.surge.animation.Animation(() -> animationSpeed, true, () -> Easing.QUINT_OUT);
        }

        private void render(RenderWorldEvent event, Vec3d pos) {
            if (pos == null) return;
            if (renderMode.equals(RenderMode.BlockOutline)) {
                Renderer3D.drawBoxOutline(bb, event, color);
            } else if (renderMode.equals(RenderMode.SolidBlock)) {
                Renderer3D.drawSolidBox(bb, event, color);
            } else if (renderMode.equals(RenderMode.Floor)) {
                //Renderer3D.drawFloor(bb, pos, event, color);
            } else {

            }
        }

        private void restartAlpha() {
            updateColorAlpha(getStartingAlpha());
            animation = getNewAnimation();
        }

        private void kill() {
            dead = true;
            AreteClient.render3DManager.deadRenders.add(this);
        }

        public void updateColorAlpha(double alpha) {
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) alpha);
        }

        public long timeAlive() {
            return System.currentTimeMillis() - aliveTime;
        }

    }
}
