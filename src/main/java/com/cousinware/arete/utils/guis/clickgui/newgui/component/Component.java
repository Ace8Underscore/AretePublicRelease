package com.cousinware.arete.utils.guis.clickgui.newgui.component;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Client.NewClickGui;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.ColorUtils;
import com.cousinware.arete.utils.guis.clickgui.newgui.component.settings.*;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.NVGWrapper;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;

public class Component {
    public float x;
    public float y;
    public float width;
    public float height;
    public String description;
    public ModuleComponent parent;
    public Animation animation = new me.surge.animation.Animation(() -> 350f, true, () -> Easing.QUINT_OUT);

    public Component(float width, float height, String description, ModuleComponent parent) {
        this.width = width;
        this.height = height;
        this.description = description;
        this.parent = parent;
    }

    //This will be grabbed for the next module
    float totalOffset;

    public void render(int mouseX, int mouseY) {


    }

    public void hoverEffect(int mouseX, int mouseY, long ctx) {
        animation.setState(isCollided(mouseX, mouseY));
        //NVGWrapper.drawRect(ctx, this.x + 2, y + 1, width - 3, height  - 1, ColorUtils.convertAlpha(new Color(22, 30, 48, 255).brighter().brighter(), (int) (225 * animation.getAnimationFactor())));
        NVGWrapper.drawRect(ctx, this.x + 3, y + 1, width - 5, height - 1, ColorUtils.convertAlpha(new Color(22, 30, 48, 255).brighter().brighter(), (int) (225 * animation.getAnimationFactor())));

    }

    public void updatePos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void click(double mouseX, double mouseY, int button, int action) {

    }

    public void renderDescription(String description, int mouseX, int mouseY) {
        float length = AreteClient.fontManager.getStringWidth(description, AreteClient.fontManager.selectedFont);
        if (NewClickGui.descriptionMode.getValue().equalsIgnoreCase("Minimal")) {
            NVGContext.render(ctx -> {
                AreteClient.fontManager.drawText(null, description, 1, MinecraftClient.getInstance().getWindow().getScaledHeight() - AreteClient.fontManager.getFontHeight(AreteClient.fontManager.selectedFont) - 7, AreteClient.getClientColor(), true);

            });
        } else if (NewClickGui.descriptionMode.getValue().equalsIgnoreCase("Hover")) {
            NVGContext.render(ctx -> {
                NVGWrapper.drawRect(ctx, (int) (mouseX - 7 - (length / 2)), mouseY - 7 - 2, length, 10, new Color(44, 60, 81, 125));
                AreteClient.fontManager.drawText(null, description, mouseX - 7 - (length / 2), mouseY - 7, AreteClient.getClientColor(), true);

            });
        } else {

        }
    }

    public boolean isCollided(int mouseX, int mouseY) {
        float scale = NewClickGui.scale.getValue().floatValue();

        float scaledMouseX = mouseX / scale;
        float scaledMouseY = mouseY / scale;

        return scaledMouseX > x &&
                scaledMouseX < x + width &&
                scaledMouseY > y &&
                scaledMouseY < y + height;
    }

    public void doScaling(Module.Category category, long ctx) {
        NanoVG.nvgScale(ctx, NewClickGui.scale.getValue().floatValue(), NewClickGui.scale.getValue().floatValue());
    }

    public boolean isVisible() {
        return switch (this) {
            case BoolSettingComponent component -> component.getSetting().isShown();
            case BoolContainerSettingComponent component -> component.getSetting().isShown();
            case ModeSettingComponent component -> component.getSetting().isShown();
            case SliderSettingComponent component -> component.getSetting().isShown();
            case StringSettingComponent component -> component.getSetting().isShown();
            default -> true;
        };
    }

    public void opened() {
        animation.setState(NewClickGui.glowOnOpen.getValue());
    }


}
