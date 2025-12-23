package com.cousinware.arete.utils.server;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class API2b2t {


    public API2b2t() {

    }

    static boolean debug = FabricLoader.getInstance().isDevelopmentEnvironment();

    public static void request(String url1, boolean sendToChat) {
        new Thread(() -> {
            ArrayList<String> data = new ArrayList<>();
            try {
                URL url = new URL(url1);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    data.add(inputLine);
                }

                String[] strings = data.getFirst().substring(1, data.getFirst().length() - 1).split(",");
                if (sendToChat) {
                    Command.sendClientSideMessage("User: " + url1.split("=")[url1.split("=").length - 1], false);
                    Arrays.stream(strings).forEach(s -> Command.sendClientSideMessage(s, false));
                }
            } catch (IOException e) {
                //AreteClient.LOGGER.error("Failed to connect to server");


            }

        }).start();

    }


    public static String request(String url1) {
        ArrayList<String> data = new ArrayList<>();


        try {
            URL url = new URL(url1);


            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                data.add(inputLine);
                if (inputLine.contains("null")) return "";
            }


        } catch (IOException e) {
            AreteClient.LOGGER.error("Failed to connect to server");

        }


        return data.toString();
    }
}
