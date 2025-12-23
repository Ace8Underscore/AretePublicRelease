package com.cousinware.arete.command;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.utils.Member;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public abstract class Command {


    static String prefix = ",";

    public void setPrefix(String prefix) {
        Command.prefix = prefix;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void sendClientSideMessage(String message) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null || mc.player == null) return;

        try {
            mc.inGameHud.getChatHud().addMessage(Text.of(clientDecoPrefix() + Formatting.GRAY + message));
        }catch (Exception e) {
            //huh?
        }

    }

    public static String clientDecoPrefix() {
        //TODO SYNC WITH CLIENT COLOR
        Color c =  new Color(47, 50, 159, 255).brighter().brighter();
        return Formatting.LIGHT_PURPLE + "[" + Formatting.GRAY + AreteClient.CLIENTNAME + Formatting.LIGHT_PURPLE + "] ";
    }

    public static String empPrefix(Member member) {
        if (member.getRanks().contains(Member.Rank.Ally)) return Formatting.GOLD + "[★]";
        return Formatting.RED + "[Ω]";
    }

    //executes when command is called
    public abstract void onCommand(String[] args) throws Exception;

    //commands help message on what command does
    public abstract String commandHelpMessage();

    //commands help message on how to use command
    //NonNull to avoide messing up our chatsuggestor in ChatManager.class
    @NotNull
    public abstract String commandUsage();

    //phrases players can type to access the command
    public abstract String[] commandCallName();
}
