package com.cousinware.arete.command.commands;

import com.cousinware.arete.command.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.jetbrains.annotations.NotNull;

public class ServerCommand extends Command {

    public static String commandString = "";

    @Override
    public void onCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {

    }


    @Override
    public String commandHelpMessage() {
        return "Magic powers to connect to the Emperium Servers woooooooooo";
    }

    @Override
    public @NotNull String commandUsage() {
        return "sv/svr (command)";
    }

    @Override
    public String commandCallName() {
        return "Sv";
    }
}
