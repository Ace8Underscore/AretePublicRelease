package com.cousinware.arete.managers;

import com.cousinware.arete.command.Command;

import java.util.ArrayList;

public class FriendManager {

    ArrayList<String> friends;

    public FriendManager() {
        friends = new ArrayList<>();
    }

    public boolean addFriend(String name) {
        boolean friend = false;
        for (String names : friends) {
            if (names.equalsIgnoreCase(name)) {
                friend = true;
                break;
            }
        }
        if (!friend) {
            friends.add(name);
            Command.sendClientSideMessage(name + "Has been Friended!", true);
            return true;
        }
        Command.sendClientSideMessage(name + " Is Already a Friend!", true);
        return false;
    }

    public boolean removeFriend(String name) {
        boolean friend = false;
        int index = 0;
        for (int i = 0; i < friends.size(); i++) {
            if (friends.get(i).equalsIgnoreCase(name)) {
                friend = true;
                index = i;
                break;
            }
        }
        if (friend) {
            friends.remove(index);
            Command.sendClientSideMessage(name + "Has been UnFriended!", true);
            return true;
        }
        Command.sendClientSideMessage(name + " Is Not a Friend!", true);
        return false;
    }

    public boolean isFriend(String name) {
        for (String names : friends) {
            if (names.equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }
}
