package com.cousinware.arete.command.commands;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.*;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SettingsCommand extends Command {

    //TODO settings within BoolSettingContainer do not populate

    @Override
    public void onCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal(commandCallName())
                .executes(context -> {
                    sendClientSideMessage(commandUsage(), true);
                    return 1;
                })
                .then(ClientCommandManager.argument("module", StringArgumentType.string())
                        .suggests(suggestModules())
                        .then(ClientCommandManager.argument("setting", StringArgumentType.string())
                                .suggests(suggestSettings())
                                .then(ClientCommandManager.argument("value", StringArgumentType.greedyString())
                                        .suggests(suggestValue())
                                        .executes(context -> {
                                            modifySetting(context);
                                            return 1;
                                        })
                                )

                        )));
    }

    public SuggestionProvider<FabricClientCommandSource> suggestValue() {
        return (context, builder) -> {
            List<String> values = getValues(context);
            values.forEach(builder::suggest);
            return CompletableFuture.supplyAsync(builder::build);
        };
    }

    public ArrayList<String> getValues(CommandContext<?> context) {
        String[] args = context.getInput().split(" ");

        ArrayList<String> value = new ArrayList<>();
        try {
            Setting currentSetting = AreteClient.moduleManager.getModuleByName(args[1]).getSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(args[2])).findFirst().get();
            switch (currentSetting) {
                case DoubleSetting doubleSetting -> value.add("Value(Double/Float)");
                case IntSetting intSetting -> value.add("Value(WholeNumber)");
                case BoolSetting booleanSetting -> {
                    value.add("True");
                    value.add("False");
                }
                case ModeSetting modeSetting -> {
                    for (int i = 0; i < modeSetting.getModes().size(); i++) {
                        value.add(((ModeSetting) currentSetting).getModes().get(i));
                    }
                }
                case StringSetting setting -> value.add("Message");
                default -> {
                }
            }

        } catch (Exception ignored) {

        }
        return value;
    }


    private void modifySetting(CommandContext<?> context) {
        String[] args = context.getInput().split(" ");
        Setting currentSetting = AreteClient.moduleManager.getModuleByName(args[1]).getSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(args[2])).findFirst().get();

        try {
            switch (currentSetting) {
                case DoubleSetting doubleSetting -> doubleSetting.setValue(Double.parseDouble(args[3]));
                case IntSetting intSetting -> intSetting.setValue(Integer.parseInt(args[3]));
                case BoolSetting booleanSetting -> booleanSetting.setValue(Boolean.parseBoolean(args[3]));
                case ModeSetting modeSetting -> modeSetting.setValue(args[3]);
                case StringSetting customTextSetting -> {
                    StringBuilder text = new StringBuilder();
                    for (int i = 3; i < args.length; i++) {
                        text.append(args[i]).append(" ");
                    }
                    customTextSetting.setValue(text.toString());
                    Command.sendClientSideMessage(args[1] + " " + args[2] + " Was set to " + text, true);
                    return;
                }
                default -> {
                }
            }

            Command.sendClientSideMessage(args[1] + " " + args[2] + " Was set to " + args[3], true);
        } catch (Exception e) {
            Command.sendClientSideMessage("Failed to change " + args[1] + " " + args[2] + " to " + args[3], true);
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

    private SuggestionProvider<FabricClientCommandSource> suggestSettings() {
        return (context, builder) -> {
            String input = builder.getRemaining().toLowerCase();
            AreteClient.moduleManager.getModuleByName(context.getInput().split(" ")[1]).getSettings().stream()
                    .map(Setting::getName)
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .forEach(builder::suggest);
            return CompletableFuture.supplyAsync(builder::build);
        };
    }

    @Override
    public String commandHelpMessage() {
        return "Used to change settings for a module";
    }

    @Override
    public @NotNull String commandUsage() {
        return "Settings (Module) (Setting) (Value)";
    }

    @Override
    public String commandCallName() {
        return "Setting";
    }
}
