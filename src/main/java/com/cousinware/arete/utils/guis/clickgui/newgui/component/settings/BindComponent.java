package com.cousinware.arete.utils.guis.clickgui.newgui.component.settings;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.KeyPressedEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.guis.clickgui.newgui.component.Component;
import com.cousinware.arete.utils.guis.clickgui.newgui.component.ModuleComponent;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.NVGWrapper;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

import java.awt.*;

public class BindComponent extends Component {

    ModuleComponent parent;
    Module module;
    boolean binding = false;

    public BindComponent(ModuleComponent parent, Module module) {
        super(parent.width, parent.height, "", parent);
        this.parent = parent;
        this.module = module;
        AreteClient.eventBus.register(this);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        String s;
        String translation = InputUtil.fromKeyCode(module.getKeybind(), 0).getTranslationKey();

        try {
            if (translation.split("\\.")[2].equalsIgnoreCase("-9991")) s = "None";
            else s = translation.split("\\.")[2].toUpperCase();
        } catch (Exception e) {
            s = "None";
        }

        String finalS = s;
        NVGContext.render(ctx -> {
            doScaling(parent.getParent().getCategory(), ctx);
            NVGWrapper.drawRect(ctx, this.x + 1, y, parent.getParent().width - 1, parent.getParent().height + 1, parent.getBackgroundColor());
            hoverEffect(mouseX, mouseY, ctx);
            //Text
            if (!binding) {
                AreteClient.fontManager.drawText(null, "KeyBind", x + 10f, y + 3, parent.getParent().getMainColor(), true);
                AreteClient.fontManager.drawLeftStringText(null, AreteClient.fontManager.selectedFont, finalS, (int) (x + parent.getParent().width - 6), (int) (y + 3), Color.WHITE, true);
            } else
                AreteClient.fontManager.drawText(null, "Listening...", x + 10f, y + 3, parent.getParent().getSecondaryColor(), true);
        });
    }

    @Override
    public void click(double mouseX, double mouseY, int button, int action) {
        if (isCollided((int) mouseX, (int) mouseY) && parent.isOpened()) {
            if (button == 0 && action == 1) {
                binding = true;
            }

        } else {
            binding = false;
        }
    }

    @Subscribe
    public void onMousePress(KeyPressedEvent event) {
        if (MinecraftClient.getInstance().currentScreen != AreteClient.newGui) return;

        if (binding) {
            //System.out.println(event.getKey());
            if (event.getKey() == 261 || event.getKey() == 256 || event.getKey() == 259) module.setKeybind(-9991);
            else module.setKeybind(event.getKey());
            binding = false;
        }
    }

//    public boolean isCollided(int mouseX, int mouseY) {
//        return (mouseX < x + parent.getParent().width && mouseX > x) && (mouseY < y + parent.getParent().height && mouseY > y);
//    }
}
