package com.cousinware.arete.module;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.utils.RotationSystem;
import com.cousinware.arete.utils.settings.Setting;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;

public class Module {

    String name;
    Category category;
    @Getter@Setter
    boolean drawn = true;
    int hexDecimalColor;
    String description = "";
    ArrayList<Setting> settings = new ArrayList<>();
    int keybind = -9991;
    public final MinecraftClient mc = MinecraftClient.getInstance();
    public static RotationSystem rotationSystem = null;

    public boolean prioritySetting = false;
    public Integer priority = 0;

    boolean enabled = false;

    public Module(String name, Category category, int hexDecimalColor, String description) {
        this.name = name;
        this.category = category;
        this.hexDecimalColor = hexDecimalColor;
        this.description = description;
    }

    public Module(String name, Category category, int hexDecimalColor) {
        this.name = name;
        this.category = category;
        this.hexDecimalColor = hexDecimalColor;
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
        onEnable();

    }

    public void disable() {
        AreteClient.eventBus.unregister(this);
        this.enabled = false;
        onDisable();
    }

    public void toggle() {
        if(isEnabled()) {
            disable();
        } else if (!isEnabled()){
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

    public String getHudInfo(){
        return "";
    }
    public enum Category {
        Combat,
        Misc,
        Player,
        Render,
        Client
    }
}
