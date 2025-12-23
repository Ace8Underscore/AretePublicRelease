package com.cousinware.arete.module.player;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.events.event.RenderWorldEvent;
import com.cousinware.arete.managers.Render3DManager;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.ColorUtils;
import com.cousinware.arete.utils.rotations.RotationSystem;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.AbstractFireBlock;
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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.util.PriorityQueue;
import java.util.Queue;

public class AntiVoid extends Module {

    ModeSetting rotaionsMode = new ModeSetting();
    public static BoolSetting cactus = new BoolSetting();
    public static BoolSetting web = new BoolSetting();
    public static BoolSetting fireBall = new BoolSetting();
    public static BoolSetting shulkerBullet = new BoolSetting();
    public static ModeSetting fire = new ModeSetting();
    public RotationSystem rotationSystem;
    public BlockPos renderPosFire;
    public Entity renderEntityShulkerBullet;
    public Entity renderEntityFireBall;

    public AntiVoid() {
        super("Avoid", Category.Player, -1, "Avoids Certain Things!");
        rotationSystem = new RotationSystem();
        rotaionsMode.setName("Rotation").setModes("None", "Old", "Sim").setValue("Sim").build(this);
        cactus.setValue(true).setName("Cactus").build(this);
        web.setValue(true).setName("Web").build(this);
        fireBall.setValue(true).setName("FireBall").build(this);
        shulkerBullet.setValue(true).setName("ShulkerBullet").build(this);
        fire.setName("Fire").setModes("Off", "Break", "Avoid").setValue("Break").build(this);
    }


    @Override
    public void onPostUpdate() {
        if (fire.getValue().equalsIgnoreCase("Avoid")) {
            AbstractFireBlock.BASE_SHAPE = Block.createCuboidShape(0.0, 0, 0.0, 16.0, 16.0, 16.0);

        } else {
            AbstractFireBlock.BASE_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
        }
    }

    @Override
    public void onUpdate() {
        if (fire.getValue().equalsIgnoreCase("Avoid")) {
            AbstractFireBlock.BASE_SHAPE = Block.createCuboidShape(0.0, 0, 0.0, 16.0, 16.0, 16.0);

        } else {
            AbstractFireBlock.BASE_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
        }

        CactusBlock.COLLISION_SHAPE = cactus.getValue() ? Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0) : Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);

        //!AntiFire
        if (fire.getValue().equalsIgnoreCase("Break")) renderPosFire = breakFire();
        //else if (fire.getValue().equalsIgnoreCase("Avoid"));

        //!FireBall
        if (fireBall.getValue()) renderEntityFireBall = antiFireBall();

        //!shulkerBullet
        if (shulkerBullet.getValue()) renderEntityShulkerBullet = antiFireBall();
    }

    public Entity antiFireBall() {
        Entity entity1 = null;
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof FireballEntity || entity instanceof ShulkerBulletEntity && shulkerBullet.getValue()) {
                if (mc.player.distanceTo(entity) < 5) {
                    if (!rotaionsMode.getValue().equalsIgnoreCase("none")) {
                        rotationSystem.rotate(entity.getEyePos(), rotaionsMode.getValue().equalsIgnoreCase("sim"));
                    }
                    mc.interactionManager.attackEntity(mc.player, entity);
                    mc.player.swingHand(Hand.MAIN_HAND);
                    entity1 = entity;
                    break;
                }
            }
        }
        return entity1;
    }

    public BlockPos breakFire() {

        Queue<BlockPos> hitLocations = new PriorityQueue<>();
        //TODO FIX WITH SETTING
        for (int x = -4 + mc.player.getBlockX(); x < 4 + mc.player.getBlockX(); x++) {
            for (int y = -4 + mc.player.getBlockY(); y < 4 + mc.player.getBlockY(); y++) {
                for (int z = -4 + mc.player.getBlockZ(); z < 4 + mc.player.getBlockZ(); z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (mc.world.getBlockState(pos).getBlock().equals(Blocks.FIRE) || mc.world.getBlockState(pos).getBlock().equals(Blocks.SOUL_FIRE))
                        hitLocations.add(pos);
                }
            }
        }
        if (hitLocations.isEmpty()) return null;

        BlockPos blockPos = hitLocations.poll();
        if (!rotaionsMode.getValue().equalsIgnoreCase("none")) {
            rotationSystem.rotate(blockPos.toCenterPos(), rotaionsMode.getValue().equalsIgnoreCase("sim"));
        }
        mc.player.swingHand(Hand.MAIN_HAND);
        mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.UP));
        return blockPos;


    }

    @Subscribe
    public void outGoingPackets(PacketEvent.Send event) {
        if (!(event.getPacket() instanceof PlayerMoveC2SPacket)) return;
        if (rotationSystem.getAction().equals(RotationSystem.ACTION.Rotating)) {
            rotationSystem.modifyPacket(((PlayerMoveC2SPacket) event.getPacket()));
            rotationSystem.setAction(RotationSystem.ACTION.Nothing);
        }
    }

    public void onDisable() {
        if (cactus.getValue()) CactusBlock.COLLISION_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);
        if (fire.getValue().equalsIgnoreCase("Avoid")) AbstractFireBlock.BASE_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

        rotationSystem.forceStop();
    }

    @Subscribe
    public void renderWorld(RenderWorldEvent event) {
        if (renderPosFire != null) {
            AreteClient.render3DManager.postRender(Render3DManager.RenderMode.BlockOutline, new Box(renderPosFire), renderPosFire.toCenterPos(), event, ColorUtils.convertAlpha(AreteClient.getClientColor(), 255), "AntiVoidFireOutline", 500f);
            AreteClient.render3DManager.postRender(Render3DManager.RenderMode.SolidBlock, new Box(renderPosFire), renderPosFire.toCenterPos(), event, ColorUtils.convertAlpha(AreteClient.getClientColor(), 125), "AntiVoidFireSolid", 500f);
        }

        if (renderEntityShulkerBullet != null) {
            AreteClient.render3DManager.postRender(Render3DManager.RenderMode.BlockOutline, renderEntityShulkerBullet, event, ColorUtils.convertAlpha(AreteClient.getClientColor(), 255), "AntiVoidShulkerOutline", 500f);
            AreteClient.render3DManager.postRender(Render3DManager.RenderMode.SolidBlock, renderEntityShulkerBullet, event, ColorUtils.convertAlpha(AreteClient.getClientColor(), 125), "AntiVoidShulkerSolid", 500f);
        }

        if (renderEntityFireBall != null) {
            AreteClient.render3DManager.postRender(Render3DManager.RenderMode.BlockOutline, renderEntityFireBall, event, ColorUtils.convertAlpha(AreteClient.getClientColor(), 255), "AntiVoidFireBallOutline", 500f);
            AreteClient.render3DManager.postRender(Render3DManager.RenderMode.SolidBlock, renderEntityFireBall, event, ColorUtils.convertAlpha(AreteClient.getClientColor(), 125), "AntiVoidFireBallSolid", 500f);
        }


    }

    public void onEnable() {

    }

}
