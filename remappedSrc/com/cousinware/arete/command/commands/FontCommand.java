package com.cousinware.arete.command.commands;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import org.jetbrains.annotations.NotNull;

public class FontCommand extends Command {
    @Override
    public void onCommand(String[] args) throws Exception {
            AreteClient.fontManager.setSelectedFont(args[1]);

        //Command.sendClientSideMessage("Font set to " + args[1]);
    }

    @Override
    public String commandHelpMessage() {
        return "Use this command to change client font use " + getPrefix() + "getfonts to get a list of fonts";
    }

    @Override
    public @NotNull String commandUsage() {
        return "Font (fontname)";
    }

    @Override
    public String[] commandCallName() {
        return new String[] {"Font"};
    }
}
