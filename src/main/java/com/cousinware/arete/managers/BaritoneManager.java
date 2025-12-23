package com.cousinware.arete.managers;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.pathing.goals.GoalNear;
import com.cousinware.arete.utils.MinecraftInterface;
import lombok.Getter;
import net.minecraft.util.math.BlockPos;

@Getter
public class BaritoneManager implements MinecraftInterface {

    public IBaritone baritoneAPI;

    public BaritoneManager() {
        baritoneAPI = BaritoneAPI.getProvider().createBaritone(mc);
    }

    //preferably range is reach range
    public void goNearPos(BlockPos pos, int range) {
        BlockPos pos1 = new BlockPos(pos.getX(), (int) mc.player.getY(), pos.getZ());
        baritoneAPI.getCustomGoalProcess().setGoalAndPath(new GoalNear(pos, range - 1));
    }

    public boolean isPathFinding() {
        return baritoneAPI.getPathingBehavior().isPathing();
    }
}
