package com.cousinware.arete.utils.settings;


import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DoubleSetting extends Setting<Double> {

    double min;
    double max;
    @Getter
    @Setter
    SettingAction modifyAction = null;

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
        if (modifyAction != null) try {
            modifyAction.execute();
        } catch (Exception ignored) {
        }
        return this;
    }

    public DoubleSetting setName(String name) {
        this.name = name;
        return this;
    }

    public DoubleSetting setDescription(String description) {
        this.description = description;
        return this;
    }

    public DoubleSetting build(Module parent) {
        this.parent = parent;
        //registers setting
        AreteClient.settingManager.registerSetting(this);
        return this;
    }

    public DoubleSetting build(Module module, BoolSettingContainer boolSettingContainer, String id) {
        this.parent = module;
        //registers setting
        boolSettingContainer.addSetting(this);
        this.id = id;
        AreteClient.settingManager.registerExtraSetting(this);
        return this;
    }
}
