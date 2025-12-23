package com.cousinware.arete.command.commands;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.module.Module;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ToggleCommand extends Command {
    @Override
    public void onCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(commandCallName())
                .executes(context -> {
                    sendClientSideMessage(commandUsage(), true);
                    return 1;
                })
                .then(ClientCommandManager.argument("name", StringArgumentType.string())
                        .suggests(suggestModules())
                        .executes(context -> toggleModule(StringArgumentType.getString(context, "name")))
                )
        );
    }

    private int toggleModule(String name) {
        Module module = AreteClient.moduleManager.getModuleByName(name);
        if (module == null) {
            Command.sendClientSideMessage(name + " Is not a valid Module!", true);
            return 0;
        } else {
            module.toggle();
            Command.sendClientSideMessage(module.isEnabled() ? module.getName() + " Was Enabled!" : module.getName() + " Was Disabled!", true);
            return 1;
        }
    }


    private SuggestionProvider<FabricClientCommandSource> suggestModules() {
        return (context, builder) -> {
            String input = builder.getRemaining().toLowerCase();
            AreteClient.moduleManager.getModules().stream()
                    .map(Module::getName)
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .forEach(builder::suggest);
            return CompletableFuture.supplyAsync(builder::build);
        };
    }


    @Override
    public String commandHelpMessage() {
        return "Turns on modules in the client";
    }

    @Override
    public @NotNull String commandUsage() {
        return getPrefix() + "Toggle (Module)";
    }

    @Override
    public String commandCallName() {
        return "Toggle";
    }
}
