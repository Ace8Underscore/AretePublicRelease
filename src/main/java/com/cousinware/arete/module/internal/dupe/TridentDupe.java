//package com.cousinware.arete.module.internal.dupe;
//
//
//import com.cousinware.arete.events.event.DisconnectEvent;
//import com.cousinware.arete.events.event.PacketEvent;
//import com.cousinware.arete.module.Module;
//import com.cousinware.arete.utils.settings.BoolSetting;
//import com.cousinware.arete.utils.settings.DoubleSetting;
//import com.google.common.eventbus.Subscribe;
//import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.network.packet.Packet;
//import net.minecraft.network.packet.c2s.play.*;
//import net.minecraft.screen.slot.SlotActionType;
//import net.minecraft.screen.sync.ItemStackHash;
//import net.minecraft.text.MutableText;
//import net.minecraft.text.Text;
//import net.minecraft.util.Formatting;
//import net.minecraft.util.Hand;
//import net.minecraft.util.Pair;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Direction;
//
//import java.util.*;
//
//public class TridentDupe extends Module {
//
//
//    public static DoubleSetting delay = new DoubleSetting();
//    public static BoolSetting dropTridents = new BoolSetting();
//    public static BoolSetting durabilityManagement = new BoolSetting();
//
//
//    private final Queue<Packet<?>> delayedPackets;
//    private final List<Pair<Long, Runnable>> scheduledTasks;
//    private final List<Pair<Long, Runnable>> scheduledTasks2;
//    private boolean cancel;
//
//    public TridentDupe() {
//        super("TridentDupe", Category.Internal, -1, "Dupes tridents Fuck You Bitch - Patched by paper");
//        this.delayedPackets = new LinkedList<>();
//
//        delay.setMin(0).setValue(5).setMax(10).setName("Delay").build(this);
//        dropTridents.setValue(true).setName("DropTrident").build(this);
//        durabilityManagement.setValue(true).setName("ManageDura").build(this);
//
//
//        this.cancel = true;
//        this.scheduledTasks = new ArrayList<Pair<Long, Runnable>>();
//        this.scheduledTasks2 = new ArrayList<Pair<Long, Runnable>>();
//    }
//
//    @Subscribe
//    private void onSendPacket(PacketEvent.Send event) {
/// /        if (event.getPacket() instanceof ClientTickEndC2SPacket || event.getPacket() instanceof PlayerMoveC2SPacket || event.getPacket() instanceof CloseHandledScreenC2SPacket) {
/// /            return;
/// /        }
//
//        if (event.getPacket() instanceof PlayerMoveC2SPacket || event.getPacket() instanceof CloseHandledScreenC2SPacket) {
//            return;
//        }
//        if (!(event.getPacket() instanceof ClickSlotC2SPacket) && !(event.getPacket() instanceof PlayerActionC2SPacket)) {
//            return;
//        }
//        if (!this.cancel) {
//            return;
//        }
//        MutableText packetStr = Text.literal(event.getPacket().toString()).formatted(Formatting.WHITE);
//        event.setCancelled(true);
//    }
//
//    public void onEnable() {
//        if (mc.player == null) {
//            return;
//        }
//        for (int i = 0; i < 9; ++i) {
//            if (mc.player.getInventory().getStack(i).getItem() != Items.TRIDENT) continue;
//            Integer n = mc.player.getInventory().getStack(i).getDamage();
//        }
//        PlayerInteractItemC2SPacket pckt = new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 10, -57.0f, 66.29f);
//        Int2ObjectOpenHashMap modifiedStacks = new Int2ObjectOpenHashMap();
//        modifiedStacks.put(3, mc.player.getInventory().getStack(mc.player.getInventory().getSelectedSlot()));
//        modifiedStacks.put(36, mc.player.getInventory().getStack(mc.player.getInventory().getSelectedSlot()));
//        ClickSlotC2SPacket packet = new ClickSlotC2SPacket(0, 15, 0, 0, SlotActionType.SWAP, new ItemStack(Items.AIR), modifiedStacks);
//        ClickSlotC2SPacket packet = new ClickSlotC2SPacket(0, 15, 0, 0, SlotActionType.SWAP, modifiedStacks, new ItemStack(Items.AIR));
//        this.scheduledTasks.clear();
//        this.dupe();
//    }
//
//    private void dupe() {
//        int delayInt = delay.getValue().intValue() * 100;
//        System.out.println(delayInt);
//        int lowestHotbarSlot = 0;
//        int lowestHotbarDamage = 1000;
//        for (int i = 0; i < 9; ++i) {
//            Integer currentHotbarDamage;
//            if (mc.player.getInventory().getStack(i).getItem() != Items.TRIDENT || lowestHotbarDamage <= (currentHotbarDamage = Integer.valueOf(mc.player.getInventory().getStack(i).getDamage())))
//                continue;
//            lowestHotbarSlot = i;
//            lowestHotbarDamage = currentHotbarDamage;
//        }
//        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
//        this.cancel = true;
//        int finalLowestHotbarSlot = lowestHotbarSlot;
//        this.scheduleTask(() -> {
//            this.cancel = false;
//            if (durabilityManagement.getValue().booleanValue() && finalLowestHotbarSlot != 0) {
//                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 44, 0, SlotActionType.SWAP, mc.player);
//                if (dropTridents.getValue().booleanValue()) {
//                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 44, 0, SlotActionType.THROW, mc.player);
//                }
//                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 36 + finalLowestHotbarSlot, 0, SlotActionType.SWAP, mc.player);
//            }
//            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 3, 0, SlotActionType.SWAP, mc.player);
//            PlayerActionC2SPacket packet2 = new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN, 0);
//            mc.getNetworkHandler().sendPacket(packet2);
//            if (dropTridents.getValue().booleanValue()) {
//                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 44, 0, SlotActionType.THROW, mc.player);
//            }
//            this.cancel = true;
//            this.scheduleTask2(this::dupe, delayInt);
//        }, delayInt);
//    }
//
//    public void scheduleTask(Runnable task, long delayMillis) {
//        long executeTime = System.currentTimeMillis() + delayMillis;
//        this.scheduledTasks.add((Pair<Long, Runnable>) new Pair(executeTime, task));
//    }
//
//    public void scheduleTask2(Runnable task, long delayMillis) {
//        long executeTime = System.currentTimeMillis() + delayMillis;
//        this.scheduledTasks2.add((Pair<Long, Runnable>) new Pair(executeTime, task));
//    }
//
//    @Override
//    public void onUpdate() {
//        Pair<Long, Runnable> entry;
//        long currentTime = System.currentTimeMillis();
//        Iterator<Pair<Long, Runnable>> iterator = this.scheduledTasks.iterator();
//        while (iterator.hasNext()) {
//            entry = iterator.next();
//            if (entry.getLeft() > currentTime) continue;
//            entry.getRight().run();
//            iterator.remove();
//        }
//        iterator = this.scheduledTasks2.iterator();
//        while (iterator.hasNext()) {
//            entry = iterator.next();
//            if (entry.getLeft() > currentTime) continue;
//            entry.getRight().run();
//            iterator.remove();
//        }
//    }
//
//    @Subscribe
//    private void onGameLeft(DisconnectEvent event) {
//        this.disable();
//    }
//
//}