package com.cousinware.arete.managers;

import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.Setting;

import java.util.ArrayList;

public class SettingManager {

    ArrayList<Setting> settingArrayList = new ArrayList<>();

    public SettingManager() {

    }

    public void registerSetting(Setting setting) {
        settingArrayList.add(setting);
        setting.getParent().addSetting(setting);
    }

    public ArrayList<Setting> getRegisteredSettings() {
        return this.settingArrayList;
    }

    public Setting getSettingByName(Module module, String name) {
        for (Setting setting : settingArrayList) {
            if (setting.getParent().equals(module) && setting.getName().equalsIgnoreCase(name)) return setting;
        }
        return null;
    }
}
