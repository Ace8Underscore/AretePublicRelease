package com.cousinware.arete.managers;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.events.event.PlayerSendMessageEvent;
import com.cousinware.arete.events.event.RenderChatEvent;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.util.ArrayList;

public class ChatManager {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public ChatManager() {
        AreteClient.eventBus.register(this);
    }

    Command com = null;

    @Subscribe
    public boolean isCommandSent(PlayerSendMessageEvent event) {
        if (event.getText().startsWith(AreteClient.commandManager.getPrefix())) {

            AreteClient.commandManager.callCommand(event.getText());
            if (event.isAddToHistory()) MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(event.getText());
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    @Subscribe
    public void chatSuggestion(RenderChatEvent event) {
        if (event.getText().startsWith(Command.getPrefix())) {
            //draw outline
            drawOutline(event);

            if (event.getText().length() == Command.getPrefix().length()) {
                suggest(AreteClient.commandManager.getCommandNames(), event);
                return;
            }
            try {
                suggest(AreteClient.commandManager.getCommandByStart(event.getText().substring(1)), event);

            } catch (Exception e) {
                //failed
            }

        }

    }

    public void drawOutline(RenderChatEvent event) {
        //Pushing
        event.getContext().getMatrices().push();

        int color = new Color(47, 50, 159, 255).brighter().brighter().getRGB();
        //Rendering the outline for our input box when prefix is detected!
        event.getContext().fill(2, mc.currentScreen.height - 15, mc.currentScreen.width - 2, mc.currentScreen.height - 14, color);
        event.getContext().fill(2, mc.currentScreen.height - 2, mc.currentScreen.width - 2, mc.currentScreen.height - 1, color);
        event.getContext().drawVerticalLine(1, mc.currentScreen.height - 15, mc.currentScreen.height -2, color);
        event.getContext().drawVerticalLine(mc.currentScreen.width - 2, mc.currentScreen.height - 15, mc.currentScreen.height -2, color);

        //Poping
        event.getContext().getMatrices().pop();
    }

    public void suggest(ArrayList<String> suggestion, RenderChatEvent event) {
        //pushing so we dont mess up mc code :^)
        event.getContext().getMatrices().push();



        //suprisingly mc doesnt care to use screen width/height they use hard coded nums
        int height = mc.currentScreen.height - 2;
        int startingHeight = height - 28;
        int y = 0;
        int width = mc.textRenderer.getWidth(getLongestCommand(suggestion));

        //if  a command is fully typed than we will go ahead and get its help string and fix the width for out gray/black box
        //do i wanna even explain how this works no...
        if (suggestion.size() != 1 && !suggestion.isEmpty()) {
            com = null;
        }


        if (suggestion.isEmpty() && com != null) {

            String string = Formatting.GOLD + com.commandCallName()[0];

            //if we got the command info we add some stuff to the string
            string += Formatting.WHITE + com.commandUsage().substring(com.commandCallName()[0].length() + 1);

            event.getContext().drawText(mc.textRenderer, string,2 , startingHeight, new Color(255, 255, 255).getRGB(), true);

            width = mc.textRenderer.getWidth(com.commandUsage().substring(suggestion.size()));
            event.getContext().fill(1, startingHeight + 14, width + 4, startingHeight + ( -0) - 2, -805306368);
        }
        if (suggestion.size() == 1 && event.getText().substring(1).toLowerCase().contains(suggestion.get(0).toLowerCase()) && com == null) {

            com = AreteClient.commandManager.getCommandByName(event.getText().substring(1, suggestion.get(0).length() + 1));

            width = mc.textRenderer.getWidth(com.commandUsage().substring(suggestion.size()));

        }

        //this fills out a gray/black box for our text to go over
        if (suggestion.size() == 1 && com!= null && com.commandUsage().length() >= suggestion.size()) width = mc.textRenderer.getWidth(com.commandUsage().substring(suggestion.size()));


        if (!suggestion.isEmpty()) event.getContext().fill(1, startingHeight + 14, width + 4, startingHeight + ((suggestion.size() - 1) * -12) - 2, -805306368);


        for (String s : suggestion) {
            // below we highlight letters typed in via player

            String string = Formatting.GOLD + s.substring(0, event.getText().length() - 1) + Formatting.WHITE + s.substring(event.getText().length() - 1);

            //if we got the command info we add some stuff to the string
            if (com != null && com.commandUsage().length() > s.length()) string += com.commandUsage().substring(s.length() + 1);
            //draws the stuff
            event.getContext().drawText(mc.textRenderer, string,2 , startingHeight + y, new Color(255, 255, 255).getRGB(), true);
            // so they GO UP IF MORE
            y-= 12;
        }

        //good Pop
        event.getContext().getMatrices().pop();
    }

    public String getLongestCommand(ArrayList<String> strings) {
        //below we get the longest command length so we can then use it to correctly size out gray/black box that our commands go over
        int len = 0;
        String string = "";
        for (String s : strings) {
            if (s.length() > len) {
                len = s.length();
                string = s;

            }
        }
        return string;
    }
}
