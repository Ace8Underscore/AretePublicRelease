package com.cousinware.arete.module.movement;

import com.cousinware.arete.events.event.BlockPushPlayerEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.BoolSettingContainer;
import com.google.common.eventbus.Subscribe;

public class Velocity extends Module {

    public static BoolSettingContainer pushSetting = new BoolSettingContainer();
    public static BoolSetting waterPush = new BoolSetting();
    public static BoolSetting entityPush = new BoolSetting();
    public static BoolSetting blockPush = new BoolSetting();


    public Velocity() {
        super("Velocity", Category.Movement, -1);
        pushSetting.setValue(true).setName("Pushing").build(this).setDescription("Any setting enabled will cancel that type of vanilla velocity");
        waterPush.setValue(true).setName("Fluids").build(this, pushSetting, "WaterPush").setDescription("Stops fluid movement when the player isnt moving");
        entityPush.setValue(true).setName("Entity").build(this, pushSetting, "EntityPush");
        blockPush.setValue(true).setName("Block").build(this, pushSetting, "BlockPush");
    }

    @Subscribe
    public void blockPushVelocity(BlockPushPlayerEvent event) {
        if (pushSetting.getValue() && blockPush.getValue()) event.setCancelled(true);
    }
}
