package com.cousinware.arete.module.Client;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.IntSetting;
import org.lwjgl.glfw.GLFW;

public class ClickGui extends Module {


    public static IntSetting r = new IntSetting();
    public static IntSetting g = new IntSetting();
    public static IntSetting b = new IntSetting();
    public static IntSetting a = new IntSetting();

    public ClickGui() {
        super("ClickGui", Category.Client, -1);
        this.setKeybind(GLFW.GLFW_KEY_Y);


        r.setName("Red").setMin(0).setMax(255).setValue(110).build(this);
        g.setName("Green").setMin(0).setMax(255).setValue(35).build(this);

        b.setName("Blue").setMin(0).setMax(255).setValue(202).build(this);
        a.setName("Alpha").setMin(0).setMax(255).setValue(125).build(this);

    }

    int delay = 0;

    @Override
    public void onUpdate() {
        delay++;
        if (delay > 1) {
            mc.setScreen(AreteClient.areteGui);
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
