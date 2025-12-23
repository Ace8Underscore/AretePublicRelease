package com.cousinware.arete.command.commands;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.jetbrains.annotations.NotNull;

public class CommandsCommand extends Command {

    @Override
    public void onCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(commandCallName()).executes(context -> {

            for (Command command : AreteClient.commandManager.getCommands()) {
                Command.sendClientSideMessage(command.commandCallName() + " - " + command.commandHelpMessage(), false);
            }
            return 1;
        }));
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
    public String commandCallName() {
        return "commands";
    }
}
