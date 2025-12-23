package com.cousinware.arete.command.commands;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.managers.FriendManager;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

public class FriendCommand extends Command {
    @Override
    public void onCommand(String[] args) throws Exception {

        String addordel = args[1];
        String name = args[2];
        FriendManager friendManager = AreteClient.friendManager;

        if (addordel.equalsIgnoreCase("add")) {
            boolean addFriend = friendManager.addFriend(name);
            if (addFriend) Command.sendClientSideMessage(Formatting.GREEN + name + Formatting.GRAY + " has been friended");
            else Command.sendClientSideMessage(Formatting.AQUA + name + Formatting.GRAY + " is already a friend");

        } else if (addordel.equalsIgnoreCase("del")) {
            boolean delFriend = friendManager.removeFriend(name);
            if (delFriend) Command.sendClientSideMessage(Formatting.RED + name  + Formatting.GRAY + " Was removed from the friends list");
            else Command.sendClientSideMessage(Formatting.AQUA + name + Formatting.GRAY +" is not a friend");

        }

    }

    @Override
    public String commandHelpMessage() {
        return "Adds Friends to Your FriendList";
    }

    @Override
    public @NotNull String commandUsage() {
        return getPrefix() + "Friend (add/del/blank) (name)";
    }

    @Override
    public String[] commandCallName() {
        return new String[] {"Friend", "Friends"};
    }
}
