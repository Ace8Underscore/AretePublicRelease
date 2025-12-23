package com.cousinware.arete.module.render;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RenderWorldEvent;
import com.cousinware.arete.managers.Render3DManager;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.ColorUtils;
import com.cousinware.arete.utils.game.EntityUtil;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.BoolSettingContainer;
import com.cousinware.arete.utils.settings.DoubleSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;

public class EntityESP extends Module {

    public static BoolSettingContainer targets = new BoolSettingContainer();
    public static BoolSetting players = new BoolSetting();
    public static BoolSetting passive = new BoolSetting();
    public static BoolSetting hostile = new BoolSetting();
    public static BoolSetting neutral = new BoolSetting();
    public static DoubleSetting distance = new DoubleSetting();
    int i = 0;
    int delay = 0;

    public EntityESP() {
        super("EntityESP", Category.Render, -1, "Render Entities");
        targets.setName("Targets").setValue(true).build(this);
        players.setName("Players").setValue(true).build(this, targets, "EntityESPTargetPlayers");
        passive.setName("Passive").setValue(true).build(this, targets, "EntityESPTargetPassive");
        hostile.setName("Hostile").setValue(true).build(this, targets, "EntityESPTargetHostile");
        neutral.setName("Neutral").setValue(false).build(this, targets, "EntityESPTargetNeutral");
        distance.setName("Distance").setMin(10).setValue(150).setMax(500).build(this);
    }

    public void onUpdate() {

    }


    @Subscribe
    public void renderWorld(RenderWorldEvent event) {

        //
//        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
//        VertexConsumer vertexConsumer = immediate.getBuffer(RenderLayer.getLines());
//        Vec3d pos = mc.player.getPos();
//        Box box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
//        VertexRendering.drawBox(event.getMatrixStack(), vertexConsumer, box, 100, 100, 100, 255);
//        immediate.draw();


        //
        for (Entity entity : mc.world.getEntities()) {
            if (!entity.isAlive()) continue;
            if ((entity instanceof ItemEntity)) continue;
            if (!players.getValue() && entity instanceof PlayerEntity || entity.getName().equals(mc.player.getName()))
                continue;
            if (entity instanceof PlayerEntity && AreteClient.friendManager.isFriend(entity.getName().getString()))
                continue;
            if (!passive.getValue() && entity instanceof PassiveEntity) continue;
            if (!hostile.getValue() && entity instanceof HostileEntity) continue;
            if (mc.player.distanceTo(entity) >= distance.getValue()) continue;
            if (!hostile.getValue() && EntityUtil.isNeutralEntityHostile(entity)) continue;
            if (!neutral.getValue() && EntityUtil.isNeutralEntity(entity)) continue;

            Color entityColor = EntityUtil.getEntityColor(entity);
            AreteClient.render3DManager.postRender(Render3DManager.RenderMode.SolidBlock, entity, event, ColorUtils.convertAlpha(entityColor, 75), "EntityESPSolid" + entity.getId(), 500f);
            AreteClient.render3DManager.postRender(Render3DManager.RenderMode.BlockOutline, entity, event, ColorUtils.convertAlpha(entityColor, 150), "EntityESPOutline" + entity.getId(), 500f);
        }

    }
}
