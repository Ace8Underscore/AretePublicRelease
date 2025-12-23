package com.cousinware.arete.utils.settings;

import com.cousinware.arete.module.Module;

public class Setting <T> {

    T value;
    String name;
    boolean shown = true;

    Module parent;

    public Setting() {
    }

    public T getValue() {
        return value;
    }


    public String getName() {
        return name;
    }

    public boolean isShown() {
        return shown;
    }

    public Setting<T> setShown(boolean shown) {
        this.shown = shown;
        return this;
    }

    public Setting<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public Module getParent() {
        return parent;
    }
}
