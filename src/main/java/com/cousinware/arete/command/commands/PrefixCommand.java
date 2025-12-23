package com.cousinware.arete.command.commands;

import com.cousinware.arete.command.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class PrefixCommand extends Command {

    @Override
    public void onCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(commandCallName())
                .executes(context -> {
                    sendClientSideMessage(commandUsage(), true);
                    return 1;
                })
                .then(ClientCommandManager.argument("name", StringArgumentType.greedyString())
                        .suggests(suggestAction())
                        .executes(context -> setPrefix1(StringArgumentType.getString(context, "name")))
                )
        );
    }

    private SuggestionProvider<FabricClientCommandSource> suggestAction() {
        return (context, builder) -> {
            String[] actions = {"prefix"};

            Arrays.stream(actions)
                    .forEach(builder::suggest);
            return CompletableFuture.supplyAsync(builder::build);
        };
    }

    public int setPrefix1(String prefix) {

        Command.prefix = prefix;
        Command.sendClientSideMessage("Prefix has been set to '" + prefix + "'", true);
        return 1;
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
    public String commandCallName() {
        return "Prefix";
    }
}
