package com.cousinware.arete.utils.guis.clickgui.newgui.component.settings;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.guis.clickgui.newgui.component.Component;
import com.cousinware.arete.utils.guis.clickgui.newgui.component.ModuleComponent;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.NVGWrapper;
import com.cousinware.arete.utils.settings.BoolSetting;
import lombok.Getter;

@Getter
public class BoolSettingComponent extends Component {

    ModuleComponent parent;
    Module module;
    BoolSetting setting;

    public BoolSettingComponent(ModuleComponent parent, Module module, BoolSetting setting) {
        super(parent.width, parent.height, setting.getDescription(), parent);
        this.parent = parent;
        this.module = module;
        this.setting = setting;
    }

    public void click(double mouseX, double mouseY, int button, int action) {
        boolean collided = isCollided((int) mouseX, (int) mouseY);
        if (collided) {
            if (button == 0 && action == 1) setting.setValue(!setting.getValue());
        }
    }

    @Override
    public void render(int mouseX, int mouseY) {
        NVGContext.render(ctx -> {
            doScaling(parent.getParent().getCategory(), ctx);
            //if (isCollided(parent.getParent().getLastMouseX(), parent.getParent().getLastMouseY())) NVGWrapper.drawRect(ctx, this.x + 1, y, parent.getParent().width - 1, parent.getParent().height + 1, parent.getBackgroundColor());

            NVGWrapper.drawRect(ctx, this.x + 1, y, parent.getParent().width - 1, parent.getParent().height + 1, parent.getBackgroundColor());

            hoverEffect(mouseX, mouseY, ctx);

            //NVGWrapper.drawVerticalLine(ctx, y + .5f, parent.getParent().height, x + 5, parent.getParent().getMainColor());
            AreteClient.fontManager.drawText(null, setting.getName(), x + 10f, y + 3, setting.getValue() ? parent.getParent().getMainColor() : parent.getParent().getSecondaryColor(), true);
        });
    }

//    public boolean isCollided(int mouseX, int mouseY) {
//        return (mouseX < x + parent.getParent().width && mouseX > x) && (mouseY < y + parent.getParent().height && mouseY > y);
//    }

}
