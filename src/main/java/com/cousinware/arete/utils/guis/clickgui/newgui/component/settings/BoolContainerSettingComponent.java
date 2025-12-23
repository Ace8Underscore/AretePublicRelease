package com.cousinware.arete.utils.guis.clickgui.newgui.component.settings;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.guis.clickgui.newgui.component.Component;
import com.cousinware.arete.utils.guis.clickgui.newgui.component.ModuleComponent;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.NVGWrapper;
import com.cousinware.arete.utils.settings.*;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class BoolContainerSettingComponent extends Component {

    ModuleComponent parent;
    Module module;
    BoolSettingContainer setting;
    ArrayList<Setting<?>> settingArrayList;
    ArrayList<Component> settingComponents;
    boolean opened;

    public BoolContainerSettingComponent(ModuleComponent parent, Module module, BoolSettingContainer setting) {
        super(parent.width, parent.height, setting.getDescription(), parent);
        this.parent = parent;
        this.module = module;
        this.setting = setting;
        settingArrayList = setting.getSettingArrayList();

        settingComponents = new ArrayList<>();
        for (Setting<?> setting1 : settingArrayList) {
            mapSetting(setting1);
        }
    }

    public void click(double mouseX, double mouseY, int button, int action) {
        if (isOpened()) getSettingComponents().forEach(component -> {
            if (component.isVisible()) component.click(mouseX, mouseY, button, action);
        });
        boolean collided = isCollided((int) mouseX, (int) mouseY);
        if (collided) {
            if (button == 0 && action == 1) setting.setValue(!setting.getValue());
            if (button == 1 && action == 1) opened = !opened;

        }
    }

    @Override
    public void render(int mouseX, int mouseY) {
        NVGContext.render(ctx -> {
            doScaling(parent.getParent().getCategory(), ctx);
            NVGWrapper.drawRect(ctx, this.x + 1, y, parent.getParent().width - 1, parent.getParent().height + 1, parent.getBackgroundColor());
            hoverEffect(mouseX, mouseY, ctx);
            AreteClient.fontManager.drawText(null, setting.getName(), x + 10f, y + 3, setting.getValue() ? parent.getParent().getMainColor() : parent.getParent().getSecondaryColor(), true);
            if (isOpened()) {
                AreteClient.fontManager.drawText(null, "+", x + parent.getParent().width - 15, y + 3, parent.getParent().getMainColor(), true);
            } else {
                AreteClient.fontManager.drawText(null, "-", x + parent.getParent().width - 15, y + 3, parent.getParent().getSecondaryColor(), true);
            }
        });
        if (isOpened()) getSettingComponents().forEach(component -> {
            if (component.isVisible()) component.render(mouseX, mouseY);
        });
    }

    public void charTyped(char chr, int modifiers) {
        //if (!isEditing()) return;
        settingComponents.forEach(component -> {
            if (component instanceof StringSettingComponent settingComponent) {
                settingComponent.charTyped(chr, modifiers);
            }
        });
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        settingComponents.forEach(component -> {
            if (component instanceof StringSettingComponent settingComponent) {
                settingComponent.keyPressed(keyCode, scanCode, modifiers);
            }
        });
        return true;
    }

    public void mapSetting(Setting<?> setting) {
        if (setting instanceof BoolSetting setting1) {
            settingComponents.add(new BoolSettingComponent(parent, module, setting1));

        } else if (setting instanceof BoolSettingContainer setting1) {
            settingComponents.add(new BoolContainerSettingComponent(parent, module, setting1));

        } else if (setting instanceof DoubleSetting setting1) {
            settingComponents.add(new SliderSettingComponent(parent, module, setting1));

        } else if (setting instanceof IntSetting setting1) {
            settingComponents.add(new SliderSettingComponent(parent, module, setting1));

        } else if (setting instanceof ModeSetting setting1) {
            settingComponents.add(new ModeSettingComponent(parent, module, setting1));

        } else if (setting instanceof StringSetting setting1) {
            settingComponents.add(new StringSettingComponent(parent, module, setting1));
        }
    }

//    public boolean isCollided(int mouseX, int mouseY) {
//        return (mouseX < x + parent.getParent().width && mouseX > x) && (mouseY < y + parent.getParent().height && mouseY > y);
//    }

}
