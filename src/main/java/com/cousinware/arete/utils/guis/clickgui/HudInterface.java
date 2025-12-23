package com.cousinware.arete.utils.guis.clickgui.defaultguis.hudgui;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RenderOverlayEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.ColorUtils;
import com.cousinware.arete.utils.guis.clickgui.newgui.NewHudGui;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.NVGWrapper;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;


public interface HudInterface {

    @Subscribe
    void render(RenderOverlayEvent context);

    //length[0] width[1]
    int[] hitBox();

    default boolean shouldRender() {
        return MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().player != null;
    }

    default void drawOutline(Module module, DrawContext context) {

        int alpha = (int) (125 + (module.getHoverAnimation().getAnimationFactor() * 100));


        int xPos = (int) module.getXSetting().getValue().floatValue();
        int yPos = (int) module.getYSetting().getValue().floatValue();
        //x
        int length = hitBox()[0];
        //y
        int height = hitBox()[1];
        Module.TextOrdering textOrdering = getTextOrdering(module);

        if (textOrdering.equals(Module.TextOrdering.Left)) {
            module.setRealX((int) module.getXSetting().getValue().floatValue());
            setInBounds(module, context);
        } else if (textOrdering.equals(Module.TextOrdering.Centered)) {
            module.setRealX((int) (module.getXSetting().getValue().floatValue() - (length / 2)));
            setInBounds(module, context);
        } else if (textOrdering.equals(Module.TextOrdering.Right)) {
            module.setRealX((int) (module.getXSetting().getValue().floatValue() - hitBox()[0]));
            setInBounds(module, context);

        }


        if (MinecraftClient.getInstance().currentScreen != AreteClient.newHudGui)
            return;
        module.getHoverAnimation().setState(module.isHovered());


        if (textOrdering.equals(Module.TextOrdering.Left)) {
            setInBounds(module, context);
            NVGContext.render(vg -> {
                NVGWrapper.drawRect(vg, module.getRealX(), yPos, length, height, ColorUtils.convertAlpha(AreteClient.getClientColor().brighter(), alpha));
            });
        } else if (textOrdering.equals(Module.TextOrdering.Centered)) {
            setInBounds(module, context);
            NVGContext.render(vg -> {
                NVGWrapper.drawRect(vg, module.getRealX(), yPos, length, height, ColorUtils.convertAlpha(AreteClient.getClientColor().brighter(), alpha));
            });
        } else if (textOrdering.equals(Module.TextOrdering.Right)) {
            setInBounds(module, context);
            NVGContext.render(vg -> {
                NVGWrapper.drawRect(vg, module.getRealX(), yPos, length, height, ColorUtils.convertAlpha(AreteClient.getClientColor().brighter(), alpha));
            });
        }

    }

    default void setInBounds(Module module, DrawContext context) {
        int magicX = 0;
        if (module.getTextMode().getValue().equals("Left")) {
            if (module.getRealX() < 0) module.getXSetting().setValue(magicX);
            else if (module.getXSetting().getValue() + ((HudInterface) module).hitBox()[0] > MinecraftClient.getInstance().getWindow().getScaledWidth())
                module.getXSetting().setValue(MinecraftClient.getInstance().getWindow().getScaledWidth() - ((HudInterface) module).hitBox()[0]);

        } else if (module.getTextMode().getValue().equals("Right")) {
            magicX = ((HudInterface) module).hitBox()[0];

            if (module.getRealX() < 0) module.getXSetting().setValue(magicX);
            else if (module.getXSetting().getValue() > MinecraftClient.getInstance().getWindow().getScaledWidth())
                module.getXSetting().setValue(MinecraftClient.getInstance().getWindow().getScaledWidth());

        } else if (module.getTextMode().getValue().equals("Middle")) {
            magicX = ((HudInterface) module).hitBox()[0] / 2;

            if (module.getRealX() < 0) module.getXSetting().setValue(magicX);
            else if (module.getXSetting().getValue() + magicX > MinecraftClient.getInstance().getWindow().getScaledWidth())
                module.getXSetting().setValue(MinecraftClient.getInstance().getWindow().getScaledWidth() - magicX);
        }

        if (module.getYSetting().getValue() < 0) module.getYSetting().setValue(0);
        else if (module.getYSetting().getValue() + ((HudInterface) module).hitBox()[1] > MinecraftClient.getInstance().getWindow().getScaledHeight())
            module.getYSetting().setValue(MinecraftClient.getInstance().getWindow().getScaledHeight() - ((HudInterface) module).hitBox()[1]);


        boolean snapping = true;
        if (snapping && module.isHovered()) {
            if (NewHudGui.mouseX < (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) + 15 && NewHudGui.mouseX > (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - 15) {
                if (MinecraftClient.getInstance().currentScreen == AreteClient.newHudGui) {
                    NVGContext.render(nvg -> {
                        NVGWrapper.drawRect(nvg, MinecraftClient.getInstance().getWindow().getScaledWidth() / 2, 0, 1, MinecraftClient.getInstance().getWindow().getScaledHeight(), Color.YELLOW);

                    });
                    module.getXSetting().setValue((MinecraftClient.getInstance().getWindow().getScaledWidth() / 2));
                }
            }

            if (NewHudGui.mouseY < (MinecraftClient.getInstance().getWindow().getScaledHeight() / 2) + 15 && NewHudGui.mouseY > (MinecraftClient.getInstance().getWindow().getScaledHeight() / 2) - 15) {

                if (MinecraftClient.getInstance().currentScreen == AreteClient.newHudGui) {
                    NVGContext.render(nvg -> {
                        NVGWrapper.drawRect(nvg, 0, MinecraftClient.getInstance().getWindow().getScaledHeight() / 2, MinecraftClient.getInstance().getWindow().getScaledWidth(), 1, Color.YELLOW);

                    });
                    module.getYSetting().setValue((MinecraftClient.getInstance().getWindow().getScaledHeight() / 2) - ((HudInterface) module).hitBox()[1] / 2);
                }
            }

        }

    }

    default Module.TextOrdering getTextOrdering(Module module) {
        String value = module.getTextMode().getValue();
        if (value.equalsIgnoreCase("Left")) return Module.TextOrdering.Left;
        else if (value.equalsIgnoreCase("Right")) return Module.TextOrdering.Right;
        else return Module.TextOrdering.Centered;
    }

    default void drawText(Module module, DrawContext context, String text, int x, int y, Color color) {
        NVGContext.render(nvg -> {
            AreteClient.fontManager.drawText(context, text, x, y - 1, color, true);
        });



    }

    default float getTrueX(Module module) {
        float windowWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
        return module.getXSetting().getValue().floatValue() / windowWidth;
    }

    default float getTrueY(Module module) {
        float windowWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
        return module.getXSetting().getValue().floatValue() / windowWidth;
    }


}