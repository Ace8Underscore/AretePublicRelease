package com.cousinware.arete.module.misc;

import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.game.BlockInteractionHelper;
import com.cousinware.arete.utils.game.InventoryUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoIgnite extends Module {

    BlockHitResult lightLocation = null;

    public AutoIgnite() {
        super("AutoIgnite", Category.Misc, -1);
    }

    @Override
    public void onUpdate() {
        if (lightLocation != null) {
            InventoryUtils.HotBarTask inventoryTask = new InventoryUtils.HotBarTask(true, Items.FLINT_AND_STEEL);

            BlockPos pos = BlockInteractionHelper.findBlockWithin(Blocks.TNT, lightLocation.getBlockPos(), 2);
            if (pos != null) {
                BlockHitResult blockHitResult = new BlockHitResult(pos.toCenterPos(), Direction.UP, pos, true);
                //if (!mc.player.isSneaking()) mc.getNetworkHandler().sendPacket(new PlayerInputC2SPacket(new PlayerInput(false, false, false, false, false, true, false)));
                mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, blockHitResult, 0));
                //if (!mc.player.isSneaking()) mc.getNetworkHandler().sendPacket(new PlayerInputC2SPacket(new PlayerInput(false, false, false, false, false, false, false)));
            }

            inventoryTask.swapBack(true);
            lightLocation = null;
        }
    }


    @Subscribe
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerInteractBlockC2SPacket) {
            if (mc.player.getStackInHand(((PlayerInteractBlockC2SPacket) event.getPacket()).getHand()).getItem().equals(Items.TNT))
                lightLocation = ((PlayerInteractBlockC2SPacket) event.getPacket()).getBlockHitResult();
        }
    }
}
