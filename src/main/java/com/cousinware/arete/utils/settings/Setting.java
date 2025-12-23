package com.cousinware.arete.utils.settings;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import lombok.Getter;

import java.io.IOException;

@Getter
public class Setting<T> {

    T value;
    String name;
    String description = "";
    public String id = "";
    boolean shown = true;

    Module parent;

    public String getName() {
        try {
            return this.name;
        } catch (Exception e) {
            e.printStackTrace();
            e.fillInStackTrace();
        }
        return this.name;
    }


    public Setting() {
    }


    public Setting<T> setShown(boolean shown) {
        this.shown = shown;
        //TODO refreshGUI

        return this;
    }

    public Setting<T> setValue(T value) {
        this.value = value;
        return this;
    }

    @FunctionalInterface
    public interface SettingAction {
        void execute() throws IOException;
    }

}
