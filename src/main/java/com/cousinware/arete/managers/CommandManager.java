package com.cousinware.arete.managers;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.command.commands.*;
import com.cousinware.arete.events.event.PlayerSendMessageEvent;
import com.cousinware.arete.utils.MinecraftInterface;
import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.ArrayList;

public class CommandManager implements MinecraftInterface {

    ArrayList<Command> commands;
    public static final CommandDispatcher<FabricClientCommandSource> DISPATCHER = new CommandDispatcher<>();
    public static final FabricClientCommandSource COMMAND_SOURCE = null;

    public CommandManager() {
        AreteClient.eventBus.register(this);
        commands = new ArrayList<>();
        addCommand();


        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            commands.forEach(command -> {
                command.onCommand(DISPATCHER);

            });
        });


    }

    @Subscribe
    public void chatMessageSent(PlayerSendMessageEvent event) {
        if (event.getText().startsWith(AreteClient.commandManager.getPrefix())) {
            try {
                CommandManager.DISPATCHER.execute(CommandManager.DISPATCHER.parse(event.getText().substring(AreteClient.commandManager.getPrefix().length()), CommandManager.COMMAND_SOURCE));
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            } finally {
                mc.inGameHud.getChatHud().addToMessageHistory(event.getText());
                event.setCancelled(true);
            }
        }

    }


    public String getPrefix() {
        return Command.getPrefix();
    }


    public void addCommand() {
        commands.add(new PrefixCommand());
        commands.add(new HelpCommand());
        commands.add(new CommandsCommand());
        commands.add(new FriendCommand());
        commands.add(new AllFriendsCommand());
        commands.add(new ToggleCommand());
        commands.add(new BindCommand());
        commands.add(new FontCommand());
        commands.add(new SettingsCommand());
        commands.add(new ServerCommand());
        commands.add(new WhoIsCommand());
        commands.add(new DrawnCommand());
        commands.add(new TrackerCommand());

    }

    public ArrayList<Command> getCommands() {
        return this.commands;
    }


    public Command getCommandByName(String name) {
        for (Command command : commands) {
            if (command.commandCallName().equalsIgnoreCase(name)) return command;

        }
        return null;
    }
}
