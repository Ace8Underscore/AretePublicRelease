package com.cousinware.arete.module;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.utils.rendering.nvg.string.coloredstring.ColoredString;
import com.cousinware.arete.utils.settings.DoubleSetting;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.cousinware.arete.utils.settings.Setting;
import lombok.Getter;
import lombok.Setter;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@Getter
public class Module {

    String name;
    Category category;
    @Getter
    @Setter
    boolean drawn = true;
    int hexDecimalColor;
    @Getter
    @Setter
    boolean saveToConfig = true;
    @Getter
    String description = "";
    ArrayList<Setting> settings = new ArrayList<>();
    int keybind = -9991;
    public static final MinecraftClient mc = MinecraftClient.getInstance();

    @Getter
    public final Animation hoverAnimation = new Animation(() -> 350F, false, () -> Easing.QUINT_OUT);


    public boolean prioritySetting = false;
    public Integer priority = 0;

    boolean enabled = false;

    //for HUD Modules
    @Setter
    private int realX = 0;
    @Setter
    private int realY = 0;
    private int lastWidth = -1;
    private int lastHeight = -1;

    @Getter
    private DoubleSetting xSetting;
    @Getter
    private DoubleSetting ySetting;
    @Getter
    private ModeSetting textMode;
    @Setter
    private boolean hovered = false;
    @Setter
    private boolean dragging = false;
    private final TextOrdering textOrdering = TextOrdering.Left;

    public Module(String name, Category category, int hexDecimalColor, String description) {
        this.name = name;
        this.category = category;
        this.hexDecimalColor = hexDecimalColor;
        this.description = description;

        if (category.equals(Category.Hud)) {
            xSetting = (DoubleSetting) new DoubleSetting().setName("x").setMin(0).setMax(3000).setValue(250).build(this).setShown(false);
            ySetting = (DoubleSetting) new DoubleSetting().setName("y").setMin(0).setMax(3000).setValue(250).build(this).setShown(false);
            textMode = new ModeSetting().setName("TextStyle").setValue("Left").setModes("Right", "Middle", "Left").build(this);
        }
    }

    public Module(String name, Category category, int hexDecimalColor) {
        this.name = name;
        this.category = category;
        this.hexDecimalColor = hexDecimalColor;
        if (category.equals(Category.Hud)) {
            xSetting = (DoubleSetting) new DoubleSetting().setName("x").setMin(0).setMax(3000).setValue(250).build(this).setShown(false);
            ySetting = (DoubleSetting) new DoubleSetting().setName("y").setMin(0).setMax(3000).setValue(250).build(this).setShown(false);
            textMode = new ModeSetting().setName("TextStyle").setValue("Left").setModes("Right", "Middle", "Left").build(this);
        }
    }

    public Integer getPriority() {
        return priority;
    }

    public void enablePriority(int priority) {
        prioritySetting = true;
        this.priority = priority;
    }

    public void onUpdate() {

    }

    public void onPostUpdate() {

    }

    public void onEnable() {
        AreteClient.eventBus.register(this);
    }

    public void onDisable() {
        //AreteClient.eventBus.unregister(this);
    }

    public void enable() {
        AreteClient.eventBus.register(this);
        this.enabled = true;
        try {
            onEnable();
        } catch (Exception ignored) {
        }

    }

    public void disable() {
        AreteClient.eventBus.unregister(this);
        this.enabled = false;
        onDisable();
    }

    public void toggle() {
        if (isEnabled()) {
            disable();
        } else if (!isEnabled()) {
            enable();
        }
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public void addSetting(Setting setting) {
        settings.add(setting);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getName() {
        return this.name;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public int getKeybind() {
        return keybind;
    }

    public Category getCategory() {
        return this.category;
    }

    @Nullable
    public ColoredString getHudInfo() {
        return null;
    }

    public String hudInfoPrefix() {
        return Formatting.GRAY + "[" + Formatting.WHITE;
    }

    public String hudInfoSuffix() {
        return Formatting.GRAY + "]";
    }

    //HUDS ONLY
    public boolean isColliding(int mouseX, int mouseY) {
        return (mouseX > getRealX() && mouseX < (getRealX() + ((com.cousinware.arete.utils.guis.clickgui.defaultguis.hudgui.HudInterface) this).hitBox()[0])) && (mouseY > ySetting.getValue() && mouseY < (ySetting.getValue() + ((com.cousinware.arete.utils.guis.clickgui.defaultguis.hudgui.HudInterface) this).hitBox()[1]));
    }

    public void resizeHud(int newWidth, int newHeight) {

        if (lastWidth == -1 || lastHeight == -1) {
            lastWidth = newWidth;
            lastHeight = newHeight;
            return;
        }
        if (mc.player != null && mc.world != null) {

            double scaleX = (double) newWidth / lastWidth;
            double scaleY = (double) newHeight / lastHeight;
            double offsetX = (this.getXSetting().getValue() / lastWidth) * ((com.cousinware.arete.utils.guis.clickgui.defaultguis.hudgui.HudInterface) this).hitBox()[0];
            double offsetY = (this.getYSetting().getValue() / lastHeight) * ((com.cousinware.arete.utils.guis.clickgui.defaultguis.hudgui.HudInterface) this).hitBox()[1];


            if (offsetX < (double) ((((com.cousinware.arete.utils.guis.clickgui.defaultguis.hudgui.HudInterface) this).hitBox()[0]) * 6) / 7) offsetX = 0;
            if (offsetY < (double) ((((com.cousinware.arete.utils.guis.clickgui.defaultguis.hudgui.HudInterface) this).hitBox()[1]) * 6) / 7) offsetY = 0;

            //((HudInterface) this).hitBox()[0]
            this.getXSetting().setValue((this.getXSetting().getValue() + offsetX) * scaleX);
            this.getYSetting().setValue((this.getYSetting().getValue() + offsetY) * scaleY);
        }

        lastWidth = newWidth;
        lastHeight = newHeight;
    }

    public enum Category {
        Combat,
        Misc,
        Player,
        Render,
        Client,
        Hud,
        Movement,
        World
    }

    public enum TextOrdering {
        Left,
        Centered,
        Right

    }
}
