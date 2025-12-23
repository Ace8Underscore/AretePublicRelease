package com.cousinware.arete.utils.settings;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class BoolSettingContainer extends Setting<Boolean> {

    ArrayList<Setting<?>> settingArrayList = new ArrayList<>();

    boolean opened = false;

    public BoolSettingContainer setValue(boolean val) {
        this.value = val;
        return this;
    }

    public BoolSettingContainer setName(String name) {
        this.name = name;
        return this;
    }

    public BoolSettingContainer setDescription(String description) {
        this.description = description;
        return this;
    }

    public BoolSettingContainer addSetting(Setting<?> setting) {
        settingArrayList.add(setting);
        return this;
    }

    public BoolSettingContainer build(Module parent) {
        this.parent = parent;
        //registers setting
        AreteClient.settingManager.registerSetting(this);
        return this;
    }

    @Override
    public Setting<Boolean> setShown(boolean shown) {
        this.shown = shown;
        //TODO refreshGUI
        return this;
    }
}
