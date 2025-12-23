package com.cousinware.arete.managers;

import com.cousinware.arete.command.Command;
import com.cousinware.arete.command.commands.*;
import net.minecraft.util.Formatting;

import java.util.ArrayList;

public class CommandManager {

    ArrayList<Command> commands;

    public CommandManager() {
        commands = new ArrayList<>();
        addCommand();
    }

    public String getPrefix() {
        return Command.getPrefix();
    }

    public void callCommand(String text){
        //gets command without data behind and without prefix
        String commandCalled = text.split(" ")[0].substring(1);
        String[] commandArgs = text.split(" ");

        for (Command command : commands) {
            for (String s : command.commandCallName()) {
                if (s.equalsIgnoreCase(commandCalled))
                    try {
                        command.onCommand(commandArgs);
                        return;
                    } catch (Exception e) {
                        Command.sendClientSideMessage(Formatting.RED + "Command Used Incorrectly! Use it Like -> " + command.commandUsage());
                        return;
                    }
            }
        }
        //if were here the command was not called
        Command.sendClientSideMessage("No command for " + text);
    }

    public void addCommand() {
        commands.add(new PrefixCommand());
        commands.add(new HelpCommand());
        commands.add(new CommandsCommand());
        commands.add(new FriendCommand());
        commands.add(new AllFriendsCommand());
        commands.add(new ToggleCommand());
        commands.add(new BindCommand());
        commands.add(new FontCommand());
        commands.add(new AllFontsCommand());
        commands.add(new SettingsCommand());
        commands.add(new AllMembers());
    }

    public ArrayList<Command> getCommands() {
        return this.commands;
    }

    public ArrayList<String> getCommandNames() {
        ArrayList<String> commandNames = new ArrayList<>();
        for (int i = 0; i < commands.size(); i++) {
            commandNames.add(commands.get(i).commandCallName()[0]);
        }
        return commandNames;
    }

    public ArrayList<String> getCommandByStart(String string) {
        ArrayList<String> commandNames = new ArrayList<>();
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).commandCallName()[0].toLowerCase().startsWith(string.toLowerCase())) {
                commandNames.add(commands.get(i).commandCallName()[0]);
            }
        }
        return commandNames;
    }

    public Command getCommandByName(String name) {
        for (Command command : commands) {
            for (String s : command.commandCallName()) {
                if (s.equalsIgnoreCase(name)) return command;
            }
        }
        return null;
    }
}
