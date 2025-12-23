package com.cousinware.arete.utils.guis.clickgui.newgui.component.settings;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Client.NewClickGui;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.guis.clickgui.newgui.component.Component;
import com.cousinware.arete.utils.guis.clickgui.newgui.component.ModuleComponent;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.NVGWrapper;
import com.cousinware.arete.utils.settings.DoubleSetting;
import com.cousinware.arete.utils.settings.IntSetting;
import com.cousinware.arete.utils.settings.Setting;
import lombok.Getter;

import java.awt.*;

@Getter
public class SliderSettingComponent extends Component {

    ModuleComponent parent;
    Module module;
    Setting<?> setting;
    boolean isDragging = false;
    boolean hasBeenModified = false;

    float barWidth;

    public SliderSettingComponent(ModuleComponent parent, Module module, Setting<?> setting) {
        super(parent.width, parent.height, setting.getDescription(), parent);
        this.parent = parent;
        this.module = module;
        this.setting = setting;

        barWidth = barWidth();

    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (isDragging && (!setting.getName().equalsIgnoreCase("scale") || !setting.getParent().getName().equalsIgnoreCase("NewClickGui")))
            calcBarWidth(mouseX, mouseY);

        NVGContext.render(ctx -> {
            doScaling(parent.getParent().getCategory(), ctx);
            NVGWrapper.drawRect(ctx, this.x + 1, y, parent.getParent().width - 1, parent.getParent().height + 1, parent.getBackgroundColor());
            hoverEffect(mouseX, mouseY, ctx);
            NVGWrapper.drawRect(ctx, this.x + 3, y + 1, barWidth, parent.getParent().height - 1, parent.getBackgroundColor().brighter().brighter().brighter());

            AreteClient.fontManager.drawText(null, setting.getName(), x + 10f, y + 3, parent.getParent().getMainColor(), true);
            AreteClient.fontManager.drawLeftStringText(null, AreteClient.fontManager.selectedFont, String.valueOf(setting.getValue()), (int) (x - 6 + parent.getParent().width), (int) (y + 3), Color.WHITE, true);
        });
    }

    @Override
    public void click(double mouseX, double mouseY, int button, int action) {
        if (isCollided((int) mouseX, (int) mouseY) && button == 0) {

            if (action == 1) {
                isDragging = true;
            }
            if (action == 0) {
                if (isDragging) {
                    if (setting.getName().equalsIgnoreCase("scale") && setting.getParent().getName().equalsIgnoreCase("NewClickGui"))
                        calcBarWidth((int) mouseX, (int) mouseY);
                    isDragging = false;
                }

            }

        } else {
            if (isDragging) {
                if (setting.getName().equalsIgnoreCase("scale") && setting.getParent().getName().equalsIgnoreCase("NewClickGui"))
                    calcBarWidth((int) mouseX, (int) mouseY);
                isDragging = false;
            }
        }
    }

    public void calcBarWidth(int mouseX, int mouseY) {
        float newMouseX = (float) (mouseX / NewClickGui.scale.getValue());
        float sliderStart = this.x + 3;
        float sliderEnd = this.x + this.width - 4;
        float maxBarLength = sliderEnd - sliderStart;

        // clamp mouse X within slider bounds
        float clampedX = Math.max(sliderStart, Math.min(newMouseX, sliderEnd));

        // normalized 0.0–1.0 position
        float percent = (clampedX - sliderStart) / maxBarLength;
        percent = Math.max(0f, Math.min(percent, 1f)); // safety clamp

        // update the bar width
        barWidth = Math.max(1, percent * maxBarLength);

        // update setting value linearly
        if (setting instanceof IntSetting intSetting) {
            int min = intSetting.getMin();
            int max = intSetting.getMax();
            int value = Math.round(min + (max - min) * percent);
            intSetting.setValue(value);
        } else if (setting instanceof DoubleSetting doubleSetting) {
            double min = doubleSetting.getMin();
            double max = doubleSetting.getMax();
            double value = min + (max - min) * percent;
            // optional rounding to tenths
            value = Math.round(value * 10.0) / 10.0;
            doubleSetting.setValue(value);
        }
    }

    public float barWidth() {
        float maxSettingVal = (setting instanceof IntSetting setting1) ? setting1.getMax() : (float) ((DoubleSetting) setting).getMax();
        float value = (setting instanceof IntSetting setting1) ? setting1.getValue() : ((DoubleSetting) setting).getValue().floatValue();
        float minSettingVal = (setting instanceof IntSetting setting1) ? setting1.getMin() : (float) ((DoubleSetting) setting).getMin();
        if (value > maxSettingVal) value = maxSettingVal;
        else if (value < minSettingVal) value = maxSettingVal;

        if (!hasBeenModified) {

            float max = parent.getParent().width - 5;
            return (value / maxSettingVal) * max;
        }
        return 0;
    }


//    public boolean isCollided(int mouseX, int mouseY) {
//        return (mouseX < x + parent.getParent().width && mouseX > x) && (mouseY < y + parent.getParent().height && mouseY > y);
//    }


//    float maxSettingVal = (setting instanceof IntSetting setting1) ?  setting1.getMax() : (float) ((DoubleSetting) setting).getMax();
//    float minSettingVal = (setting instanceof IntSetting setting1) ?  setting1.getMin() : (float) ((DoubleSetting) setting).getMin();
//    float value = (setting instanceof IntSetting setting1) ?  setting1.getValue() : ((DoubleSetting) setting).getValue().floatValue();
//    float somevalue = mouseX - this.x - 1;
//
//    float goodValue = (somevalue / (width - 5)) * maxSettingVal;
//        if (goodValue > maxSettingVal) goodValue = maxSettingVal;
//        else if (goodValue < minSettingVal) goodValue = minSettingVal;
//        if (setting instanceof IntSetting setting1) {
//        setting1.setValue((int) goodValue);
//    } else if (setting instanceof DoubleSetting setting1){
//        setting1.setValue( Math.round(goodValue * 10.0) / 10.0);
//    }
//
//        return 0f;
}
