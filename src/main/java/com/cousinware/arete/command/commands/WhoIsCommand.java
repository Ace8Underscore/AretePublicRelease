package com.cousinware.arete.command.commands;

import com.cousinware.arete.command.Command;
import com.cousinware.arete.utils.server.API2b2t;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.PlayerListEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class WhoIsCommand extends Command {

    @Override
    public void onCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(commandCallName())
                .executes(context -> {
                    sendClientSideMessage(commandUsage(), true);
                    return 1;
                })
                .then(ClientCommandManager.argument("player", StringArgumentType.string())
                        .suggests(suggestPlayer())
                        .executes(context -> {
                            lookUp(context);
                            return 1;
                        })
                )

        );
    }

    public int lookUp(CommandContext<FabricClientCommandSource> context) {
        String[] args = context.getInput().split(" ");
        String name = args[1];
        API2b2t.request("https://api.2b2t.vc/stats/player?playerName=" + name, true);
        //Command.sendClientSideMessage(name);
        return 2;
    }

    private SuggestionProvider<FabricClientCommandSource> suggestPlayer() {
        return (context, builder) -> {

            List<String> onlinePlayers = new CopyOnWriteArrayList<>();
            String data;
            if (context.getInput().split(" ").length > 1) data = context.getInput().split(" ")[1];
            else data = "";

            mc.getNetworkHandler().getPlayerList().stream().map(PlayerListEntry::getProfile).map(GameProfile::getName).forEach(playerName -> onlinePlayers.add(String.valueOf(playerName)));
            onlinePlayers.stream().filter(name -> name.toLowerCase().startsWith(data.toLowerCase())).forEach(builder::suggest);
            return CompletableFuture.supplyAsync(builder::build);
        };
    }

    @Override
    public String commandHelpMessage() {
        return "Check to See a 2b2t Players Stats";
    }

    @Override
    public @NotNull String commandUsage() {
        return "WhoIs (PlayerName)";
    }

    @Override
    public String commandCallName() {
        return "WhoIs";
    }
}
