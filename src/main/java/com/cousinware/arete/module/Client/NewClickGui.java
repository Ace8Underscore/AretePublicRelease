package com.cousinware.arete.module.Client;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.*;
import org.lwjgl.glfw.GLFW;

public class NewClickGui extends Module {


    public static IntSetting rMainColor = new IntSetting();
    public static IntSetting gMainColor = new IntSetting();
    public static IntSetting bMainColor = new IntSetting();
    public static IntSetting aMainColor = new IntSetting();
    public static IntSetting rSecondaryColor = new IntSetting();
    public static IntSetting gSecondaryColor = new IntSetting();
    public static IntSetting bSecondaryColor = new IntSetting();
    public static IntSetting aSecondaryColor = new IntSetting();
    public static ModeSetting descriptionMode = new ModeSetting();
    public static BoolSetting glowOnOpen = new BoolSetting();
    public static DoubleSetting scale = new DoubleSetting();
    public static BoolSettingContainer mainColor = new BoolSettingContainer();
    public static BoolSettingContainer secondaryColor = new BoolSettingContainer();
    int delay = 0;

    public NewClickGui() {
        super("NewClickGui", Category.Client, -1);
        this.setKeybind(GLFW.GLFW_KEY_Y);

//        Color mainColor = new Color(31, 241, 255, 255);
//        Color secondaryColor = new Color(255, 0, 60, 255);
        mainColor.setName("MainColor").setValue(true).build(this);
        rMainColor.setName("Red").setMin(0).setMax(255).setValue(31).build(this, mainColor, "MainColorRed");
        gMainColor.setName("Green").setMin(0).setMax(255).setValue(241).build(this, mainColor, "MainColorGreen");
        bMainColor.setName("Blue").setMin(0).setMax(255).setValue(255).build(this, mainColor, "MainColorBlue");
        aMainColor.setName("Alpha").setMin(0).setMax(255).setValue(255).build(this, mainColor, "MainColorAlpha");

        secondaryColor.setName("SecondaryColor").setValue(true).build(this);
        rSecondaryColor.setName("Red").setMin(0).setMax(255).setValue(255).build(this, secondaryColor, "SecondaryColorRed");
        gSecondaryColor.setName("Green").setMin(0).setMax(255).setValue(0).build(this, secondaryColor, "SecondaryColorGreen");
        bSecondaryColor.setName("Blue").setMin(0).setMax(255).setValue(60).build(this, secondaryColor, "SecondaryColorBlue");
        aSecondaryColor.setName("Alpha").setMin(0).setMax(255).setValue(255).build(this, secondaryColor, "SecondaryColorAlpha");
        descriptionMode.setName("Info").setValue("Minimal").setModes("Off", "Minimal").build(this);
        glowOnOpen.setValue(true).setName("GlowOnOpen").build(this).setToggleAction(() -> {
            //Component new me.surge.animation.Animation(() -> 350f, NewClickGui.glowOnOpen.getValue(), () -> Easing.QUINT_OUT);
            if (!glowOnOpen.getValue())
                AreteClient.newGui.frames.forEach(frame -> frame.getModuleComponents().forEach(moduleComponent -> moduleComponent.getSettingComponent().forEach(component -> component.animation.setState(false))));
            else
                AreteClient.newGui.frames.forEach(frame -> frame.getModuleComponents().forEach(moduleComponent -> moduleComponent.getSettingComponent().forEach(component -> component.animation.setState(true))));

        });
        scale.setName("Scale").setValue(1).setMin(.2).setMax(2).build(this);
        setDrawn(false);

    }

    @Override
    public void onUpdate() {
        delay++;
        if (delay > 1) {

            mc.setScreen(AreteClient.newGui);
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
