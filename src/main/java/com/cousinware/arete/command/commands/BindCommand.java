package com.cousinware.arete.command.commands;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.module.Module;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class BindCommand extends Command {

    public void onCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(commandCallName()).executes(context -> {
                    Command.sendClientSideMessage(commandUsage(), true);
                    return 1;
                })
                .then(ClientCommandManager.argument("name", StringArgumentType.string())
                        .suggests(suggestModules())
                        .then(ClientCommandManager.argument("key", StringArgumentType.string())
                                .suggests(suggestBind())
                                .executes(context -> bind(context, StringArgumentType.getString(context, "key")))
                        )));
    }


    private int bind(CommandContext<?> context, String key) {
        String[] data = context.getInput().split(" ");
        Module module = AreteClient.moduleManager.getModuleByName(data[1]);
        if (key.equalsIgnoreCase("none")) {
            module.setKeybind(-9991);
            Command.sendClientSideMessage(module.getName() + " Was UnBounded", true);
        } else {
            module.setKeybind(InputUtil.fromTranslationKey("key.keyboard." + key.toLowerCase()).getCode());
            Command.sendClientSideMessage(module.getName() + " Was Binded To " + key, true);
        }
        return 1;
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

    private SuggestionProvider<FabricClientCommandSource> suggestBind() {
        return (context, builder) -> {
            String[] actions = {"Key", "None"};
            Arrays.stream(actions)
                    .forEach(builder::suggest);
            return CompletableFuture.supplyAsync(builder::build);
        };
    }

    @Override
    public String commandHelpMessage() {
        return "Binds modules to a certain key on the keyboard";
    }

    @Override
    public @NotNull String commandUsage() {
        return "Bind (Module) (Key)";
    }

    @Override
    public String commandCallName() {
        return "bind";
    }
}
