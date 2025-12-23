package com.cousinware.arete.command.commands;

import com.cousinware.arete.command.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends Command {

    @Override
    public void onCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("bind").executes(context -> {

            Command.sendClientSideMessage("Use " + getPrefix() + "commands to see all of the clients commands", true);
            return 1;
        }));
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
    public String commandCallName() {
        return "Help";
    }
}
