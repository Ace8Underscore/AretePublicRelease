package com.cousinware.arete.utils;

import com.cousinware.arete.client.AreteClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Users {

    public URL link;
    public ArrayList<String> users = new ArrayList<>();

    public Users() {
        fetch();
        genMembers();
    }

    public void genMembers() {
        try {
            AreteClient.members.clear();
            AreteClient.validIgns.clear();
            for (String s : users) {
                //Name:Rank:??TBD
                String[] split = s.split(":");
                String[] splitRanks = getRanks(split[1]);
                String[] splitAlaias = new String[]{""};
                try {
                    splitAlaias = getAliases(split[2]);
                } catch (Exception e) {

                }
                AreteClient.members.add(new Member(split[0], splitRanks, splitAlaias));
                AreteClient.validIgns.add(split[0]);
                Arrays.stream(splitAlaias).toList().forEach(alias -> AreteClient.validIgns.add(alias));


            }
        } catch (Exception e) {
            //failed to load members. site formatting????
        }
    }

    public String[] getAliases(String aliases) {
        ArrayList<String> ab = new ArrayList<>();
        String[] a = aliases.split(",");
        if (a.length < 1) return null;
        else {
            for (String s : a) {
                ab.add(s);
            }
        }

        return ab.toArray(new String[0]);
    }

    public String[] getRanks(String ranks) {
        ArrayList<String> ab = new ArrayList<>();
        String[] a = ranks.split(",");
        if (a.length < 1) return null;
        else {
            for (String s : a) {
                ab.add(s);
            }
        }
        return ab.toArray(new String[0]);
    }

    public void fetch() {
        try {
            link = new URL("https://pastebin.com/raw/8AwU8qgV");
            getFriends();
        } catch (MalformedURLException e) {
            System.out.println(e.getCause());
        }
    }

    public void getFriends() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(link.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                users.add(inputLine);
            }
        } catch (Exception e) {

        }
    }

    public ArrayList<String> getUsers() {
        //fetch();
        return this.users;
    }


}
