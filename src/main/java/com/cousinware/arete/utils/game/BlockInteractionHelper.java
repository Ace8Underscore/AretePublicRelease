package com.cousinware.arete.utils.game;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.managers.RotationManager;
import com.cousinware.arete.utils.MinecraftInterface;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class BlockInteractionHelper implements MinecraftInterface {

    public static ArrayList<BlockPos> getPosWithinXZ(BlockPos pos1, BlockPos pos2) {
        int x1 = Math.min(pos1.getX(), pos2.getX());
        int x2 = Math.max(pos1.getX(), pos2.getX()) + 1;
        int z1 = Math.min(pos1.getZ(), pos2.getZ());
        int z2 = Math.max(pos1.getZ(), pos2.getZ()) + 1;

        ArrayList<BlockPos> poses = new ArrayList<>();

        for (int i = x1; i < x2; i++) {
            for (int j = z1; j < z2; j++) {
                poses.add(new BlockPos(i, pos1.getY(), j));
            }
        }
        return poses;
    }

    public static ArrayList<BlockPos> getPosWithinXYZ(BlockPos pos1, BlockPos pos2) {
        int x1 = Math.min(pos1.getX(), pos2.getX());
        int x2 = Math.max(pos1.getX(), pos2.getX()) + 1;
        int y1 = Math.min(pos1.getY(), pos2.getY());
        int y2 = Math.max(pos1.getY(), pos2.getY()) + 1;
        int z1 = Math.min(pos1.getZ(), pos2.getZ());
        int z2 = Math.max(pos1.getZ(), pos2.getZ()) + 1;

        ArrayList<BlockPos> poses = new ArrayList<>();

        for (int i = x1; i < x2; i++) {
            for (int k = y1; k < y2; k++) {
                for (int j = z1; j < z2; j++) {
                    poses.add(new BlockPos(i, k, j));
                }
            }
        }
        return poses;
    }

    public static ArrayList<BlockPos> getBlocksAroundPlayer(int radius, int yRange) {
        BlockPos pos = mc.player.getBlockPos();
        int x1 = pos.getX() - radius;
        int x2 = pos.getX() + radius;
        int y1 = pos.getY() - yRange;
        int y2 = pos.getY() + yRange;
        int z1 = pos.getZ() - radius;
        int z2 = pos.getZ() + radius;

        ArrayList<BlockPos> poses = new ArrayList<>();
        for (int i = x1; i < x2; i++) {
            for (int k = y1; k < y2; k++) {
                for (int j = z1; j < z2; j++) {
                    poses.add(new BlockPos(i, k, j));
                }
            }
        }
        return poses;
    }

    @Nullable
    public static BlockPos findBlockWithin(Block searchBlock, BlockPos pos, int searchRadius) {

        boolean debug = false;
        for (int x = -searchRadius; x < searchRadius; x++) {
            for (int y = -searchRadius; y < searchRadius; y++) {
                for (int z = -searchRadius; z < searchRadius; z++) {
                    BlockPos currentSearchPos = pos.add(x, y, z);
                    Block block = mc.world.getBlockState(currentSearchPos).getBlock();
//                    if (debug) {
//                        Box bb = Renderer3D.getBB(currentSearchPos);
//                        AreteClient.render3DManager.postRender(Render3DManager.RenderMode.BlockOutline, bb, bb.getCenter(), null, Color.ORANGE, "Debug" + x + " " + y + " " + z, 250);
//                        if (block.equals(searchBlock)) Command.sendClientSideMessage("Found", false);
//                    }
                    if (block.equals(searchBlock)) return currentSearchPos;
                }
            }
        }
        return null;
    }

    public static boolean canPlaceBlock(BlockPos pos) {
        if (!mc.world.getBlockState(pos).isAir() && !mc.world.getBlockState(pos).isLiquid()) return false;

        for (Direction direction : Direction.values()) {
            if (!mc.world.getBlockState(pos.add(direction.getVector())).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(pos.add(direction.getVector())).getBlock().equals(Blocks.WATER) && !mc.world.getBlockState(pos.add(direction.getVector())).getBlock().equals(Blocks.LAVA))
                return true;
        }

        return false;
    }

    public static BlockPos vec3dToPos(Vec3d vec3d) {
        int offsetX = mc.player.getX() < 0 ? -1 : 0;
        int offsetZ = mc.player.getZ() < 0 ? -1 : 0;
        return new BlockPos((int) vec3d.getX() + offsetX, (int) vec3d.getY(), (int) vec3d.getZ() + offsetZ);
    }

    public static RotationManager.Rotation placeBlock(BlockPos pos, Vec3d vec3d) {

        ArrayList<Direction> directions = new ArrayList<>();
        ArrayList<Packet> quePackets = new ArrayList<>();
        Direction placeDirection = null;
        BlockPos newPlaceSpot = null;
        directions = new ArrayList<>();
        for (Direction direction : Direction.values()) {

            if (!MinecraftClient.getInstance().world.getBlockState(pos.add(direction.getVector())).isAir() && !MinecraftClient.getInstance().world.getBlockState(pos.add(direction.getVector())).isLiquid()) {


                directions.add(direction.getOpposite());
                placeDirection = direction.getOpposite();
                newPlaceSpot = pos.add(direction.getVector());
                if (placeDirection != null && newPlaceSpot != null) break;
            }

        }
        if (placeDirection == null || newPlaceSpot == null) return null;
        Vec3d vec3d1 = newPlaceSpot.toCenterPos();

        //TODO change to packet to fix with silent switch hands
        // MinecraftClient.getInstance().player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(vec3d1, placeDirection, newPlaceSpot,  true), 0));
        MinecraftClient.getInstance().interactionManager.interactBlock(MinecraftClient.getInstance().player, Hand.MAIN_HAND, new BlockHitResult(vec3d1, placeDirection, newPlaceSpot, false));
        //Command.sendClientSideMessage("VEC:" + vec3d1 + "||||| \n" + "DIRECTION:" + placeDirection + "||||| \n" + "NEWBLOCKPOS:" + newPlaceSpot);
        //Command.sendClientSideMessage(placeDirection.toString());
        //Command.sendClientSideMessage("placeDirection: " + placeDirection + "|| BlockPos: " + pos + "|| Vec3d: " + pos.toCenterPos().toString());
        return new RotationManager.Rotation((float) AreteClient.rotationManager.getYaw(vec3d1, true), (float) AreteClient.rotationManager.getPitch(vec3d1, true));
    }

    public static RotationManager.Rotation placeBlockInAir(BlockPos pos, boolean useOffhand) {

        ArrayList<Direction> directions = new ArrayList<>();
        ArrayList<Packet> quePackets = new ArrayList<>();
        Direction placeDirection = null;
        BlockPos newPlaceSpot = null;
        directions = new ArrayList<>();
        int offsetX = mc.player.getX() > 0 ? -1 : 0;
        int offsetZ = mc.player.getZ() > 0 ? -1 : 0;

        for (Direction direction : Direction.values()) {


            directions.add(direction.getOpposite());
            placeDirection = direction.getOpposite();
            newPlaceSpot = pos.add(direction.getVector().add(direction.getOpposite().getVector()));
            //newPlaceSpot.subtract(new Vec3i(offsetX, 0, offsetZ));


        }
        if (placeDirection == null || newPlaceSpot == null) return null;
        //Vec3d vec3d1 = new Vec3d(newPlaceSpot.getX(), newPlaceSpot.getY(), newPlaceSpot.getZ() );

        //TODO change to packet to fix with silent switch hands
        // MinecraftClient.getInstance().player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(vec3d1, placeDirection, newPlaceSpot,  true), 0));
        //mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(useOffhand ? Hand.OFF_HAND : Hand.MAIN_HAND, new BlockHitResult(newPlaceSpot.toCenterPos(), placeDirection, newPlaceSpot, true), InventoryUtils.getSequence()));

        MinecraftClient.getInstance().interactionManager.interactBlock(MinecraftClient.getInstance().player, useOffhand ? Hand.OFF_HAND : Hand.MAIN_HAND, new BlockHitResult(newPlaceSpot.toCenterPos(), placeDirection, newPlaceSpot, true));

        //Command.sendClientSideMessage("VEC:" + vec3d1 + "||||| \n" + "DIRECTION:" + placeDirection + "||||| \n" + "NEWBLOCKPOS:" + newPlaceSpot);
        //Command.sendClientSideMessage(placeDirection.toString());
        //Command.sendClientSideMessage("placeDirection: " + placeDirection + "|| BlockPos: " + pos + "|| Vec3d: " + pos.toCenterPos().toString());
        return new RotationManager.Rotation((float) AreteClient.rotationManager.getYaw(newPlaceSpot.toCenterPos(), true), (float) AreteClient.rotationManager.getPitch(newPlaceSpot.toCenterPos(), true));
    }

    public static void placeBlockFix(RotationManager.Rotation rotaion) {
        MinecraftClient.getInstance().interactionManager.interactBlock(MinecraftClient.getInstance().player, Hand.MAIN_HAND, rotaion.getHitResult());

    }

    public static RotationManager.Rotation placeBlockRecursive(BlockPos pos, int interval) {

        ArrayList<Direction> directions = new ArrayList<>();
        Direction[] allDirections = new Direction[]{Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        Direction placeDirection = null;
        BlockPos newPlaceSpot = null;
        if (interval == allDirections.length) {

            return null;
        }
        directions = new ArrayList<>();
        if (interval != -1) pos = pos.add(allDirections[interval].getVector());

        for (Direction direction : allDirections) {
            if (direction.equals(Direction.UP)) continue;
            //if (interval != -1 && direction.equals(allDirections[interval].getOpposite())) continue;
            if (!MinecraftClient.getInstance().world.getBlockState(pos.add(direction.getVector())).isAir() && !MinecraftClient.getInstance().world.getBlockState(pos.add(direction.getVector())).isLiquid()) {


                directions.add(direction.getOpposite());
                placeDirection = direction.getOpposite();
                newPlaceSpot = pos.add(direction.getVector());
                Vec3d vec3d1 = new Vec3d(newPlaceSpot.getX() + .5f, newPlaceSpot.getY(), newPlaceSpot.getZ() + .5f);

                //AreteClient.packetManager.queuePacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(vec3d1, placeDirection, newPlaceSpot,  true), 0));
                //MinecraftClient.getInstance().player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(vec3d1, placeDirection, newPlaceSpot,  true), 10));
                //MinecraftClient.getInstance().interactionManager.interactBlock(MinecraftClient.getInstance().player, Hand.MAIN_HAND, new BlockHitResult(vec3d1, placeDirection, newPlaceSpot,  true));
                //Command.sendClientSideMessage("VEC:" + vec3d1 + "||||| \n" + "DIRECTION:" + placeDirection + "||||| \n" + "NEWBLOCKPOS:" + newPlaceSpot);
                //Command.sendClientSideMessage(placeDirection.toString());
                //Command.sendClientSideMessage("placeDirection: " + placeDirection + "|| BlockPos: " + pos + "|| Vec3d: " + pos.toCenterPos().toString());
                return new RotationManager.Rotation((float) AreteClient.rotationManager.getYaw(vec3d1, true), (float) AreteClient.rotationManager.getPitch(vec3d1, true), new BlockHitResult(vec3d1, placeDirection, newPlaceSpot, true));


            } //else Command.sendClientSideMessage("skipping over " + direction + "   || POS" + pos + "|||| INTERVAL" + interval);

        }
        //TODO make && not ||
        //System.out.println("Recursive check on " + allDirections[interval] + "    ||" + allDirections[interval].getVector());
        return placeBlockRecursive(interval == -1 ? pos : pos.add(-allDirections[interval].getVector().getX(), -allDirections[interval].getVector().getY(), -allDirections[interval].getVector().getZ()), interval == -1 ? 0 : interval + 1);


        //TODO change to packet to fix with silent switch hands
        // MinecraftClient.getInstance().player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(vec3d1, placeDirection, newPlaceSpot,  true), 0));
        //Command.sendClientSideMessage("VEC:" + vec3d1 + "||||| \n" + "DIRECTION:" + placeDirection + "||||| \n" + "NEWBLOCKPOS:" + newPlaceSpot);
        //Command.sendClientSideMessage(placeDirection.toString());
        //Command.sendClientSideMessage("placeDirection: " + placeDirection + "|| BlockPos: " + pos + "|| Vec3d: " + pos.toCenterPos().toString());

    }
}
