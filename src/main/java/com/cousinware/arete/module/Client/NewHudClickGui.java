package com.cousinware.arete.module.Client;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;

public class NewHudClickGui extends Module {


    int delay = 0;

    public NewHudClickGui() {
        super("NewHudClickGui", Category.Client, -1);


    }

    @Override
    public void onUpdate() {
        delay++;
        if (delay > 1) {

            mc.setScreen(AreteClient.newHudGui);
//            for (Frame frame : AreteGui.frames) {
//                boolean xPos = Math.random() >= .5;
//                boolean yPos = Math.random() >= .5;
//                frame.velocity.setVelocity(Math.random() * 5 * (xPos ? -1 : 1), Math.random() * 5 * (yPos ? -1 : 1));
//            }
            this.disable();
            delay = 0;
        }
    }
}
