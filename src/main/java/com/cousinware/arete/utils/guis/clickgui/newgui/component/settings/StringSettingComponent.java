package com.cousinware.arete.utils.guis.clickgui.newgui.component.settings;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.Timer;
import com.cousinware.arete.utils.guis.clickgui.newgui.component.Component;
import com.cousinware.arete.utils.guis.clickgui.newgui.component.ModuleComponent;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.NVGWrapper;
import com.cousinware.arete.utils.settings.StringSetting;
import lombok.Getter;

import java.awt.*;

@Getter
public class StringSettingComponent extends Component {

    ModuleComponent parent;
    Module module;
    StringSetting setting;
    boolean editing = false;
    Timer editBarTimer = new Timer(900);
    String editBar = "";

    public StringSettingComponent(ModuleComponent parent, Module module, StringSetting setting) {
        super(parent.width, parent.height, setting.getDescription(), parent);
        this.parent = parent;
        this.module = module;
        this.setting = setting;
    }


    @Override
    public void render(int mouseX, int mouseY) {

        NVGContext.render(ctx -> {
            doScaling(parent.getParent().getCategory(), ctx);
            //if (isCollided(parent.getParent().getLastMouseX(), parent.getParent().getLastMouseY())) NVGWrapper.drawRect(ctx, this.x + 1, y, parent.getParent().width - 1, parent.getParent().height + 1, parent.getBackgroundColor());

            NVGWrapper.drawRect(ctx, this.x + 1, y, parent.getParent().width - 1, parent.getParent().height + 1, parent.getBackgroundColor());

            hoverEffect(mouseX, mouseY, ctx);

            //NVGWrapper.drawVerticalLine(ctx, y + .5f, parent.getParent().height, x + 5, parent.getParent().getMainColor());

            //NVGWrapper.drawRect(ctx, this.x + 3, y + 1, barWidth(), parent.getParent().height - 1, parent.getParent().getMainColor().darker());

            AreteClient.fontManager.drawText(null, setting.getName(), x + 10f, y + 3, parent.getParent().getMainColor(), true);
            if (editBar.isBlank()) {
                if (editBarTimer.canTick()) editBar = "|";
            } else {
                if (editBarTimer.canTick()) editBar = "";
            }
            String displayText = editing ? setting.getValue() + editBar : setting.getValue();
            AreteClient.fontManager.drawLeftStringText(null, AreteClient.fontManager.selectedFont, displayText, (int) (x - 6 + parent.getParent().width), (int) (y + 3), !isEditing() ? Color.LIGHT_GRAY : Color.WHITE, true);
        });
    }

    @Override
    public void click(double mouseX, double mouseY, int button, int action) {
        if (isCollided((int) mouseX, (int) mouseY) && button == 0) {
            if (action == 1) {
                editing = !editing;
            }

        } else {
            editing = false;
        }
    }

    public void charTyped(char chr, int modifiers) {
        //if (!isEditing()) return;
        if (editing) {
            setting.setValue(setting.getValue() + chr);
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (editing) {
            if (keyCode == 259 && !setting.getValue().isEmpty())
                setting.setValue(setting.getValue().substring(0, setting.getValue().length() - 1));
        }
        return true;
    }

//    public boolean isCollided(int mouseX, int mouseY) {
//        return (mouseX < x + parent.getParent().width && mouseX > x) && (mouseY < y + parent.getParent().height && mouseY > y);
//    }


}
