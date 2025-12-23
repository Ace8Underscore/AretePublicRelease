package com.cousinware.arete.command.commands;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class FontCommand extends Command {

    @Override
    public String commandHelpMessage() {
        return "Use this command to change client font use " + getPrefix() + "getfonts to get a list of fonts";
    }

    @Override
    public @NotNull String commandUsage() {
        return "Font (fontname)";
    }

    @Override
    public String commandCallName() {
        return "Font";
    }


    @Override
    public void onCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(commandCallName())
                .executes(context -> {
                    sendClientSideMessage(commandUsage() + " Current Font -> " + AreteClient.fontManager.selectedFont, true);
                    return 1;
                })
                .then(ClientCommandManager.argument("name", StringArgumentType.string())
                        .suggests(suggestFont())

                        .executes(context -> changeFont(StringArgumentType.getString(context, "name")))
                )
        );
    }

    private int changeFont(String name) {
        //Command.sendClientSideMessage("Font change");
        AreteClient.fontManager.setAltFont(name);
        AreteClient.fontManager.setSelectedFont(name);
        return 0;
    }

    private SuggestionProvider<FabricClientCommandSource> suggestFont() {
        return (context, builder) -> {
            String input = builder.getRemaining().toLowerCase();
            AreteClient.fontManager.getLoadedFontNames().stream()
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .filter(name -> !name.contains(" "))
                    .forEach(builder::suggest);
            return CompletableFuture.supplyAsync(builder::build);
        };
    }
}
