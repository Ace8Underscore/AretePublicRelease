package com.cousinware.arete.utils.settings;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;

public class IntSetting extends Setting<Integer> {

    int min;
    int max;

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public IntSetting setMin(int min) {
        this.min = min;
        return this;
    }

    public IntSetting setMax(int max) {
        this.max = max;
        return this;
    }

    public IntSetting setValue(int val) {
        this.value = val;
        return this;
    }

    public IntSetting setName(String name) {
        this.name = name;
        return this;
    }

    public IntSetting build(Module parent) {
        this.parent = parent;
        //registers setting
        AreteClient.settingManager.registerSetting(this);
        return this;
    }
}
