package com.cousinware.arete.module.player;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.module.movement.ElytraFly;
import com.cousinware.arete.utils.Timer;
import com.cousinware.arete.utils.game.InventoryUtils;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class FireworkTweaks extends Module {

    BoolSetting antiWaste = new BoolSetting();
    BoolSetting forceUse = new BoolSetting();
    BoolSetting onlyFlying = new BoolSetting();
    Timer timerFromUse = new Timer(500);

    public FireworkTweaks() {
        super("FireworkTweaks", Category.Player, -1, "Modifies Firework Vanilla Functions");
        antiWaste.setName("AntiWaste").setValue(true).build(this);
        forceUse.setName("ForceUse").setValue(true).build(this).setDescription("Stops fireworks from being used for non flying function");
        onlyFlying.setName("OnlyFlying").setValue(true).build(this).setDescription("Only allows firework use when player is flying");
    }

    @Subscribe
    public void onPacketSend(PacketEvent.Send event) {

        if (mc.world == null || mc.player == null) return;

        if (AreteClient.moduleManager.getModuleByName("ElytraFly").isEnabled() && ElytraFly.modeSetting.getValue().equalsIgnoreCase("Firework"))
            return;

        if (event.getPacket() instanceof PlayerInteractItemC2SPacket packet && antiWaste.getValue() && mc.player.isGliding()) {
                if (mc.player.getStackInHand(packet.getHand()).getItem().equals(Items.FIREWORK_ROCKET) && !canFirework()) {
                    event.setCancelled(true);

                    return;
                } else {
                    timerFromUse.reset();
                }
            }
        if (event.getPacket() instanceof PlayerInteractBlockC2SPacket packet && antiWaste.getValue() && mc.player.isGliding()) {
                if (mc.player.getStackInHand(packet.getHand()).getItem().equals(Items.FIREWORK_ROCKET) && !canFirework()) {
                    event.setCancelled(true);
                    return;
                } else {
                    timerFromUse.reset();
                }
            }


        //forceuse
        if (event.getPacket() instanceof PlayerInteractBlockC2SPacket packet && forceUse.getValue() && mc.player.isGliding()) {
            if (mc.player.getStackInHand(packet.getHand()).getItem().equals(Items.FIREWORK_ROCKET)) {
                event.setCancelled(true);
                mc.getNetworkHandler().sendPacket(new PlayerInteractItemC2SPacket(packet.getHand(), InventoryUtils.getSequence(), mc.player.getYaw(), mc.player.getPitch()));
            }
        }


        //only flying check
        if (!mc.player.isGliding() && onlyFlying.getValue()) {
            if (event.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {
                if (mc.player.getStackInHand(packet.getHand()).getItem().equals(Items.FIREWORK_ROCKET) && !isInteractable(packet.getBlockHitResult().getBlockPos(), packet.getBlockHitResult())) {

                    event.setCancelled(true);
                }
            } else if (event.getPacket() instanceof PlayerInteractItemC2SPacket packet) {
                if (mc.player.getStackInHand(packet.getHand()).getItem().equals(Items.FIREWORK_ROCKET)) {
                    event.setCancelled(true);
                }
            }
        }


    }

    public boolean isInteractable(BlockPos pos, BlockHitResult blockHitResult) {
        ActionResult result = mc.world.getBlockState(pos).onUse(mc.world, mc.player, blockHitResult);
        return result.isAccepted();
    }

    //


    //checks firework entites and if ones owner is the player we return false; otherwise if we can tick from our delay timer(which is used because server takes a couple ticks to spawn firework client side)

    public boolean canFirework() {
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof FireworkRocketEntity firework) {
                if (firework.getOwner() != null && firework.getOwner().equals(mc.player)) return false;
            }

        }
        return timerFromUse.canTick();
    }
}
