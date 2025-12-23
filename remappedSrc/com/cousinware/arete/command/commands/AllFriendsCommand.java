package com.cousinware.arete.command.commands;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import org.jetbrains.annotations.NotNull;

public class AllFriendsCommand extends Command {
    @Override
    public void onCommand(String[] args) throws Exception {

        for (String s : AreteClient.friendManager.getFriends()) {
            Command.sendClientSideMessage(s);
        }

    }

    @Override
    public String commandHelpMessage() {
        return "Prints out the friend List";
    }

    @Override
    public @NotNull String commandUsage() {
        return "";
    }

    @Override
    public String[] commandCallName() {
        return new String[] {"AllFriends"};
    }
}
