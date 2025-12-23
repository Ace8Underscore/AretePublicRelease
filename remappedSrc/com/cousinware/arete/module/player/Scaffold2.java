package com.cousinware.arete.module.player;

import com.cousinware.arete.command.Command;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.managers.RotationManager2;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.BlockInteractionHelper;
import com.cousinware.arete.utils.rotations.Rotation;
import com.cousinware.arete.utils.rotations.RotationSystem2;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class Scaffold2 extends Module implements Rotation {

    RotationSystem2 rotationSystem;
    ModeSetting rotaionsMode = new ModeSetting();

    Vec3d targetPlaceBlock;
    BlockPos pos;

    public Scaffold2() {
        super("Scaffold2", Category.Player, -1, "test");
        rotaionsMode.setName("Rotation").setModes("None", "Old", "Sim").setValue("Sim").build(this);
        rotationSystem = new RotationSystem2();

    }

    public void onUpdate() {
        if (pos != null) {
            RotationManager2.Rotaion  rot = BlockInteractionHelper.placeBlockRecursive(pos, -1);
            placeBlock(rot);
            BlockHitResult blockHitResult = new BlockHitResult(pos.toCenterPos(), Direction.UP, pos, false);
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, blockHitResult, 1));
            //if (!BlockInteractionHelper.canPlaceBlock(pos)) pos = null;
        }





        targetPlaceBlock = getTargetPlaceBlock();
        if (targetPlaceBlock == null) return;
        pos = BlockInteractionHelper.vec3dToPos(targetPlaceBlock);



        RotationManager2.Rotaion  rot = BlockInteractionHelper.placeBlockRecursive(pos, -1);
        if (rot == null) return;//no valid ways to place a block in this location
        if (!rotaionsMode.getValue().equalsIgnoreCase("none")) {
            rotationSystem.rotate(rot.getHitResult().getPos(), !mc.player.isFallFlying() && rotaionsMode.getValue().equalsIgnoreCase("sim"));
        }


        //place block

    }

    public void placeBlock(RotationManager2.Rotaion rotaion) {


        if (rotaion == null) return;
        BlockInteractionHelper.placeBlockFix(rotaion);
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    @Subscribe
    @Override
    public void packetListener(PacketEvent.Send event) {
        if (!(event.getPacket() instanceof PlayerMoveC2SPacket)) return;
        if (rotationSystem.getAction().equals(RotationSystem2.ACTION.Rotating)) {
            rotationSystem.modifyPacket(((PlayerMoveC2SPacket) event.getPacket()));
            rotationSystem.setAction(RotationSystem2.ACTION.Nothing);
        }
    }

    @Nullable
    public Vec3d getTargetPlaceBlock() {
        Vec3d vec3d = mc.player.getPos().add(0, -1, 0);
        BlockPos pos = BlockInteractionHelper.vec3dToPos(vec3d);
        if (BlockInteractionHelper.canPlaceBlock(pos)) {
            return vec3d;
        }
        return null;
    }
}
