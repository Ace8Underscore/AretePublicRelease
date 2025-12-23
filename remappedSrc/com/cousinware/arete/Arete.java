package com.cousinware.arete;

import com.cousinware.arete.client.AreteClient;
import net.fabricmc.api.ClientModInitializer;

public class Arete implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        AreteClient.memberUpdateThread.start();
    }

}
