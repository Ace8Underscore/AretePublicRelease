package com.cousinware.arete.command.commands;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import org.jetbrains.annotations.NotNull;

public class CommandsCommand extends Command {
    @Override
    public void onCommand(String[] args) throws Exception {

        for (Command command :AreteClient.commandManager.getCommands()) {
            Command.sendClientSideMessage(command.commandCallName()[0] + " - " + command.commandHelpMessage());
        }

    }

    @Override
    public String commandHelpMessage() {
        return "Shows all available commands";
    }

    @Override
    public @NotNull String commandUsage() {
        return "";
    }

    @Override
    public String[] commandCallName() {
        return new String[] {"Commands"};
    }
}
