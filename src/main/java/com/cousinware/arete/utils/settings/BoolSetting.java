package com.cousinware.arete.utils.settings;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoolSetting extends Setting<Boolean> {

    SettingAction enableAction = null;
    SettingAction disableAction = null;
    SettingAction toggleAction = null;

    public BoolSetting setValue(boolean val) {
        this.value = val;
        if (val) onEnable();
        else onDisable();
        return this;
    }

    public BoolSetting setName(String name) {
        this.name = name;
        return this;
    }

    public BoolSetting setDescription(String description) {
        this.description = description;
        return this;
    }


    public BoolSetting build(Module parent) {
        this.parent = parent;
        //registers setting
        AreteClient.settingManager.registerSetting(this);
        return this;
    }

    public BoolSetting build(Module parent, BoolSettingContainer boolSettingContainer, String id) {
        this.parent = parent;
        //registers setting
        this.id = id;
        boolSettingContainer.addSetting(this);
        AreteClient.settingManager.registerExtraSetting(this);
        return this;
    }

    public void onDisable() {
        if (toggleAction != null) {
            try {
                toggleAction.execute();
            } catch (Exception ignored) {
            }
        }
        if (disableAction == null) return;
        try {
            disableAction.execute();
        } catch (Exception ignored) {
        }
    }

    public void onEnable() {
        if (toggleAction != null) {
            try {
                toggleAction.execute();
            } catch (Exception ignored) {
            }
        }

        if (enableAction == null) return;
        try {
            enableAction.execute();
        } catch (Exception ignored) {
        }
    }



}

