package com.cousinware.arete.utils.settings;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IntSetting extends Setting<Integer> {

    int min;
    int max;
    @Getter
    @Setter
    SettingAction modifyAction = null;

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
        if (modifyAction != null) try {
            modifyAction.execute();
        } catch (Exception ignored) {
        }
        return this;
    }

    public IntSetting setName(String name) {
        this.name = name;
        return this;
    }

    public IntSetting setDescription(String description) {
        this.description = description;
        return this;
    }

    public IntSetting build(Module parent) {
        this.parent = parent;
        //registers setting
        AreteClient.settingManager.registerSetting(this);
        return this;
    }

    public IntSetting build(Module module, BoolSettingContainer boolSettingContainer, String id) {
        this.parent = module;
        //registers setting
        boolSettingContainer.addSetting(this);
        AreteClient.settingManager.registerExtraSetting(this);
        this.id = id;
        return this;
    }
}
