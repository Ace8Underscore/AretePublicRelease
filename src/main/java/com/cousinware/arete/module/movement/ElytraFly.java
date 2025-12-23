package com.cousinware.arete.module.movement;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.Timer;
import com.cousinware.arete.utils.game.InventoryUtils;
import com.cousinware.arete.utils.rendering.nvg.string.coloredstring.ColoredString;
import com.cousinware.arete.utils.rotations.RotationSystem;
import com.cousinware.arete.utils.settings.*;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.PlayerInput;

import java.awt.*;

import static com.cousinware.arete.module.movement.ElytraFly.ForeverElytraFly.flyingState;


public class ElytraFly extends Module {

    public static ModeSetting modeSetting = new ModeSetting();

    //Forever
    public static BoolSetting maintainYForever = new BoolSetting();
    public static BoolSetting climbY = new BoolSetting();
    public static BoolSetting fireworkMaintainForever = new BoolSetting();
    public static BoolSetting ignoreMousePitchInputForever = new BoolSetting();

    //Bounce
    public static BoolSettingContainer pitchSpoof = new BoolSettingContainer();
    public static DoubleSetting pitchSpoofAngle = new DoubleSetting();

    //Firework
    public static ModeSetting delayMode = new ModeSetting();
    public static DoubleSetting delay = new DoubleSetting();
    public static RotationSystem rotationSystem = new RotationSystem();
    public static IntSetting tpsCompensate = new IntSetting();


    public ElytraFly() {
        super("ElytraFly", Category.Movement, -1);
        modeSetting.setValue("Forever").setModes("Forever", "Bounce", "Firework").setName("Mode").build(this).setChangeMode(() -> {
            if (modeSetting.getValue().equalsIgnoreCase("Bounce")) {
                maintainYForever.setShown(false);
                fireworkMaintainForever.setShown(false);
                ignoreMousePitchInputForever.setShown(false);
                climbY.setShown(false);
                pitchSpoof.setShown(true);
                delayMode.setShown(false);
                delay.setShown(false);
                tpsCompensate.setShown(false);
            } else if (modeSetting.getValue().equals("Forever")) {
                maintainYForever.setShown(true);
                fireworkMaintainForever.setShown(true);
                ignoreMousePitchInputForever.setShown(true);
                climbY.setShown(!maintainYForever.getValue());
                pitchSpoof.setShown(false);
                delayMode.setShown(false);
                delay.setShown(false);
                tpsCompensate.setShown(false);
            } else if (modeSetting.getValue().equalsIgnoreCase("Firework")) {
                maintainYForever.setShown(false);
                climbY.setShown(false);
                fireworkMaintainForever.setShown(false);
                ignoreMousePitchInputForever.setShown(false);
                pitchSpoof.setShown(false);
                pitchSpoofAngle.setShown(false);
                delayMode.setShown(true);
                delay.setShown(delayMode.getValue().equals("Constant"));
                tpsCompensate.setShown(delayMode.getValue().equals("Dynamic"));
            }
        });


        //Forever Mode Settings
        maintainYForever.setValue(false).setName("MaintainY").setDescription("With this disabled the player will gain height; enable if all you care about is horizontal speed aka go fast").build(this);
        maintainYForever.setDisableAction(() -> climbY.setShown(true));
        maintainYForever.setEnableAction(() -> climbY.setShown(false));
        climbY.setName("ClimbY").setValue(false).setDescription("Modifies flying algo to make your Y go as high as you want").build(this);
        fireworkMaintainForever.setValue(true).setName("FireworkMaintain").setDescription("If the height drops due to looking around a firework will be used to maintain height. P.S. fireworks wont be used if yaw is not modified").build(this);
        ignoreMousePitchInputForever.setValue(true).setName("IgnorePitchInput").build(this);

        //Bounce Mode Settings

        pitchSpoof.setName("PitchSpoof").setValue(true).build(this);
        pitchSpoofAngle.setName("Angle").setMin(0).setMax(90).setValue(75).build(this, pitchSpoof, "ElytraFlyBouncePitchSpoofAngle");

        //Firework Mode Settings

        delayMode.setValue("Dynamic").setModes("Dynamic", "Constant").setName("Delay").build(this);
        delayMode.setChangeMode(() -> {
            if (delayMode.getValue().equalsIgnoreCase("Constant")) {
                delayMode.setDescription("Constant will conflict with FireworkTweaks AntiWaste");
                tpsCompensate.setShown(false);
            } else {
                delayMode.setDescription("Dynamic Uses a firework once current firework ends");
                tpsCompensate.setShown(true);
            }
            delay.setShown(!delayMode.getValue().equals("Dynamic"));
        });
        delay.setValue(2.1).setMin(.5).setMax(4).setName("FireworkDelay").build(this);
        delay.setModifyAction(() -> {
            FireworkElytraFly.timer.setDelay((int) (delay.getValue() * 1000));
        });
        tpsCompensate.setName("TpsComp").setMin(0).setMax(10).setValue(3).build(this).setDescription("Fires firework selected ticks before to compensate for ping and tps lag");


    }

