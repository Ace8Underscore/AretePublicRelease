package com.cousinware.arete.command.commands;

import com.cousinware.arete.command.Command;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends Command {
    @Override
    public void onCommand(String[] args) throws Exception {
        Command.sendClientSideMessage("Use " + getPrefix() + "commands to see all of the clients commands");
    }

    @Override
    public String commandHelpMessage() {
        return "Helps you with using the client";
    }

    @Override
    public @NotNull String commandUsage() {
        return "";
    }

    @Override
    public String[] commandCallName() {
        return new String[] {"Help"};
    }
}
