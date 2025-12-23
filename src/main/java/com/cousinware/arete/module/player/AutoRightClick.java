package com.cousinware.arete.module.player;

import com.cousinware.arete.command.Command;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.Timer;
import com.cousinware.arete.utils.game.BlockInteractionHelper;
import com.cousinware.arete.utils.settings.IntSetting;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class AutoRightClick extends Module {

    IntSetting delayMs = new IntSetting();
    Timer timer = new Timer();
    HitResult result = null;

    public AutoRightClick() {
        super("AutoRightClick", Category.Player, -1, "Right clicks block you were looking at when turned on");
        delayMs.setValue(50).setMin(0).setMax(1000).setName("DelayMs").build(this).setModifyAction(() -> {
            timer.setDelay(delayMs.getValue());
        });
        timer.setDelay(delayMs.getValue());
    }

    public void onEnable() {
        if (!mc.crosshairTarget.getType().equals(HitResult.Type.BLOCK)) {

            Command.sendClientSideMessage("No Block Found!", false);
        } else if (mc.crosshairTarget.getType().equals(HitResult.Type.BLOCK)) {
            Command.sendClientSideMessage("Block Found " + mc.world.getBlockState(BlockInteractionHelper.vec3dToPos(mc.crosshairTarget.getPos())), false);
            result = mc.crosshairTarget;
        }
    }

    public void onDisable() {
        result = null;
    }

    public void onUpdate() {
        if (!timer.canTick()) return;
        if (result == null) return;
        BlockHitResult result1 = (BlockHitResult) result;
        mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result1, mc.world.pendingUpdateManager.incrementSequence().getSequence()));
    }
}
