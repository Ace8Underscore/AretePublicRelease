package com.cousinware.arete.module.misc;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.events.event.PlayerEnterRenderEvent;
import com.cousinware.arete.events.event.PlayerLeaveRenderEvent;
import com.cousinware.arete.events.event.PlayerLeaveServerEvent;
import com.cousinware.arete.events.event.RenderWorldEvent;
import com.cousinware.arete.managers.Render3DManager;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.ColorUtils;
import com.cousinware.arete.utils.server.API2b2t;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.BoolSettingContainer;
import com.cousinware.arete.utils.settings.IntSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NewPlayerDetector extends Module {


    public static IntSetting daysOld = new IntSetting();
    public static IntSetting r = new IntSetting();
    public static IntSetting g = new IntSetting();
    public static IntSetting b = new IntSetting();
    public static IntSetting a = new IntSetting();
    public static BoolSettingContainer render = new BoolSettingContainer();
    public static BoolSetting notify = new BoolSetting();
    List<String> newPlayers = new CopyOnWriteArrayList<>();


    public NewPlayerDetector() {
        super("NewPlayerDetector", Category.Misc, -1, "Detects new Players");
        daysOld.setName("DaysOld").setMin(1).setMax(365).setValue(30).build(this);
        notify.setName("Notify").setValue(true).setDescription("Sends Client Side Chat Message").build(this);
        r.setName("Red").setMin(0).setMax(255).setValue(110).build(this, render, "NewPlayerDetectorRedRender");
        g.setName("Green").setMin(0).setMax(255).setValue(35).build(this, render, "NewPlayerDetectorGreenRender");
        b.setName("Blue").setMin(0).setMax(255).setValue(202).build(this, render, "NewPlayerDetectorBlueRender");
        a.setName("Alpha").setMin(0).setMax(255).setValue(125).build(this, render, "NewPlayerDetectorAlphaRender");
        render.setName("Render").setValue(true).build(this);
    }


    public void onUpdate() {

    }

    @Subscribe
    public void renderWorld(RenderWorldEvent event) {
        if (!render.getValue()) return;
        for (Entity entity : mc.world.getEntities()) {
            if (newPlayers.contains(entity.getName().getString())) {

                Color entityColor = new Color(r.getValue(), g.getValue(), b.getValue(), a.getValue());
                AreteClient.render3DManager.postRender(Render3DManager.RenderMode.SolidBlock, entity, event, ColorUtils.convertAlpha(entityColor, 75), "NewPlayerDetectorSolid" + entity.getId(), 500f);
                AreteClient.render3DManager.postRender(Render3DManager.RenderMode.BlockOutline, entity, event, ColorUtils.convertAlpha(entityColor, 150), "NewPlayerDetectorOutline" + entity.getId(), 500f);

            }
        }
    }

    @Subscribe
    public void onLeaveServer(PlayerLeaveServerEvent event) {
        newPlayers.clear();
    }

    @Subscribe
    public void onPlayerEnterRender(PlayerEnterRenderEvent event) {
        Entity player = event.getPlayer();
        if (player.getName().equals(mc.player.getName())) return;
        //Command.sendClientSideMessage("Entered Range: " + event.getPlayer().getName().getString(), true);
        checkIfNewPlayer(player.getName().getString());
    }

    @Subscribe
    public void onPlayerLeaveRender(PlayerLeaveRenderEvent event) {
        String name = event.getPlayer().getName().getString();
        //Command.sendClientSideMessage("Left Range: " + name, true);
        newPlayers.remove(name);
    }


    public void checkIfNewPlayer(String name) {
        new Thread(() -> {
            String data = API2b2t.request("https://api.2b2t.vc/seen?playerName=" + name);
            if (data.isEmpty()) return;
            logic(data, name);

        }).start();
    }

    public void logic(String data, String name) {

        try {
            String jd = data.split(":")[1].substring(1, 11);
            LocalDate currentTime = LocalDate.now();

            String[] args = jd.split("-");

            LocalDate joinDate = LocalDate.of(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));

            double days = (currentTime.toEpochDay() - joinDate.toEpochDay());

            if (days < daysOld.getValue()) {
                if (notify.getValue())
                    Command.sendClientSideMessage("New Player " + name + " Joined " + days + " Days Ago", false);
                newPlayers.add(name);
            }
        } catch (Exception e) {

        }
    }


}
