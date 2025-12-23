package com.cousinware.arete.command.commands;

import com.cousinware.arete.command.Command;
import com.cousinware.arete.module.player.Tracker;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.PlayerListEntry;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrackerCommand extends Command {
    @Override
    public void onCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(commandCallName())
                .executes(context -> {
                    sendClientSideMessage(commandUsage(), true);
                    return 1;
                })
                .then(ClientCommandManager.argument("name", StringArgumentType.string())
                        .suggests(suggestAction())
                        .then(ClientCommandManager.argument("player", StringArgumentType.string())
                                .suggests(suggestPlayer())
                                .executes(context -> {
                                    track(context);
                                    return 1;
                                })
                        )

                ));
    }


    public int track(CommandContext<FabricClientCommandSource> context) {
        String[] args = context.getInput().split(" ");


        if (args[1].equalsIgnoreCase("del")) {
            Command.sendClientSideMessage(args[2] + " Removed from the tracker", false);
            Tracker.trackingNames.remove(args[2]);
        } else if (args[1].equalsIgnoreCase("add")) {
            Command.sendClientSideMessage(args[2] + " Added to the tracker", false);
            Tracker.trackingNames.add(args[2]);
        }
        return 2;
    }

    private SuggestionProvider<FabricClientCommandSource> suggestAction() {
        return (context, builder) -> {
            String[] actions = {"add", "del"};

            Arrays.stream(actions)
                    .forEach(builder::suggest);
            return CompletableFuture.supplyAsync(builder::build);
        };
    }

    private SuggestionProvider<FabricClientCommandSource> suggestPlayer() {
        return (context, builder) -> {

            List<String> onlinePlayers = new CopyOnWriteArrayList<>();
            String data;
            if (context.getInput().split(" ").length > 2) data = context.getInput().split(" ")[2];
            else data = "";

            mc.getNetworkHandler().getPlayerList().stream().map(PlayerListEntry::getProfile).map(GameProfile::getName).forEach(playerName -> onlinePlayers.add(String.valueOf(playerName)));
            onlinePlayers.stream().filter(name -> name.toLowerCase().startsWith(data.toLowerCase())).forEach(builder::suggest);
            return CompletableFuture.supplyAsync(builder::build);
        };
    }


    @Override
    public String commandHelpMessage() {
        return "Adds Player To Track to Your TrackerList";
    }

    @Override
    public @NotNull String commandUsage() {
        return getPrefix() + "Tracker (add/del) (name)";
    }

    @Override
    public String commandCallName() {
        return "Tracker";
    }
}
