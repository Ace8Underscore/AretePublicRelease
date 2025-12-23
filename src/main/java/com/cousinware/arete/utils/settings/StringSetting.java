package com.cousinware.arete.utils.settings;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;

public class StringSetting extends Setting<String> {

    public StringSetting setValue(String val) {
        this.value = val;
        return this;
    }


    public StringSetting setName(String name) {
        this.name = name;
        return this;
    }

    public StringSetting setDescription(String description) {
        this.description = description;
        return this;
    }

    public StringSetting build(Module parent) {
        this.parent = parent;
        //registers setting
        AreteClient.settingManager.registerSetting(this);
        return this;
    }

    public StringSetting build(Module module, BoolSettingContainer boolSettingContainer, String id) {
        this.parent = module;

        //registers setting
        boolSettingContainer.addSetting(this);
        this.id = id;
        AreteClient.settingManager.registerExtraSetting(this);
        return this;
    }
}