    public void onDisable() {
        if (modeSetting.getValue().equalsIgnoreCase("Firework")) FireworkElytraFly.onDisable();

    }

    public void onEnable() {
        if (modeSetting.getValue().equalsIgnoreCase("forever")) ForeverElytraFly.onEnable();
        if (modeSetting.getValue().equalsIgnoreCase("bounce")) BounceElytraFly.onEnable();
        if (modeSetting.getValue().equalsIgnoreCase("Firework")) FireworkElytraFly.onEnable();


    }

    @Override
    public void onUpdate() {
        if (modeSetting.getValue().equalsIgnoreCase("forever")) ForeverElytraFly.onUpdate();
        if (modeSetting.getValue().equalsIgnoreCase("bounce")) BounceElytraFly.onUpdate();
        if (modeSetting.getValue().equalsIgnoreCase("Firework")) FireworkElytraFly.onUpdate();


    }

    @Override
    public ColoredString getHudInfo() {
        return ColoredString.of(Color.WHITE, modeSetting.getValue().equalsIgnoreCase("Forever") ? flyingState.name() : modeSetting.getValue());
        //return null;
    }


    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof PlayerPositionLookS2CPacket && this.isEnabled()) {
            BounceElytraFly.rubberbanded = true;
        }
    }

    @Subscribe
    public void packet(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket && modeSetting.getValue().equalsIgnoreCase("bounce")) {


            if (((PlayerMoveC2SPacket) event.getPacket()).onGround) {
                if (mc.player.isOnGround()) mc.player.jump();
                //mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));


            }
            //pitch for movement server side so no flag
            if (((PlayerMoveC2SPacket) event.getPacket()).pitch != pitchSpoofAngle.getValue())
                ((PlayerMoveC2SPacket) event.getPacket()).pitch = Float.parseFloat(String.valueOf(pitchSpoofAngle.getValue()));
        }
    }

    @Subscribe
    public void packetListener(PacketEvent.Send event) {
        if (!modeSetting.getValue().equalsIgnoreCase("Firework")) return;
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet && FireworkElytraFly.spoofPitch && modeSetting.getValue().equalsIgnoreCase("firework")) {
            if (rotationSystem.getAction().equals(RotationSystem.ACTION.Rotating)) {
                rotationSystem.modifyPacket(packet);
            }
        }

        if (mc.player.isGliding() && event.getPacket() instanceof PlayerInputC2SPacket(PlayerInput input)) {
            if (input.forward() || input.backward() || input.left() || input.right()) {
                event.setCancelled(true);
                mc.getNetworkHandler().sendPacket(new PlayerInputC2SPacket(new PlayerInput(false, false, false, false, input.jump(), input.jump(), input.sprint())));
            }
        }
    }

    public static class FireworkElytraFly {



        static float straightPitch = -1.05f;
        static float magicPitch = -1.075f;
        static boolean spoofPitch = false;
        static Timer timer = new Timer((int) (delay.getValue() * 1000));
        static double startingY = -180;
        static Timer timerFromUse = new Timer(500);
        public static boolean fireworking = false;
        static int lastUsedFireworkDuration = -1;
        static int lastUsedFireworkTickFrom = 0;

        public static void onUpdate() {
            if (!mc.player.isGliding()) {
                spoofPitch = false;
                rotationSystem.forceStop();
                return;
            } else {
                spoofPitch = true;
                if (startingY != -180) {
                    changePitchBasedOnPosatan();
                } else magicPitch = straightPitch;
                rotationSystem.rotatePitch(magicPitch, true);
            }

            if (mc.player.getGlidingTicks() == 1) {
                startingY = mc.player.getPos().getY();
                //firework();
                return;
            }
            if (delayMode.getValue().equalsIgnoreCase("Dynamic")) {
                if (canFirework()) {
                    fireworking = true;
                    firework();
                } else {
                    fireworking = false;
                }
            } else {
                if (timer.canTick()) {
                    firework();
                }
            }

        }

        public static void onEnable() {
            if (mc.player.isGliding()) {
                startingY = mc.player.getPos().getY();
                //firework();
            }
        }

        public static void onDisable() {
            rotationSystem.forceStop();
            fireworking = false;
        }


        public static void changePitchBasedOnPosatan() {
            double currentY = mc.player.getPos().getY();
            double deltaY = currentY - startingY;
            double gain = 1.32;
            float offset = (float) Math.atan(deltaY * gain) * 10;

            magicPitch = straightPitch + offset;
        }



        public static void firework() {
            if (mc.player.isSprinting()) mc.player.setSprinting(false);
            useFirework();

        }

        private static void useFirework() {
            InventoryUtils.InventoryTask task1 = new InventoryUtils.InventoryTask(Items.FIREWORK_ROCKET);
            task1.swapItem();
            task1.useItem(mc.player.lastHeadYaw, magicPitch);
            lastUsedFireworkDuration = getFireworkFlightDurationToTicks(mc.player.getMainHandStack());
            task1.swapItem();

        }

        public static int getFireworkFlightDurationToTicks(ItemStack stack) {
            if (!stack.isOf(Items.FIREWORK_ROCKET)) {
                //System.out.println("Failed to get firework");
                return lastUsedFireworkDuration;
            }
            int duration = stack.getComponents().get(DataComponentTypes.FIREWORKS).flightDuration();
            int randomOffset = 4;
            return switch (duration) {
                //added randomOffset to each return due to mc adding random 0-12 tick of life span
                case 3 -> 30 + randomOffset;
                case 2 -> 20 + randomOffset;
                default -> 10 + randomOffset;
            };
        }


        public static boolean canFirework() {
            lastUsedFireworkTickFrom++;
            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof FireworkRocketEntity firework) {
                    if (firework.getOwner() != null && firework.getOwner().equals(mc.player) && tpsCompensate.getValue() != 0) {
                        if (firework.age >= lastUsedFireworkDuration - tpsCompensate.getValue() && lastUsedFireworkTickFrom > lastUsedFireworkDuration - (tpsCompensate.getValue() + 5)) {
                            //System.out.println("Owner: " + firework.getOwner() + "  Age: " + firework.age + "  LastUsedFireworkDuration: " + lastUsedFireworkDuration);
                            lastUsedFireworkTickFrom = 0;
                            timer.reset();
                            return true;
                        }
                    }

                    if (firework.getOwner() != null && firework.getOwner().equals(mc.player)) {
                        return false;

                    }
                }

            }
            return timerFromUse.canTick();
        }
    }


    public class BounceElytraFly {

        public static boolean rubberbanded = false;

        public static void onUpdate() {
            mc.player.setSprinting(true);
            if (!mc.player.isGliding() && !mc.player.isOnGround()) {
                mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                mc.player.startGliding();
            } else {

            }
        }

        private static boolean ignoreGround(ClientPlayerEntity player) {
            if (!player.isTouchingWater() && !player.hasStatusEffect(StatusEffects.LEVITATION)) {
                ItemStack itemStack = player.getEquippedStack(EquipmentSlot.CHEST);
                player.startGliding();
                return true;
            } else return false;
        }


        public static void onEnable() {

        }
    }


    class ForeverElytraFly {

        static float glidingDownPitch = 32.5f;
        static float spikePitch = -49f;
        static float pitchModify = .5f;
        static int fallDistance = 50;
        static double startingY;
        static double minY;
        static ForeverElytraFly.State flyingState = ForeverElytraFly.State.Gliding;
        static int glideTick = 0;
        static Timer fireWorkTimer = new Timer(2000);


        public static void onUpdate() {
            double currentY = mc.player.getPos().getY();
            if (flyingState.equals(State.Gliding)) {
                glideTick++;

                if (glideTick == 2) {
                    if (!maintainYForever.getValue() && climbY.getValue()) {
                        startingY = currentY;
                        minY = currentY - fallDistance;
                    }
                }
            }
            //below line fixes firework issue with maintainY and refer to if (maintainY.getValue()) return mc.player.getPos().getY() >= startingY; in shouldStartGliding fail safe
            if (flyingState.equals(State.SpikePitch) && mc.player.getPitch() > glidingDownPitch - 25 && maintainYForever.getValue() && fireworkMaintainForever.getValue())
                flyingState = (State.FireWork);

            if (mc.player.isGliding() && !flyingState.equals(State.FireWork)) {
                if (flyingState.equals(State.Gliding)) {
                    mc.player.setPitch(glidingDownPitch);
                }


                if (currentY <= minY && !flyingState.equals(State.SpikePitch)) {
                    mc.player.setPitch(spikePitch);
                    flyingState = State.SpikePitch;
                    return;
                }

                if (flyingState.equals(State.SpikePitch)) {
                    mc.player.setPitch(mc.player.getPitch() + pitchModify);
                    if (shouldStartGliding()) {
                        flyingState = State.Gliding;
                        glideTick = 0;

                    }
                    return;
                }

                if (currentY <= minY + .5 && fireworkMaintainForever.getValue()) {
                    if (glideTick < 80) {
                        flyingState = State.FireWork;
                        glideTick = 0;
                        return;
                    }
                }


            }
            if (mc.player.isGliding() && flyingState.equals(State.FireWork)) {
                mc.player.setPitch(spikePitch);
                if (isLosingHeight() && currentY > startingY) {
                    flyingState = State.SpikePitch;
                    return;
                }

                if (isLosingHeight() && fireWorkTimer.canTick()) useFirework();
            }

        }

        public static void onEnable() {
            startingY = mc.player.getPos().getY();
            minY = startingY - fallDistance;
            flyingState = State.Gliding;
            glideTick = 0;
        }

        private static void useFirework() {
            InventoryUtils.HotBarTask task = new InventoryUtils.HotBarTask(true, Items.FIREWORK_ROCKET);
            task.useItem();
            task.swapBack(true);
        }

        public static boolean shouldStartGliding() {
            if (maintainYForever.getValue()) return mc.player.getPos().getY() >= startingY;
            return isLosingHeight();
        }

        public static boolean isLosingHeight() {
            return (mc.player.getY() < mc.player.lastY);
        }


        public enum State {
            Gliding,
            SpikePitch,
            FireWork,

        }
    }
}
