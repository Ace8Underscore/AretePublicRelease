package com.cousinware.arete.utils.settings;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;

public class ModeSetting extends Setting<String> {

    ArrayList<String> modes = new ArrayList<>();
    @Getter
    @Setter
    SettingAction changeMode;
    int index;

    public ModeSetting setValue(String val) {
        this.value = val;
        if (changeMode != null) try {
            changeMode.execute();
        } catch (Exception ignored) {
        }
        return this;
    }

    public void advance() {
        if (index == modes.size() - 1) {
            index = 0;
            setValue(modes.get(index));
        } else {
            index++;
            setValue(modes.get(index));
        }

    }

    public void setup() {
        for (int i = 0; i < modes.size(); i++) {
            if (modes.get(i).equalsIgnoreCase(this.getValue())) index = i;
        }
    }

    public ModeSetting setModes(String... strings) {
        modes.addAll(Arrays.asList(strings));
        return this;
    }

    public ArrayList<String> getModes() {
        return modes;
    }

    public ModeSetting setName(String name) {
        this.name = name;
        return this;
    }

    public ModeSetting setDescription(String description) {
        this.description = description;
        return this;
    }

    public ModeSetting build(Module parent) {
        this.parent = parent;
        setup();
        //registers setting
        AreteClient.settingManager.registerSetting(this);
        return this;
    }

    public ModeSetting build(Module module, BoolSettingContainer boolSettingContainer, String id) {
        this.parent = module;
        setup();
        //registers setting
        boolSettingContainer.addSetting(this);
        this.id = id;
        AreteClient.settingManager.registerExtraSetting(this);
        return this;
    }
}
