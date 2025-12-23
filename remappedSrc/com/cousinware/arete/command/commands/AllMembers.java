package com.cousinware.arete.command.commands;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import org.jetbrains.annotations.NotNull;

public class AllMembers extends Command{

    @Override
    public void onCommand(String[] args) throws Exception {

        AreteClient.members.forEach(member -> Command.sendClientSideMessage(member.toString()));

    }

    @Override
    public String commandHelpMessage() {
        return "Shows All The Emperium Users!";
    }

    @Override
    public @NotNull String commandUsage() {
        return "";
    }

    @Override
    public String[] commandCallName() {
        return new String[]{"AllMembers"};
    }


}
