package com.cousinware.arete.utils.game;

import com.cousinware.arete.utils.MinecraftInterface;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.world.chunk.WorldChunk;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

@Getter
public class EntityUtil implements MinecraftInterface {

    @Setter
    public static float tickDelta = 0;

    public static ArrayList<BlockEntity> getBlockEntities() {
        ArrayList<BlockEntity> list = new ArrayList<>();
        for (WorldChunk chunk : getLoadedChunks()) list.addAll(chunk.getBlockEntities().values());
        return list;
    }

    public static Entity getClosestEntity(Class<?> clazz) {
        Entity closestEnt = null;
        for (Entity loadedEnt : mc.world.getEntities()) {
            if (loadedEnt.getClass().isInstance(clazz) || clazz.isInstance(loadedEnt) || clazz.isInstance(loadedEnt.getClass())) {
                if (closestEnt == null) closestEnt = loadedEnt;
                if (mc.player.distanceTo(loadedEnt) < mc.player.distanceTo(closestEnt)) closestEnt = loadedEnt;
            }
        }
        return closestEnt;
    }

    public static ArrayList<WorldChunk> getLoadedChunks() {
        ArrayList<WorldChunk> chunkList = new ArrayList<>();
        int renderDistance = mc.options.getViewDistance().getValue();

        for (int x = -renderDistance; x <= renderDistance; x++) {
            for (int z = -renderDistance; z <= renderDistance; z++) {
                WorldChunk chunk = mc.world.getChunkManager().getWorldChunk((int) mc.player.getX() / 16 + x, (int) mc.player.getZ() / 16 + z);
                if (chunk != null) chunkList.add(chunk);
            }
        }
        return chunkList;
    }

    public static boolean isNeutralEntity(Entity entity) {
        return (entity instanceof IronGolemEntity || entity instanceof WolfEntity || entity instanceof ZombifiedPiglinEntity || entity instanceof EndermanEntity);
    }

    public static boolean isNeutralEntityHostile(Entity entity) {
        if (!isNeutralEntity(entity)) return false;

        if (entity instanceof IronGolemEntity) {
            return ((IronGolemEntity) entity).isUniversallyAngry(Objects.requireNonNull(mc.getServer()).getOverworld());
        } else if (entity instanceof WolfEntity) {
            return ((WolfEntity) entity).isUniversallyAngry(Objects.requireNonNull(mc.getServer()).getOverworld());

        } else if (entity instanceof ZombifiedPiglinEntity) {
            //
            return ((ZombifiedPiglinEntity) entity).hasAngerTime();
        } else if (entity instanceof EndermanEntity) {
            return ((EndermanEntity) entity).hasAngerTime();
        }

        return false;
    }

    public static Color getEntityColor(Entity entity) {
        if (entity instanceof HostileEntity) return Color.RED;
        if (entity instanceof PassiveEntity || entity instanceof FishEntity || entity instanceof AnimalEntity || entity instanceof SquidEntity || entity instanceof AmbientEntity)
            return Color.GREEN;
        return Color.ORANGE;
    }
}
