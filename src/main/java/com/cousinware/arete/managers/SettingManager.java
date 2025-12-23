package com.cousinware.arete.managers;

import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.Setting;
import lombok.Getter;

import java.util.ArrayList;

public class SettingManager {

    ArrayList<Setting<?>> settingArrayList = new ArrayList<>();
    @Getter
    ArrayList<Setting<?>> extraSettingArrayList = new ArrayList<>();

    public SettingManager() {

    }

    public void registerSetting(Setting<?> setting) {
        settingArrayList.add(setting);
        setting.getParent().addSetting(setting);
    }

    public void registerExtraSetting(Setting<?> setting) {
        extraSettingArrayList.add(setting);
    }

    public ArrayList<Setting<?>> getRegisteredSettings() {
        return this.settingArrayList;
    }

    public ArrayList<Setting<?>> getAllSettings() {
        ArrayList<Setting<?>> arrayList = new ArrayList<>();
        arrayList.addAll(settingArrayList);
        arrayList.addAll(extraSettingArrayList);

        return arrayList;

    }

    public Setting<?> getSettingByName(Module module, String name) {
        for (Setting<?> setting : settingArrayList) {
            if (setting.getParent().equals(module) && setting.getName().equalsIgnoreCase(name)) return setting;
        }
        return null;
    }
}
