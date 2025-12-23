package com.cousinware.arete.utils.guis.clickgui.newgui.component.settings;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.guis.clickgui.newgui.component.Component;
import com.cousinware.arete.utils.guis.clickgui.newgui.component.ModuleComponent;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.NVGWrapper;
import com.cousinware.arete.utils.settings.ModeSetting;
import lombok.Getter;

import java.awt.*;

@Getter
public class ModeSettingComponent extends Component {

    ModuleComponent parent;
    Module module;
    ModeSetting setting;

    public ModeSettingComponent(ModuleComponent parent, Module module, ModeSetting setting) {
        super(parent.width, parent.height, setting.getDescription(), parent);
        this.parent = parent;
        this.module = module;
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        NVGContext.render(ctx -> {
            doScaling(parent.getParent().getCategory(), ctx);
            NVGWrapper.drawRect(ctx, this.x + 1, y, parent.getParent().width - 1, parent.getParent().height + 1, parent.getBackgroundColor());
            hoverEffect(mouseX, mouseY, ctx);
            AreteClient.fontManager.drawText(null, setting.getName(), x + 10, y + 3, parent.getParent().getMainColor(), true);
            AreteClient.fontManager.drawLeftStringText(null, AreteClient.fontManager.selectedFont, setting.getValue(), (int) (x - 6 + parent.getParent().width), (int) (y + 3), Color.WHITE, true);
        });
    }

    @Override
    public void click(double mouseX, double mouseY, int button, int action) {
        if (isCollided((int) mouseX, (int) mouseY) && button == 0 && action == 1) {
            setting.advance();

        }
    }

//    public boolean isCollided(int mouseX, int mouseY) {
//        return (mouseX < x + parent.getParent().width && mouseX > x) && (mouseY < y + parent.getParent().height && mouseY > y);
//    }


}
