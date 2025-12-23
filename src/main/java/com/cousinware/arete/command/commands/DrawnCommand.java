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

public class DrawnCommand extends Command {
    @Override
    public void onCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(commandCallName())
                .executes(context -> {
                    sendClientSideMessage(commandUsage(), true);
                    return 1;
                })
                .then(ClientCommandManager.argument("name", StringArgumentType.string())
                        .suggests(suggestModules())
                        .executes(context -> drawnModule(StringArgumentType.getString(context, "name")))
                )
        );
    }

    private int drawnModule(String name) {
        com.cousinware.arete.module.Module module = AreteClient.moduleManager.getModuleByName(name);
        if (module == null) {
            Command.sendClientSideMessage(name + " Is not a valid Module!", true);
            return 0;
        } else {
            module.setDrawn(!module.isDrawn());
            Command.sendClientSideMessage(module.getName() + "Has been " + (module.isDrawn() ? "Drawn" : "UnDrawn!"), true);
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
        return "Removes modules from showing on the array list";
    }

    @Override
    public @NotNull String commandUsage() {
        return "Drawn (module)";
    }

    @Override
    public String commandCallName() {
        return "Drawn";
    }
}
