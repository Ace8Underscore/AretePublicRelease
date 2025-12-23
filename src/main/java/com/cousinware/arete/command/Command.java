package com.cousinware.arete.command;

import com.cousinware.arete.client.AreteClient;
import com.mojang.brigadier.CommandDispatcher;
import lombok.Getter;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public abstract class Command {

    protected static final MinecraftClient mc = MinecraftClient.getInstance();


    @Getter
    public static String prefix = ",";

    public void setPrefix(String prefix) {
        Command.prefix = prefix;
    }

    public static Text pastCommand;

    public static void sendClientSideMessage(String message, boolean disappear) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null || mc.player == null) return;
        if (disappear && pastCommand != null) {
            removeMessage(pastCommand);

        }


        try {
            Text text = Text.of(clientDecoPrefix() + Formatting.GRAY + message);
            mc.inGameHud.getChatHud().addMessage(Text.of(clientDecoPrefix() + Formatting.GRAY + message));
            if (disappear) {
                pastCommand = text;
            }
        } catch (Exception e) {
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) System.out.println(message);
        }

    }

    public static void removeMessage(Text text) {
        int index = 0;
        for (int i = 0; i < mc.inGameHud.getChatHud().getMessageHistory().size(); i++) {
            if (mc.inGameHud.getChatHud().getMessageHistory().get(i).equalsIgnoreCase(String.valueOf(text))) {
                System.out.println("removing");
                index = i;

            }
        }
    }


    public static String clientDecoPrefix() {
        //TODO SYNC WITH CLIENT COLOR
        Color c = new Color(47, 50, 159, 255).brighter().brighter();
        return Formatting.LIGHT_PURPLE + "[" + Formatting.GRAY + AreteClient.CLIENTNAME + Formatting.LIGHT_PURPLE + "] ";
    }

    //executes when command is called
    public abstract void onCommand(CommandDispatcher<FabricClientCommandSource> dispatcher);


    //commands help message on what command does
    public abstract String commandHelpMessage();

    //commands help message on how to use command
    @NotNull
    public abstract String commandUsage();

    //phrases players can type to access the command
    public abstract String commandCallName();


}
