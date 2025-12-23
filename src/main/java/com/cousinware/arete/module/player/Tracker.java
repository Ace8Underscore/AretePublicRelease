package com.cousinware.arete.module.player;

import com.cousinware.arete.command.Command;
import com.cousinware.arete.events.event.PlayerJoinServerEvent;
import com.cousinware.arete.events.event.PlayerLeaveServerEvent;
import com.cousinware.arete.events.event.RecieveMessageEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tracker extends Module {

    public static List<String> trackingNames = new CopyOnWriteArrayList<>();
    public static ModeSetting tabDisplay = new ModeSetting();

    public Tracker() {
        super("Tracker", Category.Player, -1, "Use Tracker Command to add or remove players from the tracking list");
        tabDisplay.setName("TabDisplay").setModes("Normal", "Minimal").setValue("Normal").build(this);
    }

    @Subscribe
    public void playerJoinWorld(PlayerJoinServerEvent event) {
        if (trackingNames.contains(event.getName())) {
            sendMessage(event.getName() + " Joined The Game");
        }
    }

    @Subscribe
    public void playerLeaveWorld(PlayerLeaveServerEvent event) {
        if (trackingNames.contains(event.getName())) {
            sendMessage(event.getName() + " Left The Game");
        }
    }

    @Subscribe
    public void chatMessage(RecieveMessageEvent event) {
        String sender = getSender(event.getText().getString());
        if (trackingNames.contains(sender)) {
            MutableText prefix = Text.literal(Formatting.RED + "[Tracker] -> ");
            MutableText finText = prefix.append(event.getText().copy());
            event.setMessage(finText);

        }

    }

    public static String getSender(String s) {

        try {
            String sender;
            sender = s.split("<")[1];
            return sender.split(">")[0];
        } catch (Exception e) {

        }
        return s;
    }


    private void sendMessage(String message) {
        Command.sendClientSideMessage(Formatting.RED + "[Tracker] -> " + message, false);
    }
}
