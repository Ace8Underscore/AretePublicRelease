package com.cousinware.arete.module.movement;

import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.game.InventoryUtils;
import com.cousinware.arete.utils.game.PlayerUtils;
import com.cousinware.arete.utils.rendering.nvg.string.coloredstring.ColoredString;
import com.cousinware.arete.utils.rotations.Rotation;
import com.cousinware.arete.utils.rotations.RotationSystem;
import com.cousinware.arete.utils.settings.BoolSettingContainer;
import com.cousinware.arete.utils.settings.IntSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.awt.*;

public class LongJump extends Module implements Rotation {

    Step step = Step.ChestToElytra;

    IntSetting flightTime = new IntSetting();
    BoolSettingContainer spoofPitch = new BoolSettingContainer();
    IntSetting pitch = new IntSetting();
    int timeOutTicksFirework = 4;
    int timeOutTicksFireworkCounter = 0;
    int chestSlot = 0;
    int flightTimeDelay = 0;
    int deployFail = 0;
    RotationSystem rotationSystem;

    public LongJump() {
        super("LongJump", Category.Movement, -1);
        flightTime.setValue(7).setMin(0).setMax(50).setDescription("Flight Time in Ticks").setName("FlightTime").build(this);
        spoofPitch.setValue(false).setName("SpoofPitch").build(this);
        pitch.setName("Pitch").setValue(-5).setMin(-90).setMax(90).build(this, spoofPitch, "PitchSpoofPitch");
        setSaveToConfig(false);
    }

    public void onEnable() {
        rotationSystem = new RotationSystem();
        step = Step.ChestToElytra;
        flightTimeDelay = 0;
        timeOutTicksFirework = 4;
        timeOutTicksFireworkCounter = 0;
        deployFail = 0;
    }

    private void useFirework() {
//        InventoryUtils.HotBarTask task = new InventoryUtils.HotBarTask(true, Items.FIREWORK_ROCKET);
//        task.useItem();
//        task.swapBack(true);
//        if (task.isItemNotFound()) {
        InventoryUtils.InventoryTask task1 = new InventoryUtils.InventoryTask(Items.FIREWORK_ROCKET);
        task1.swapItem();
        task1.useItem(mc.player.getHeadYaw(), spoofPitch.getValue() ? pitch.getValue() : mc.player.getPitch());
        task1.swapItem();
        // }

    }

    public void onDisable() {
        rotationSystem.forceStop();
        if (mc.player.isGliding()) mc.player.stopGliding();
        if (!wearingChestPlate()) swapToChest();

        //anti Desync
        mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
        mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
        if (mc.player.isSneaking()) mc.player.setSneaking(false);
    }

    public int getSlotWithStack(ItemStack stack) {
        for (int i = 0; i < mc.player.getInventory().getMainStacks().size(); ++i) {
            if (stack.getItem() == mc.player.getInventory().getMainStacks().get(i).getItem()) {
                return i;
            }
        }

        return -1;
    }

    public void firework() {
        useFirework();
        if (spoofPitch.getValue()) {
            rotationSystem.rotatePitch(pitch.getValue(), true);
        }
    }

    public void onUpdate() {
        if (mc.player.isTouchingWater()) {
            this.disable();
            return;
        }
        if (step.equals(Step.ChestToElytra)) {

            if (wearingChestPlate()) {
                int elytraSlot = getSlotWithStack(new ItemStack(Items.ELYTRA));


                if (elytraSlot == -1 || !InventoryUtils.doesInventoryContain(Items.FIREWORK_ROCKET)) {
                    //STOP no elytra found or no fireworks found
                    this.disable();
                    return;
                }
                //if elytra is in hotbar do correct offset to get correct elySlot
                if (elytraSlot < 9) elytraSlot += 36;
                chestSlot = elytraSlot;

                //swap to elytra and return
                swapToElytra();
                step = Step.Firework;
                return;
            }
        }
        if (step.equals(Step.Firework)) {
            //counter below so if for some od reason we dont get flying IE water/blocks other obstructions we toggle
            if (timeOutTicksFireworkCounter > timeOutTicksFirework) {
                //swapToChest();
                timeOutTicksFireworkCounter = 0;
                this.disable();
            }
            timeOutTicksFireworkCounter++;
            //In Step Firework we do two things start fall flying and then firework
            if (!mc.player.isGliding()) {
                if (mc.player.isOnGround()) {
                    PlayerUtils.jump();
                } else {
                    mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                    mc.player.startGliding();
                    //we return to wait a tick to let server catch up so we can firework away
                    return;
                }
            }

            if (mc.player.isGliding()) {
                firework();

                //Step set to movement we have now used firework and are moving(hopefully)
                step = Step.Movement;
                return;
            }
        }
        if (step.equals(Step.Movement)) {
            //were on the ground so were not flying so lets toggle and swap to chest so we have full armor
            if (!mc.player.isGliding() && mc.player.isOnGround()) {
                //swapToChest();
                this.disable();
                return;
            }

            //if were not fall flying after 3 redeoply events we toggle and swap to chest
            if (!mc.player.isGliding() && deployFail > 3) {
                //swapToChest();
                this.disable();
                return;
            }
            //redeploy just incase lag tbh shouldnt happen
            if (!mc.player.isGliding() && deployFail < 3) {
                deployFail++;
                mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                mc.player.startGliding();
                return;
            }


            //were fall flying now we just wait for delay to pass and we toggle and swap to chest
            if (mc.player.isGliding()) {
                flightTimeDelay++;
                if (flightTimeDelay > flightTime.getValue()) {
                    mc.player.stopGliding();
                    //swapToChest();
                    flightTimeDelay = 0;
                    this.disable();

                }
            }

        }
    }

    public boolean wearingChestPlate() {
        Item item = mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem();

        return item.equals(Items.NETHERITE_CHESTPLATE) || item.equals(Items.DIAMOND_CHESTPLATE) || item.equals(Items.CHAINMAIL_CHESTPLATE) || item.equals(Items.IRON_CHESTPLATE) || item.equals(Items.LEATHER_CHESTPLATE) || item.equals(Items.GOLDEN_CHESTPLATE);
    }

    public void swapToChest() {

        InventoryUtils.InventoryTask task1 = new InventoryUtils.InventoryTask(Items.NETHERITE_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.IRON_CHESTPLATE, Items.LEATHER_CHESTPLATE, Items.GOLDEN_CHESTPLATE);
        task1.swapItem();
        task1.useItem(mc.player.getHeadYaw(), spoofPitch.getValue() ? pitch.getValue() : mc.player.getPitch());
        task1.swapItem();

    }

    public void swapToElytra() {
//        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, 0, SlotActionType.PICKUP, mc.player);
//        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 6, 0, SlotActionType.PICKUP, mc.player);
//        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, 0, SlotActionType.PICKUP, mc.player);

        InventoryUtils.InventoryTask task1 = new InventoryUtils.InventoryTask(Items.ELYTRA);
        task1.swapItem();
        task1.useItem(mc.player.getHeadYaw(), spoofPitch.getValue() ? pitch.getValue() : mc.player.getPitch());
        task1.swapItem();

    }


    public ColoredString getHudInfo() {
        return ColoredString.of(Color.WHITE, "Elytra");
        //return null;
    }

    @Override
    @Subscribe
    public void packetListener(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet && spoofPitch.getValue()) {
            if (rotationSystem.getAction().equals(RotationSystem.ACTION.Rotating)) {
                rotationSystem.modifyPacket(packet);
            }
        }
    }


    public enum Step {
        ChestToElytra,
        Firework,
        Movement,
        SwapToChest,
    }
}
