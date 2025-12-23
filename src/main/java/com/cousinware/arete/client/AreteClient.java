package com.cousinware.arete.client;

import com.cousinware.arete.managers.*;
import com.cousinware.arete.module.Client.NewClickGui;
import com.cousinware.arete.utils.file.ConfigFolder;
import com.cousinware.arete.utils.guis.clickgui.newgui.NewGui;
import com.cousinware.arete.utils.guis.clickgui.newgui.NewHudGui;
import com.cousinware.arete.utils.texture.Capes;
import com.google.common.eventbus.EventBus;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.Timer;

public class AreteClient implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("Arete");


    public static String RELEASE = "Public Build";
    public static String VERSION = "v0.1.0";
    public static String CLIENTNAME = "Arete";

    public static EventBus eventBus;
    public static String title = MinecraftClient.getInstance().getWindowTitle();

    static ConfigFolder configFolder;
    public static OS os;
    public static Capes capes;
    public static Render3DManager render3DManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static SettingManager settingManager;
    public static ModuleManager moduleManager;
    public static FontManager fontManager;

    public static NewGui newGui;
    public static NewHudGui newHudGui;
    public static KeybindManager keyboardManager;
    public static final TextRenderer[] tr = new TextRenderer[1];
    public static GameManager gameManager;
    public static AssetManager assetManager;
    public static BaritoneManager baritoneManager;
    public static RotationManager rotationManager;
    public static PacketManager packetManager;

    public static boolean isGameLaunched = false;
    String osName;

    public static Timer timer = new Timer(AreteClient.class.getSimpleName());


    public static Identifier getFileLocation(String path) {
        return Identifier.of("arete", path);
    }



    @Override
    public void onInitialize() {
        osCheck();

        eventBus = new EventBus("AreteEventBus");
        baritoneManager = new BaritoneManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        settingManager = new SettingManager();
        rotationManager = new RotationManager();
        moduleManager = new ModuleManager();
        render3DManager = new Render3DManager();
        fontManager = new FontManager();
        keyboardManager = new KeybindManager();
        gameManager = new GameManager();
        packetManager = new PacketManager();
        try {
            AreteClient.configFolder = new ConfigFolder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initClickGuis();



    }


    public void initClickGuis() {
        newGui = new NewGui();
        newHudGui = new NewHudGui();


    }


    public static Color getClientColor() {
        return new Color(NewClickGui.rMainColor.getValue(), NewClickGui.gMainColor.getValue(), NewClickGui.bMainColor.getValue(), NewClickGui.aMainColor.getValue());
    }

    public void osCheck() {
        osName = System.getProperty("os.name");
        if (osName.charAt(0) == 'w' || osName.charAt(0) == 'W') {
            // for some reason the main mc thread runs Headless as true
            //due to this some things in java are limited ie adding something to clipboard or doing our login stuff
            os = OS.WINDOWS;
            //System.setProperty("java.awt.headless", "false");

        } else if (osName.charAt(0) == 'm' || osName.charAt(0) == 'M') {
            os = OS.MAC;
        } else {
            os = OS.LINUX;
        }
    }

    public enum OS {
        WINDOWS,
        MAC,
        LINUX

    }

}

