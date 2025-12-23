package com.cousinware.arete.module.player;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.managers.RotationManager;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.RotationSystem;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CactusBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.PriorityQueue;
import java.util.Queue;

public class AntiVoid extends Module {

    public static BoolSetting cactus = new BoolSetting();
    public static BoolSetting web = new BoolSetting();
    public static BoolSetting fireBall = new BoolSetting();
    public static BoolSetting shulkerBullet = new BoolSetting();
    public static ModeSetting fire = new ModeSetting();

    public AntiVoid() {
        super("Avoid", Category.Player, -1, "Avoids Certain Things!");
        cactus.setValue(true).setName("Cactus").build(this);
        web.setValue(true).setName("Web").build(this);
        fireBall.setValue(true).setName("FireBall").build(this);
        shulkerBullet.setValue(true).setName("ShulkerBullet").build(this);
        fire.setName("Fire").setModes("Off", "Break", "Avoid").setValue("Break").build(this);
    }

    @Override
    public void onUpdate() {
       // mc.player.setYaw(mc.player.getBodyYaw() + .0005f);
        //if (cactus.getValue()) CactusBlock.COLLISION_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);
        CactusBlock.COLLISION_SHAPE = cactus.getValue() ? Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0) : Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);
        if (!isEnabled()) System.out.println("disabled");

        //!AntiFire
        if (fire.getValue().equalsIgnoreCase("Break")) breakFire();
        else if (fire.getValue().equalsIgnoreCase("Avoid"));

        //!FireBall
        if (fireBall.getValue()) antiFireBall();

        //!shulkerBullet
        if (shulkerBullet.getValue()) antiFireBall();
    }

    public void antiFireBall() {
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof FireballEntity || entity instanceof ShulkerBulletEntity && shulkerBullet.getValue()) {
                if (mc.player.distanceTo(entity) < 5 && !rotationSystem.getCurrentAction().equals(RotationSystem.ACTION.Rotating)) {
                    RotationManager.Rotaion rotaion = AreteClient.rotationManager.genRotation(entity.getEyePos());
                    mc.interactionManager.attackEntity(mc.player, entity);
                    mc.player.swingHand(Hand.MAIN_HAND);
                    rotationSystem.setCurrentAction(RotationSystem.ACTION.Rotating);
                    rotationSystem.updateRotation(rotaion);
                    break;
                }
            }
        }
    }

    public void breakFire() {

        Queue<BlockPos> hitLocations = new PriorityQueue<>();
        //TODO FIX WITH SETTING
        for (int x = -4 + mc.player.getBlockX(); x < 4 + mc.player.getBlockX(); x++)  {
            for (int y = -4 + mc.player.getBlockY(); y < 4 + mc.player.getBlockY(); y++)  {
                for (int z = -4 + mc.player.getBlockZ(); z < 4 + mc.player.getBlockZ(); z++)  {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (mc.world.getBlockState(pos).getBlock().equals(Blocks.FIRE) || mc.world.getBlockState(pos).getBlock().equals(Blocks.SOUL_FIRE)) hitLocations.add(pos);
                }
            }
        }
        if (hitLocations.isEmpty()) return;
        //Command.sendClientSideMessage(String.valueOf(hitLocations.peek()));
        //mc.world.breakBlock(hitLocations.poll(), false);
        BlockPos blockPos = hitLocations.poll();
        mc.player.swingHand(Hand.MAIN_HAND);
        mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.UP));

        //mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, Direction.UP));

    }

    @Subscribe
    public void outGoingPackets(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket && rotationSystem.getCurrentAction().equals(RotationSystem.ACTION.Rotating)) {
            Command.sendClientSideMessage("rotating");
            ((PlayerMoveC2SPacket) event.getPacket()).yaw = rotationSystem.getYaw();
            ((PlayerMoveC2SPacket) event.getPacket()).pitch = rotationSystem.getPitch();
            //
            rotationSystem.setCurrentAction(RotationSystem.ACTION.Nothing);
        }
    }

    public void onDisable() {
        //reset cactus box
        //TODO FIX BUG IF AVOID IS TURNED OFF CLIENT CAN PHASE INTO CACTUS
        if (cactus.getValue()) CactusBlock.COLLISION_SHAPE =  Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);
        rotationSystem.stop();
    }

    public void onEnable() {
        rotationSystem = new RotationSystem();
        rotationSystem.setMoveFix(false);
    }

}
