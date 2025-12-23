package com.cousinware.arete.command.commands;

import com.cousinware.arete.command.Command;
import org.jetbrains.annotations.NotNull;

public class PrefixCommand extends Command {
    @Override
    public void onCommand(String[] args) throws Exception {
        System.out.println(args[0]);
        if (args[1].equalsIgnoreCase(" ")) return;
        setPrefix(args[1]);

        sendClientSideMessage("Prefix was set to " + getPrefix());
    }

    @Override
    public String commandHelpMessage() {
        return "Changes the client command prefix";
    }

    @Override
    public @NotNull String commandUsage() {
        return getPrefix() + "Prefix (NewPrefix)";
    }

    @Override
    public String[] commandCallName() {
        return new String[] {"Prefix"};
    }
}
