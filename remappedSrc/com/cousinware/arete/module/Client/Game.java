package com.cousinware.arete.module.Client;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;

public class Game extends Module {


    public Game() {
        super("Game", Category.Client, -1);
    }

    int delay = 0;

    @Override
    public void onUpdate() {
        delay++;
        if (delay > 1) {
            AreteClient.gameGui.gameRunning = false;
            mc.setScreen(AreteClient.gameGui);

            this.disable();
            delay = 0;
        }
    }
}

