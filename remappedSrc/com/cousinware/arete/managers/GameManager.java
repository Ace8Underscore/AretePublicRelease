package com.cousinware.arete.managers;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.GameStartEvent;
import com.cousinware.arete.utils.texture.Capes;
import com.cousinware.arete.utils.rendering.font.nvg.NVGContext;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.nanovg.NanoVG;

import java.io.File;

public class GameManager {

    public GameManager() {
        AreteClient.eventBus.register(this);
    }

    @Subscribe
    public void loadFonts(GameStartEvent event) {
        if (AreteClient.os.equals(AreteClient.OS.WINDOWS)) loadWindowsFonts();
        FontManager.font = NanoVG.nvgCreateFont(NVGContext.context, "comicsans", "C:\\Windows\\Fonts\\OpenSans-Regular.ttf");
    }

    @Subscribe
    public void loadTextures(GameStartEvent event) {
        AreteClient.capes = new Capes();

    }

    public void loadWindowsFonts() {
        try {
            File directoryPath = new File("C:\\Windows\\Fonts\\");
            //List of all files and directories
            File[] filesList = directoryPath.listFiles();
            if (filesList == null || filesList.length == 0) throw new RuntimeException("No Fonts Found: Windows User!");

            for (File file : filesList) {
                // if its a ttf and doesnt end with __ or start with samsung we will use it as a useable font
                if (!file.getName().substring(file.getName().length() - 3).equalsIgnoreCase("ttf") || file.getName().toLowerCase().startsWith("samsung") || file.getName().toLowerCase().endsWith("___.ttf")) continue;

                AreteClient.fontManager.addFont(file.getAbsolutePath(), file.getName().toLowerCase());

            }
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

}
