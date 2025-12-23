package com.cousinware.arete.module.world;

import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.settings.ModeSetting;

import java.security.SecureRandom;

public class NoBlockRotation extends Module {

    public static final SecureRandom secureRandom = new SecureRandom();
    public static ModeSetting mode = new ModeSetting();
    public static long randomLong = 0;

    public NoBlockRotation() {
        super("BlockSpoof", Category.World, -1, "Spoofs block rotations to prevent coord exploit");
        mode.setName("Mode").setValue("AutoOffset").setModes("Randomize", "Constant", "AutoOffset").build(this);
        mode.setChangeMode(() -> {
            if (mc.world != null) mc.worldRenderer.reload();
        });
        randomLong = secureRandom.nextLong();
    }

    public static long trueRandomness() {
        return secureRandom.nextLong();
    }

    public void onEnable() {
        if (mc.world != null) mc.worldRenderer.reload();

    }

    public void onDisable() {
        if (mc.world != null) mc.worldRenderer.reload();

    }
}
