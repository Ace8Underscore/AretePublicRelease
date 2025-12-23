package com.cousinware.arete.managers;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.GameStartEvent;
import com.cousinware.arete.module.Client.Core;
import com.cousinware.arete.utils.rendering.nvg.NVGContext;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.system.MemoryUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class GameManager {

    public GameManager() {
        AreteClient.eventBus.register(this);
    }

    @Subscribe
    public void gameStart(GameStartEvent event) {
        AreteClient.isGameLaunched = true;

    }

    public void loadFonts() {

        String defaultFont = "";
        if (AreteClient.os.equals(AreteClient.OS.WINDOWS)) {
            loadWindowsFonts();
            String fontName = "verdana";
            FontManager.font = NanoVG.nvgCreateFont(NVGContext.context, fontName, "C:\\Windows\\Fonts\\verdana.ttf");
            AreteClient.fontManager.setSelectedFont(fontName);
            defaultFont = fontName;
        } else if (AreteClient.os.equals(AreteClient.OS.MAC)) {
            loadMacFonts();
            String fontName = "keyboard";
            FontManager.font = NanoVG.nvgCreateFont(NVGContext.context, fontName, "/System/Library/Fonts/Geneva.ttf");
            AreteClient.fontManager.setSelectedFont(fontName);
            defaultFont = fontName;

        } else if (AreteClient.os.equals(AreteClient.OS.LINUX)) {
            loadLinuxFonts();
            String fontName = "verdana";
            FontManager.font = NanoVG.nvgCreateFont(NVGContext.context, "verdana", "/home/" + System.getProperty("user.name") + "/Documents/fonts/Poppins-Medium.ttf");
            AreteClient.fontManager.setSelectedFont(fontName);
            defaultFont = fontName;
        }
        loadFontFromResources("/assets/arete/fonts/jetbrainsmono.ttf", "jetbrainsmono");
        //Minecraft font for when people dont want custom font. AKA fuck minecrafts renderer
        loadFontFromResources("/assets/arete/fonts/minecraft.ttf", "minecraft");
        // if font is not found we load the default which is included in the jar
        if (Core.customFont.getValue() && !AreteClient.fontManager.getLoadedFontNames().contains(defaultFont) && !AreteClient.fontManager.altFont.isBlank() && AreteClient.fontManager.altFont.length() < 2)
            AreteClient.fontManager.setSelectedFont("jetbrainsmono");
        else if (Core.customFont.getValue() && AreteClient.fontManager.altFont.length() > 2) {
            AreteClient.fontManager.setSelectedFont(AreteClient.fontManager.altFont);
        } else if (!Core.customFont.getValue()) {
            AreteClient.fontManager.setSelectedFont("minecraft");
        }

    }


    public void loadWindowsFonts() {
        try {
            File directoryPath = new File("C:\\Windows\\Fonts\\");
            //List of all files and directories
            File[] filesList = directoryPath.listFiles();
            if (filesList == null || filesList.length == 0) throw new RuntimeException("No Fonts Found: Windows User!");

            for (File file : filesList) {
                // if its a ttf and doesnt end with __ or start with samsung we will use it as a useable font
                if (!file.getName().substring(file.getName().length() - 3).equalsIgnoreCase("ttf") || file.getName().toLowerCase().startsWith("samsung") || file.getName().toLowerCase().endsWith("___.ttf"))
                    continue;

                AreteClient.fontManager.addFont(file.getAbsolutePath(), file.getName().toLowerCase());

            }
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    public void loadMacFonts() {
        try {
            File directoryPath = new File("/System/Library/Fonts/");
            //List of all files and directories
            File[] filesList = directoryPath.listFiles();
            if (filesList == null || filesList.length == 0) throw new RuntimeException("No Fonts Found: Mac User!");

            for (File file : filesList) {
                // if its a ttf and doesnt end with __ or start with samsung we will use it as a useable font
                AreteClient.fontManager.addFont(file.getAbsolutePath(), file.getName().toLowerCase());

            }
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    public void loadLinuxFonts() {
        try {
            File directoryPath = new File("/home/" + System.getProperty("user.name") + "/Documents/fonts/");
            //List of all files and directories
            File[] filesList = directoryPath.listFiles();
            if (filesList == null || filesList.length == 0) throw new RuntimeException("No Fonts Found: Linux User!");

            for (File file : filesList) {
                // if its a ttf and doesnt end with __ or start with samsung we will use it as a useable font
                AreteClient.fontManager.addFont(file.getAbsolutePath(), file.getName().toLowerCase());

            }
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    public static void loadFontFromResources(String resourcePath, String fontName) {
        try (InputStream input = GameManager.class.getResourceAsStream(resourcePath)) {
            // Load font from the resources folder
            if (input == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            os.write(input.readAllBytes());
            byte[] fontBytes = os.toByteArray();
            //Also MemoryUtil should be used for created byte buffers NanoVG "REQUIRES" it
            ByteBuffer byteBuffer = MemoryUtil.memAlloc(fontBytes.length);
            byteBuffer.put(fontBytes);
            //why do we flip i have no idea someone on stack over flow said to
            byteBuffer.flip();


            // Load font with NanoVG
            AreteClient.fontManager.addFont(byteBuffer, fontName);


        } catch (IOException e) {
            throw new RuntimeException("Error loading font from resources", e);
        }
    }

}
