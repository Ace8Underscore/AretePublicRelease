package com.cousinware.arete.utils.settings;


import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;

public class DoubleSetting extends Setting<Double> {

    double min;
    double max;

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public DoubleSetting setMin(double min) {
        this.min = min;
        return this;
    }

    public DoubleSetting setMax(double max) {
        this.max = max;
        return this;
    }

    public DoubleSetting setValue(double val) {
        this.value = val;
        return this;
    }

    public DoubleSetting setName(String name) {
        this.name = name;
        return this;
    }

    public DoubleSetting build(Module parent) {
        this.parent = parent;
        //registers setting
        AreteClient.settingManager.registerSetting(this);
        return this;
    }
}
