package com.cousinware.arete.module.world;

import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.managers.RotationManager;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.Timer;
import com.cousinware.arete.utils.game.BlockInteractionHelper;
import com.cousinware.arete.utils.rotations.Rotation;
import com.cousinware.arete.utils.rotations.RotationSystem;
import com.cousinware.arete.utils.settings.IntSetting;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

import java.util.ArrayList;

public class AutoLight extends Module implements Rotation {

    IntSetting lightLevel = new IntSetting();
    IntSetting delay = new IntSetting();
    IntSetting range = new IntSetting();
    ModeSetting rotationsMode = new ModeSetting();
    Timer timer;
    RotationSystem rotationSystem;

    public AutoLight() {
        super("AutoLight", Category.World, -1, "Places light sources in hand to light up large areas");
        lightLevel.setName("LightLevel").setMin(0).setMax(14).setValue(7).build(this);
        delay.setName("Delay").setMin(0).setMax(20).setValue(2).build(this).setModifyAction(() -> {
            timer.setDelay(delay.getValue());
        });
        range.setName("Range").setMin(0).setMax(6).setValue(4).build(this);
        rotationsMode.setName("Rotation").setModes("None", "Old", "Sim").setValue("Sim").build(this);
        timer = new Timer(delay.getValue() * 50);

    }

    public void onEnable() {
        rotationSystem = new RotationSystem();
    }

    public void onDisable() {
        rotationSystem.forceStop();
    }


    public void onUpdate() {

        if (!timer.canTick()) {
            return;
        }
        ArrayList<BlockPos> possiblePlacePoses = BlockInteractionHelper.getBlocksAroundPlayer(range.getValue(), 1);
        BlockPos bestPos = null;
        for (BlockPos pos : possiblePlacePoses) {
            if (!mc.world.getBlockState(pos.up()).isAir() || mc.world.getBlockState(pos).isAir()) continue;
            if (mc.player.getPos().distanceTo(pos.toCenterPos()) > range.getValue()) continue;
            int blockLight = mc.world.getLightLevel(LightType.BLOCK, pos.up());
            if (blockLight <= lightLevel.getValue()) {
                bestPos = pos;
                break;
            }
        }
        if (bestPos != null) {
            RotationManager.Rotation rotation = BlockInteractionHelper.placeBlock(bestPos.up(), bestPos.up().toCenterPos());
            if (rotation == null || rotationsMode.getValue().equalsIgnoreCase("none")) return;
            rotationSystem.setRotation(rotation, rotationsMode.getValue().equalsIgnoreCase("sim"));
        }
    }

    @Override
    @Subscribe
    public void packetListener(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet) {
            if (rotationSystem.getAction().equals(RotationSystem.ACTION.Rotating)) {
                rotationSystem.modifyPacket(packet);
                rotationSystem.setAction(RotationSystem.ACTION.Nothing);
            }

        }
    }
}
