package com.cousinware.arete.utils.guis.clickgui.newgui;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Client.NewClickGui;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.MinecraftInterface;
import com.cousinware.arete.utils.guis.clickgui.newgui.component.ModuleComponent;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.NVGWrapper;
import lombok.Getter;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;
import java.util.ArrayList;

@Getter
public class Frame implements MinecraftInterface {
    //size of the category frame
    public float height = 14;
    public float width = 125;
    public float x;
    public float y;
    public boolean isDragging = false;
    ArrayList<ModuleComponent> moduleComponents;
    //for scrolling
    float cursorLocation = 0;
    Module.Category category;
    int lastMouseX = 0;
    int lastMouseY = 0;
    Color mainColor = new Color(31, 241, 255, 255);
    Color secondaryColor = new Color(255, 0, 60, 255);
    Color seperateColor = new Color(44, 60, 81, 255);
    Color lighterGray = new Color(27, 37, 56, 255);
    Color backgroundColor = new Color(22, 30, 48, 255);


    public Frame(Module.Category category, float x, float y) {
        this.moduleComponents = new ArrayList<>();
        this.category = category;
        this.x = x;
        this.y = y;

        AreteClient.moduleManager.getModules().forEach(module -> {
            if (module.getCategory().equals(category)) moduleComponents.add(new ModuleComponent(this, module, 0, 0, 0));
        });
    }


    public void render(int mouseX, int mouseY) {
        updateColors();

        if (isDragging) doDragging(mouseX, mouseY);

        float yOffset = y + height + 1;
        for (ModuleComponent moduleComponent : moduleComponents) {
            moduleComponent.updatePos(this.x, yOffset);
            moduleComponent.render(mouseX, mouseY);
            yOffset += moduleComponent.getTotalYOffset();
        }
        //renders the rest of the border now that we know how far down the list goes
        renderCategoryFrame();
        renderBorder(yOffset);


    }

    public void updateColors() {
        mainColor = new Color(NewClickGui.rMainColor.getValue(), NewClickGui.gMainColor.getValue(), NewClickGui.bMainColor.getValue(), NewClickGui.aMainColor.getValue());
        secondaryColor = new Color(NewClickGui.rSecondaryColor.getValue(), NewClickGui.gSecondaryColor.getValue(), NewClickGui.bSecondaryColor.getValue(), NewClickGui.aSecondaryColor.getValue());
    }

    public void doDragging(int mouseX, int mouseY) {
        float scale = NewClickGui.scale.getValue().floatValue();
        float scaledMouseX = mouseX / scale;
        float scaledMouseY = mouseY / scale;
        //Flooring because if we dont Frame runs away from mouse due to floating point precession
        if (scaledMouseX != lastMouseX) x += (float) (Math.floor(scaledMouseX) - lastMouseX);
        if (scaledMouseY != lastMouseY) y += (float) (Math.floor(scaledMouseY) - lastMouseY);


        lastMouseX = (int) Math.floor(scaledMouseX);
        lastMouseY = (int) Math.floor(scaledMouseY);
    }

    public void mouseClicked(double mouseX, double mouseY, int button, int action) {
        moduleComponents.forEach(moduleComponent -> moduleComponent.click(mouseX, mouseY, button, action));

        boolean collided = isCollided((int) mouseX, (int) mouseY);
        float scale = NewClickGui.scale.getValue().floatValue();
        int scaledMouseX = (int) Math.floor(mouseX / scale);
        int scaledMouseY = (int) Math.floor(mouseY / scale);

        this.lastMouseX = scaledMouseX;
        this.lastMouseY = scaledMouseY;

        if (collided && button == 0) {

            if (action == 1) {
                isDragging = true;
            }
            if (action == 0) {
                isDragging = false;

            }

        } else {
            isDragging = false;
        }

    }

    public void charTyped(char chr, int modifiers) {
        //System.out.println("typed1");
        moduleComponents.forEach(moduleComponent -> moduleComponent.charTyped(chr, modifiers));
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        moduleComponents.forEach(moduleComponent -> moduleComponent.keyPressed(keyCode, scanCode, modifiers));
        return true;
    }

    public void renderBorder(float yOffset) {
        float realOffset = yOffset - y;
        NVGContext.render(ctx -> {
            NanoVG.nvgScale(ctx, NewClickGui.scale.getValue().floatValue(), NewClickGui.scale.getValue().floatValue());
            NVGWrapper.drawVerticalLineGlow(ctx, y + .5f, realOffset, x, 1f, 2f, mainColor);
            NVGWrapper.drawVerticalLineGlow(ctx, y + .5f, realOffset, x + width, 1f, 2f, mainColor);
            NVGWrapper.drawHorizontalLineGlow(ctx, x + .25f, width + .5f, yOffset + 1, 1f, 2f, mainColor);
        });
    }

    //The categoryFrame will also render the glowing extenral part around itself and all widgets below it till the end of the list or maxHeight is reached
    public void renderCategoryFrame() {

        NVGContext.render(ctx -> {
            NanoVG.nvgScale(ctx, NewClickGui.scale.getValue().floatValue(), NewClickGui.scale.getValue().floatValue());
            NVGWrapper.drawHorizontalLineGlow(ctx, x + .25f, width + .5f, y - .75f, 1f, 2f, mainColor);
            NVGWrapper.drawRect(ctx, x + 1, y, width - 1, height, lighterGray);
            NVGWrapper.drawHorizontalLine(ctx, x + 1, width - 1, y + height, seperateColor);
            NVGWrapper.drawHorizontalLine(ctx, x + 1, width - 1, y + height - .5f, seperateColor);
            NVGWrapper.drawHorizontalLine(ctx, x + 1, width - 1, y + height + .5f, seperateColor);
            AreteClient.fontManager.drawText(null, category.name(), x + 7.5f, y + 3, mainColor, true);
        });
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


}
