package com.cousinware.arete.utils.guis.clickgui.newgui.component;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.MinecraftInterface;
import com.cousinware.arete.utils.guis.clickgui.newgui.Frame;
import com.cousinware.arete.utils.guis.clickgui.newgui.component.settings.*;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.NVGWrapper;
import com.cousinware.arete.utils.settings.*;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;

@Getter
public class ModuleComponent extends Component implements MinecraftInterface {

    //this is height of this module with settings and all
    float totalYOffset;

    ArrayList<Setting> settings;
    ArrayList<Component> settingComponent;
    Frame parent;
    Module module;
    boolean opened;
    Color backgroundColor = new Color(22, 30, 48, 255);

    int lastMouseX = 0;
    int lastMouseY = 0;

    public ModuleComponent(Frame parent, Module module, float x, float y, float offSet) {
        super(parent.width, parent.height, module.getDescription(), null);
        settingComponent = new ArrayList<>();

        this.x = x;
        this.y = y + offSet;
        this.parent = parent;
        this.module = module;

        settings = AreteClient.moduleManager.getModuleByName(module.getName()).getSettings();
        initSettings();

    }

    public void initSettings() {
        for (Setting<?> setting : settings) {
            mapSetting(setting);

        }

        //finally init bind
        settingComponent.add(new BindComponent(this, module));
    }

    public void mapSetting(Setting<?> setting) {
        if (setting instanceof BoolSetting setting1) {
            settingComponent.add(new BoolSettingComponent(this, module, setting1));

        } else if (setting instanceof BoolSettingContainer setting1) {
            settingComponent.add(new BoolContainerSettingComponent(this, module, setting1));

        } else if (setting instanceof DoubleSetting setting1) {
            settingComponent.add(new SliderSettingComponent(this, module, setting1));

        } else if (setting instanceof IntSetting setting1) {
            settingComponent.add(new SliderSettingComponent(this, module, setting1));

        } else if (setting instanceof ModeSetting setting1) {
            settingComponent.add(new ModeSettingComponent(this, module, setting1));

        } else if (setting instanceof StringSetting setting1) {
            settingComponent.add(new StringSettingComponent(this, module, setting1));
        }

    }

    public void click(double mouseX, double mouseY, int button, int action) {

        boolean collided = isCollided((int) mouseX, (int) mouseY);
        if (collided) {
            if (button == 1 && action == 1) {
                opened = !opened;
                settingComponent.forEach(Component::opened);
            }
            if (button == 0 && action == 1) module.toggle();
        }
        if (isOpened()) settingComponent.forEach(component -> {
            if (component.isVisible()) component.click(mouseX, mouseY, button, action);
        });
        this.lastMouseX = (int) mouseX;
        this.lastMouseY = (int) mouseY;
        if (module.getCategory().equals(Module.Category.Hud)) doHudClick(mouseX, mouseY, button, action);
    }

    public void charTyped(char chr, int modifiers) {
        settingComponent.forEach(component -> {
            if (component instanceof StringSettingComponent component1) {
                component1.charTyped(chr, modifiers);
            } else if (component instanceof BoolContainerSettingComponent component1) {
                component1.charTyped(chr, modifiers);
            }
        });
    }


    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        settingComponent.forEach(component -> {
            if (component instanceof StringSettingComponent component1) {
                component1.keyPressed(keyCode, scanCode, modifiers);
            } else if (component instanceof BoolContainerSettingComponent component1) {
                component1.keyPressed(keyCode, scanCode, modifiers);
            }
        });
        return true;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (module.getCategory().equals(Module.Category.Hud)) doHudDragging(mouseX, mouseY);
        totalYOffset = 0;
        if (opened) {
            //offset for settings
            totalYOffset += getSettingYOffset();
        }
        //offset for module area
        totalYOffset += parent.height;


        NVGContext.render(ctx -> {
            doScaling(parent.getCategory(), ctx);
            NVGWrapper.drawRect(ctx, x + 1, y, parent.width - 1, parent.height + 1, backgroundColor);
            hoverEffect(mouseX, mouseY, ctx);
            AreteClient.fontManager.drawText(null, module.getName(), x + 7.5f, y + 3, module.isEnabled() ? parent.getMainColor() : parent.getSecondaryColor(), true);
            if (isOpened()) {
                AreteClient.fontManager.drawCenteredText(null, AreteClient.fontManager.selectedFont, "+", (int) (x + parent.width - 10), (int) (y + 3), parent.getMainColor(), true);
            } else {
                AreteClient.fontManager.drawCenteredText(null, AreteClient.fontManager.selectedFont, "-", (int) (x + parent.width - 10), (int) (y + 3), parent.getSecondaryColor(), true);
            }
        });

        settingComponent.forEach(component -> {
            if (isOpened() && component.isVisible()) component.render(mouseX, mouseY);
        });

    }

    public void doHudClick(double mouseX, double mouseY, int button, int action) {

        if (module.isColliding((int) mouseX, (int) mouseY) && button == 0) {
            if (action == 1) {
                module.setDragging(true);
            } else if (action == 0) {
                module.setDragging(false);

            }
        } else {
            module.setDragging(false);
        }

        if (button == 1 && action == 1 && module.isColliding((int) mouseX, (int) mouseY)) {
            //right click which changes text ordering mode
            module.getTextMode().advance();
        }
    }

    public void doHudDragging(int mouseX, int mouseY) {
        if (module.isDragging()) {
            int updatedX = (int) module.getXSetting().getValue().floatValue();
            int updatedY = (int) module.getYSetting().getValue().floatValue();
            if (mouseX != lastMouseX) updatedX += mouseX - lastMouseX;
            if (mouseY != lastMouseY) updatedY += mouseY - lastMouseY;

            module.getXSetting().setValue(updatedX);
            module.getYSetting().setValue(updatedY);


            lastMouseX = mouseX;
            lastMouseY = mouseY;
        }

        module.setHovered(module.isColliding(mouseX, mouseY));
    }

    public float getSettingYOffset() {
        float totalOffset = 0;
        for (Component settingComp : settingComponent) {
            if (!settingComp.isVisible()) continue;
            if (settingComp instanceof BoolContainerSettingComponent boolContainerComponent) {
                if (boolContainerComponent.isOpened()) {
                    //add this setting before so it will be rendered above its sub settings
                    totalOffset += parent.height;
                    settingComp.updatePos(parent.x, y + totalOffset);
                    for (Component containerSetting : boolContainerComponent.getSettingComponents()) {
                        if (!settingComp.isVisible()) continue;
                        totalOffset += parent.height;
                        containerSetting.updatePos(parent.x, y + totalOffset);
                    }
//                    totalOffset += boolContainerComponent.getSettingArrayList().size() * parent.height;
//                    settingComp.updatePos(parent.x, totalOffset);
//                    continue;
                    continue;
                }
            }
            totalOffset += parent.height;
            settingComp.updatePos(parent.x, y + totalOffset);
        }
        return totalOffset;
    }

//    public boolean isCollided(int mouseX, int mouseY) {
//        return (mouseX <= x + parent.width && mouseX >= x) && (mouseY <= y + parent.height && mouseY >= y);
//    }
}
