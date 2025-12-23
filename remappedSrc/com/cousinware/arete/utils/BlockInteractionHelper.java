package com.cousinware.arete.utils;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.managers.RotationManager;
import com.cousinware.arete.managers.RotationManager2;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class BlockInteractionHelper implements MinecraftInterface{

    public static boolean canPlaceBlock(BlockPos pos) {
        if (!mc.world.getBlockState(pos).isAir() && !mc.world.getBlockState(pos).isLiquid()) return false;

        for (Direction direction : Direction.values()) {
            if (!mc.world.getBlockState(pos.add(direction.getVector())).getBlock().equals(Blocks.AIR) && !mc.world.getBlockState(pos.add(direction.getVector())).getBlock().equals(Blocks.WATER) && !mc.world.getBlockState(pos.add(direction.getVector())).getBlock().equals(Blocks.LAVA)) return true;
        }

        return false;
    }

    public static BlockPos vec3dToPos(Vec3d vec3d) {
        return new BlockPos((int)vec3d.getX(), (int)vec3d.getY(), (int)vec3d.getZ());
    }

    public static RotationManager.Rotaion placeBlock(BlockPos pos, Vec3d vec3d) {

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
            }

        }
        if (placeDirection == null || newPlaceSpot == null) return null;
        Vec3d vec3d1 = new Vec3d(newPlaceSpot.getX() + .5f, (int)newPlaceSpot.getY(), newPlaceSpot.getZ() + .5f);

        //TODO change to packet to fix with silent switch hands
       // MinecraftClient.getInstance().player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(vec3d1, placeDirection, newPlaceSpot,  true), 0));
        MinecraftClient.getInstance().interactionManager.interactBlock(MinecraftClient.getInstance().player, Hand.MAIN_HAND, new BlockHitResult(vec3d1, placeDirection, newPlaceSpot,  true));
        //Command.sendClientSideMessage("VEC:" + vec3d1 + "||||| \n" + "DIRECTION:" + placeDirection + "||||| \n" + "NEWBLOCKPOS:" + newPlaceSpot);
        //Command.sendClientSideMessage(placeDirection.toString());
        //Command.sendClientSideMessage("placeDirection: " + placeDirection + "|| BlockPos: " + pos + "|| Vec3d: " + pos.toCenterPos().toString());
        return new RotationManager.Rotaion((float) AreteClient.rotationManager.getYaw(vec3d1, true), (float) AreteClient.rotationManager.getPitch(vec3d1, true));
    }

    public static void placeBlockFix(RotationManager2.Rotaion rotaion) {
        MinecraftClient.getInstance().interactionManager.interactBlock(MinecraftClient.getInstance().player, Hand.MAIN_HAND, rotaion.getHitResult());

    }

    public static RotationManager2.Rotaion placeBlockRecursive(BlockPos pos, int interval) {

        ArrayList<Direction> directions = new ArrayList<>();
        Direction[] allDirections = new Direction[] {Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        Direction placeDirection = null;
        BlockPos newPlaceSpot = null;
        if (interval == allDirections.length) {

            return null;
        }
        directions = new ArrayList<>();
        if (interval != -1) pos = pos.add(allDirections[interval].getVector());

        for (Direction direction : allDirections) {
            if(direction.equals(Direction.UP)) continue;
            //if (interval != -1 && direction.equals(allDirections[interval].getOpposite())) continue;
            System.out.println(direction);
            if (!MinecraftClient.getInstance().world.getBlockState(pos.add(direction.getVector())).isAir() && !MinecraftClient.getInstance().world.getBlockState(pos.add(direction.getVector())).isLiquid()) {


                directions.add(direction.getOpposite());
                placeDirection = direction.getOpposite();
                newPlaceSpot = pos.add(direction.getVector());
                Vec3d vec3d1 = new Vec3d(newPlaceSpot.getX() + .5f, (int)newPlaceSpot.getY(), newPlaceSpot.getZ() + .5f);

                //AreteClient.packetManager.queuePacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(vec3d1, placeDirection, newPlaceSpot,  true), 0));
                //MinecraftClient.getInstance().player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(vec3d1, placeDirection, newPlaceSpot,  true), 10));
                //MinecraftClient.getInstance().interactionManager.interactBlock(MinecraftClient.getInstance().player, Hand.MAIN_HAND, new BlockHitResult(vec3d1, placeDirection, newPlaceSpot,  true));
                //Command.sendClientSideMessage("VEC:" + vec3d1 + "||||| \n" + "DIRECTION:" + placeDirection + "||||| \n" + "NEWBLOCKPOS:" + newPlaceSpot);
                //Command.sendClientSideMessage(placeDirection.toString());
                //Command.sendClientSideMessage("placeDirection: " + placeDirection + "|| BlockPos: " + pos + "|| Vec3d: " + pos.toCenterPos().toString());
                return new RotationManager2.Rotaion((float) AreteClient.rotationManager.getYaw(vec3d1, true), (float) AreteClient.rotationManager.getPitch(vec3d1, true), new BlockHitResult(vec3d1, placeDirection, newPlaceSpot,  true));


            } //else Command.sendClientSideMessage("skipping over " + direction + "   || POS" + pos + "|||| INTERVAL" + interval);

        }
        //TODO make && not ||
            //System.out.println("Recursive check on " + allDirections[interval] + "    ||" + allDirections[interval].getVector());
            return placeBlockRecursive(interval == -1 ? pos : pos.add(-allDirections[interval].getVector().getX(), -allDirections[interval].getVector().getY(), -allDirections[interval].getVector().getZ()),interval == -1 ? 0 : interval + 1 );


        //TODO change to packet to fix with silent switch hands
        // MinecraftClient.getInstance().player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(vec3d1, placeDirection, newPlaceSpot,  true), 0));
        //Command.sendClientSideMessage("VEC:" + vec3d1 + "||||| \n" + "DIRECTION:" + placeDirection + "||||| \n" + "NEWBLOCKPOS:" + newPlaceSpot);
        //Command.sendClientSideMessage(placeDirection.toString());
        //Command.sendClientSideMessage("placeDirection: " + placeDirection + "|| BlockPos: " + pos + "|| Vec3d: " + pos.toCenterPos().toString());

    }
}
