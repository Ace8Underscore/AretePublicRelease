package com.cousinware.arete.module.misc;

import com.cousinware.arete.events.event.DisconnectEvent;
import com.cousinware.arete.module.Module;
import com.google.common.eventbus.Subscribe;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * @author Ace________/Ace_#1233
 */

public class LogoutCoords extends Module {

    public LogoutCoords() {
        super("LogoutCoords", Category.Misc, 14363678, "Saves your coords to the clipboard when logging out of a server");
    }

    @Subscribe
    public void onPlayerLeaveEvent(DisconnectEvent event) {
        if (!mc.isInSingleplayer()) {

                int x = (int) mc.player.getX();
                int y = (int) mc.player.getY();
                int z = (int) mc.player.getZ();
                String coords = "Logout Coords: X:" + x + " Y:" + y + " Z:" + z;

                StringSelection data = new StringSelection(coords);
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                cb.setContents(data, data);


        }
    }
}