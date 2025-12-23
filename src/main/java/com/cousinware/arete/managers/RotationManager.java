package com.cousinware.arete.managers;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.TickEvent;
import com.cousinware.arete.utils.MinecraftInterface;
import com.cousinware.arete.utils.rotations.RotationSystem;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Random;

@Setter
@Getter
public class RotationManager implements MinecraftInterface {

    public ArrayList<RotationSystem> rotationSystems = new ArrayList<>();
    public float simulationYaw = -720;
    public float simulationPitch = -180;
    public boolean simulation = false;

    public RotationManager() {

        AreteClient.eventBus.register(this);
    }

    boolean pos = true;

    @Subscribe
    public void onTick(TickEvent event) {
        for (RotationSystem rotSys : rotationSystems) {
            if (mc.player == null || mc.world == null || ignoreScreen()) return;
            if (rotSys.getAction().equals(RotationSystem.ACTION.Rotating)) {
                break;
            }
        }
        if (mc.player == null || mc.world == null || ignoreScreen()) return;

        int i = pos ? 1 : -1;
        mc.player.setYaw(mc.player.getBodyYaw() + .0005f * i);
        pos = !pos;
    }

    public boolean ignoreScreen() {
        return (mc.currentScreen instanceof CraftingScreen) || (mc.currentScreen instanceof BeaconScreen) || (mc.currentScreen instanceof CartographyTableScreen) || (mc.currentScreen instanceof BrewingStandScreen) || (mc.currentScreen instanceof CrafterScreen) || (mc.currentScreen instanceof EnchantmentScreen) || (mc.currentScreen instanceof ForgingScreen<?>) || (mc.currentScreen instanceof HopperScreen) || (mc.currentScreen instanceof ShulkerBoxScreen) || (mc.currentScreen instanceof MerchantScreen) || (mc.currentScreen instanceof SmithingScreen) || (mc.currentScreen instanceof StonecutterScreen) || (mc.currentScreen instanceof Generic3x3ContainerScreen) || (mc.currentScreen instanceof AbstractFurnaceScreen<?>) || (mc.currentScreen instanceof AbstractSignEditScreen) || (mc.currentScreen instanceof BookEditScreen) || (mc.currentScreen instanceof BookScreen) || (mc.currentScreen instanceof GenericContainerScreen) || (mc.currentScreen instanceof HorseScreen);
    }

    public boolean currentlyRotating() {
        for (RotationSystem rotSys : rotationSystems) {
            if (rotSys.getAction().equals(RotationSystem.ACTION.Rotating)) {
                return true;
            }
        }
        return false;
    }


    public double getYaw(Vec3d pos, boolean packetDifference) {
        Random random = new Random();

        return (packetDifference ? random.nextDouble(0, .01) : 0) + mc.player.getYaw() + MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(pos.getZ() - mc.player.getZ(), pos.getX() - mc.player.getX())) - 90f - mc.player.getYaw());
    }

    public double getPitch(Vec3d pos, boolean packetDifference) {
        Random random = new Random();
        double diffX = pos.getX() - mc.player.getX();
        double diffY = pos.getY() - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double diffZ = pos.getZ() - mc.player.getZ();

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        return (packetDifference ? random.nextDouble(0, .01) : 0) + mc.player.getPitch() + MathHelper.wrapDegrees((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)) - mc.player.getPitch());
    }

    public Rotation genRotation(Vec3d pos) {
        return new Rotation((float) getYaw(pos, true), (float) getPitch(pos, true));
    }


    @Getter
    public static class Rotation {

        float yaw;
        float pitch;
        BlockHitResult hitResult;

        public Rotation(float yaw, float pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public Rotation(float yaw, float pitch, BlockHitResult hitResult) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.hitResult = hitResult;
        }

    }

}
