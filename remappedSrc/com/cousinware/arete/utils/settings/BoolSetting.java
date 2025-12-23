package com.cousinware.arete.utils.settings;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;

public class BoolSetting extends Setting<Boolean> {

    public BoolSetting setValue(boolean val) {
        this.value = val;
        return this;
    }

    public BoolSetting setName(String name) {
        this.name = name;
        return this;
    }

    public BoolSetting build(Module parent) {
        this.parent = parent;
        //registers setting
        AreteClient.settingManager.registerSetting(this);
        return this;
    }
}
