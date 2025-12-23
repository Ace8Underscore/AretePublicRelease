package com.cousinware.arete.managers;

import java.util.ArrayList;

public class FriendManager {

    ArrayList<String> friends;

    public FriendManager() {
        friends = new ArrayList<>();
    }

    public boolean addFriend(String name) {
        boolean friend = false;
        int index = 0;
        for (String names : friends) {
            if (names.equalsIgnoreCase(name)) {
                friend = true;
                break;
            }
        }
        if (!friend) {
            friends.add(name);
            return true;
        }
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
            return true;
        }
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
