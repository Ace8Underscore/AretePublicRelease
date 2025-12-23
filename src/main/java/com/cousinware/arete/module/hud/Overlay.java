package com.cousinware.arete.module.hud;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.RenderOverlayEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.game.PlayerUtils;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.hudgui.HudInterface;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.cousinware.arete.utils.rendering.nvg.string.coloredstring.ColoredString;
import com.cousinware.arete.utils.rendering.nvg.string.coloredstring.ColoredStringComparator;
import com.cousinware.arete.utils.rendering.nvg.string.coloredstring.ColoredStringHelper;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.BoolSettingContainer;
import com.cousinware.arete.utils.settings.ModeSetting;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Overlay extends Module implements HudInterface {

    BoolSetting fps = new BoolSetting();
    BoolSetting tps = new BoolSetting();
    BoolSetting ping = new BoolSetting();
    BoolSetting server = new BoolSetting();
    BoolSetting potionEffect = new BoolSetting();

    BoolSettingContainer speed = new BoolSettingContainer();
    ModeSetting speedMode = new ModeSetting();


    BoolSettingContainer order = new BoolSettingContainer();
    ModeSetting ordering = new ModeSetting();
    BoolSetting autoOrder = new BoolSetting();

    float width = 0;
    float height = 0;
    ArrayList<ColoredString[]> message = new ArrayList<>();


    public Overlay() {
        super("Overylay", Category.Hud, -1);
        fps.setName("Fps").setValue(true).build(this);
        ping.setName("Ping").setValue(true).build(this);
        server.setName("Server").setValue(true).build(this);
        speed.setName("Speed").setValue(true).build(this);
        speedMode.setName("Unit").setValue("km/h").setModes("bp/s", "km/h").build(this, speed, "SpeedMode");
        potionEffect.setName("PotionEffects").setValue(true).build(this);
        order.setName("Ordering").setValue(true).build(this);
        ordering.setName("Order").setModes("Up", "Down").setValue("Down").build(this, order, "OrderingMode");
        autoOrder.setName("AutoOrder").setValue(true).setDescription("When below or above middle of screen text will re-order").build(this, order, "AutoOrder");
        setDrawn(false);
    }

    public void onEnable() {

    }

    public void onDisable() {
        message.clear();
    }


    public void onUpdate() {
        message.clear();

        if (speed.getValue()) {
            String speed = speedMode.getValue().equalsIgnoreCase("bp/s") ? String.valueOf(PlayerUtils.getSpeed(PlayerUtils.SpeedType.BPS)) : String.valueOf(PlayerUtils.getSpeed(PlayerUtils.SpeedType.KMPH));
            message.add(new ColoredString[]{new ColoredString(AreteClient.getClientColor(), "Speed "), new ColoredString(Color.WHITE, speed + speedMode.getValue())});
        }

        if (fps.getValue()) {

            message.add(new ColoredString[]{new ColoredString(AreteClient.getClientColor(), "Fps "), new ColoredString(Color.WHITE, String.valueOf(mc.getCurrentFps()))});
        }

        if (ping.getValue()) {
            if (mc.isInSingleplayer()) {
                message.add(new ColoredString[]{new ColoredString(AreteClient.getClientColor(), "Ping "), new ColoredString(Color.WHITE, String.valueOf(0))});

            } else {
                if (mc.player != null && mc.player.networkHandler != null && mc.player.getUuid() != null && mc.player.networkHandler.getListedPlayerListEntries() != null) {
                    //long ping = Objects.requireNonNull(mc.player.networkHandler.getPlayerListEntry(mc.player.getUuid())).getLatency();
                    long ping = 1;
                    message.add(new ColoredString[]{new ColoredString(AreteClient.getClientColor(), "Ping "), new ColoredString(Color.WHITE, String.valueOf(ping))});
                }
            }
        }

        if (server.getValue()) {
            if (mc.isInSingleplayer()) {
                message.add(new ColoredString[]{new ColoredString(AreteClient.getClientColor(), "Server "), new ColoredString(Color.WHITE, "localhost")});

            } else {
                String ip = mc.getNetworkHandler().getServerInfo().address;
                message.add(new ColoredString[]{new ColoredString(AreteClient.getClientColor(), "Server "), new ColoredString(Color.WHITE, ip)});
            }
        }


        //sort

        sort();

        if (potionEffect.getValue() && !mc.player.isSpectator()) {

            mc.player.getStatusEffects().forEach(statusEffectInstance -> {
                String name = statusEffectInstance.getEffectType().value().getName().getString();
                String amplifier = String.valueOf(statusEffectInstance.getAmplifier() + 1);
                String duration = getStatusEffectDuration(statusEffectInstance);
                Color statusColor = new Color(statusEffectInstance.getEffectType().value().getColor());

                message.addFirst(new ColoredString[]{new ColoredString(statusColor, name + " " + amplifier + " "), new ColoredString(Color.WHITE, duration)});

            });
        }

        if (ordering.getValue().equalsIgnoreCase("up") && !message.isEmpty()) {
            width = ColoredStringHelper.getWidth(message.getFirst());
        } else if (ordering.getValue().equalsIgnoreCase("down") && !message.isEmpty()) {
            width = ColoredStringHelper.getWidth(message.getFirst());
        } else {
            width = 16;
        }
    }

    public String getStatusEffectDuration(StatusEffectInstance statusEffectInstance) {
        StringBuilder finalDuration = new StringBuilder();
        String duration = StatusEffectUtil.getDurationText(statusEffectInstance, 1, 20).getLiteralString();
        if (duration == null) return "∞";
        for (int i = 0; i < duration.split(":").length; i++) {
            String current = duration.split(":")[i];
            if (current.startsWith("00")) {
                if (i == duration.split(":").length - 1) finalDuration.append(current);
                else finalDuration.append("0");
            } else if (current.startsWith("0")) {
                if (i == duration.split(":").length - 1) finalDuration.append(current);
                else finalDuration.append(current.charAt(1));
            } else {
                finalDuration.append(current);
                //continue;
            }

            if (i != duration.split(":").length - 1) finalDuration.append(":");
        }
        if (finalDuration.toString().startsWith("-")) return finalDuration.substring(1);
        return finalDuration.toString();
    }

    @Override
    public void render(RenderOverlayEvent context) {
        drawOutline(this, context.getContext());

        doOrdering();

        AtomicInteger offset = new AtomicInteger(0);
        for (ColoredString[] coloredStrings : message) {
            int y = (int) (offset.getAcquire() * AreteClient.fontManager.getFontHeight(AreteClient.fontManager.selectedFont));
            if (ordering.getValue().equalsIgnoreCase("up")) y *= -1;
            int finalY = y;
            NVGContext.render(nvg -> {
                AreteClient.fontManager.renderTextMultiColor(context.getContext(), (int) getXSetting().getValue().floatValue(), (int) (getYSetting().getValue().floatValue() - finalY), true, getTextOrdering(this), coloredStrings);
            });
            offset.getAndIncrement();

        }
        height = (int) (message.size() * AreteClient.fontManager.getFontHeight(AreteClient.fontManager.selectedFont));
        if (ordering.getValue().equalsIgnoreCase("down")) height *= -1;

    }

    public void sort() {
        //if (ordering.getValue().equalsIgnoreCase("up"))
        message.sort(new ColoredStringComparator().reversed());
        //else message.sort(new ColoredStringComparator().reversed());
    }

    public void doOrdering() {
        if (!autoOrder.getValue()) return;
        if (getYSetting().getValue() < mc.getWindow().getScaledHeight() / 2) ordering.setValue("Up");
        else ordering.setValue("Down");
    }

    @Override
    public int[] hitBox() {
        return new int[]{(int) width, 10};
    }
}
