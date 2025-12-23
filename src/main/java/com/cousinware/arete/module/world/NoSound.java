package com.cousinware.arete.module.world;

import com.cousinware.arete.events.event.SoundEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.Timer;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.BoolSettingContainer;
import com.google.common.eventbus.Subscribe;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class NoSound extends Module {

    public static BoolSetting enderman = new BoolSetting();

    public static BoolSettingContainer entities = new BoolSettingContainer();
    public static BoolSettingContainer blocks = new BoolSettingContainer();
    public static BoolSettingContainer player = new BoolSettingContainer();
    public static BoolSettingContainer misc = new BoolSettingContainer();
    BoolSetting netherPortal = new BoolSetting();
    BoolSetting endPortal = new BoolSetting();
    BoolSetting swingSword = new BoolSetting();
    BoolSetting elytra = new BoolSetting();
    BoolSetting firework = new BoolSetting();
    BoolSetting exp = new BoolSetting();
    BoolSetting queuePing = new BoolSetting();
    BoolSetting entityFallDamage = new BoolSetting();
    BoolSetting villager = new BoolSetting();
    BoolSetting pillager = new BoolSetting();
    BoolSetting warden = new BoolSetting();
    BoolSetting witch = new BoolSetting();
    BoolSetting phantom = new BoolSetting();
    BoolSetting fire = new BoolSetting();
    BoolSetting noSoundFix = new BoolSetting();


    //gets avg Distance of sounds from last second
    private double avgDistance = 0;
    private final Timer distanceTimer = new Timer(1000);
    private double avgDistanceTotal = 0;
    //gets amount of times avgDistance has been added to so we can / by this number for accurate distnace
    private int avgDistanceCount = 0;
    //ticcked so we reset data at end of sound loop
    boolean ticked = false;

    public NoSound() {
        super("NoSound", Category.World, -1);
        entities.setName("Entity").setValue(true).build(this);
        enderman.setName("Enderman").setValue(false).build(this, entities, "NoSoundEnderman");
        villager.setName("Villager").setValue(false).build(this, entities, "NoSoundVillager");
        pillager.setName("Pillager").setValue(false).build(this, entities, "NoSoundPillager");
        warden.setName("Warden").setValue(false).build(this, entities, "NoSoundWarden");
        phantom.setName("Phantom").setValue(true).build(this, entities, "NoSoundPhantom");
        witch.setName("Witch").setValue(false).build(this, entities, "NoSoundWitch");
        entityFallDamage.setName("EntityFallDamage").setValue(false).build(this, entities, "NoSoundEntityFallDamage");
        blocks.setName("Blocks").setValue(true).build(this);
        netherPortal.setName("NetherPortal").setValue(true).build(this, blocks, "NoSoundNetherPortal");
        endPortal.setName("EndPortal").setValue(false).build(this, blocks, "NoSoundEndPortal");
        player.setName("Player").setValue(true).build(this);
        elytra.setName("Elytra").setValue(true).build(this, player, "NoSoundElytra");
        swingSword.setName("SwingSword").setValue(false).build(this, player, "NoSoundSwingSword");
        firework.setName("Firework").setValue(false).build(this, player, "NoSoundFirework");
        exp.setName("Experience").setValue(false).build(this, player, "NoSoundEXP");
        fire.setName("Fire").setValue(false).build(this, player, "NoSoundFire");

        misc.setName("Misc").setValue(true).build(this);
        queuePing.setName("QueuePing").setValue(true).build(this, misc, "NoSoundQueuePing");

        noSoundFix.setName("NoSoundFix").setDescription("Fixes sounds not playing when sound cap reaches 247 sounds. You must leave and re-enter chunks for this to take into effect").setValue(true).build(this);

    }

    @Subscribe
    public void receiveSoundEvent(SoundEvent event) {
        if (mc.world == null && mc.player == null) return;

        if (noSoundFix.getValue()) {
            if (!distanceTimer.canTick() && Math.sqrt(mc.player.squaredDistanceTo(event.getPos())) < (mc.options.getViewDistance().getValue() * 16)) {
                avgDistanceCount++;
                //System.out.println(Math.sqrt(mc.player.squaredDistanceTo(event.getPos())));
                avgDistanceTotal += Math.sqrt(mc.player.squaredDistanceTo(event.getPos()));
                avgDistance = avgDistanceTotal / avgDistanceCount;

            } else {
                ticked = true;

            }
        }

        if (netherPortal.getValue() && event.getSoundEvent().equals(SoundEvents.BLOCK_PORTAL_AMBIENT.id()) || event.getSoundEvent().equals(SoundEvents.BLOCK_PORTAL_TRAVEL.id()) || event.getSoundEvent().equals(SoundEvents.BLOCK_PORTAL_TRIGGER.id())) {
            event.setCancelled(true);
            return;
        }
        if (endPortal.getValue() && event.getSoundEvent().equals(SoundEvents.BLOCK_END_PORTAL_SPAWN.id())) {
            event.setCancelled(true);
            return;
        }
        if (enderman.getValue() && (event.getSoundEvent().equals(SoundEvents.ENTITY_ENDERMAN_AMBIENT.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_ENDERMAN_SCREAM.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_ENDERMAN_STARE.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_ENDERMAN_DEATH.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_ENDERMAN_HURT.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_ENDERMAN_TELEPORT.id()))) {
            event.setCancelled(true);
            return;
        }
        if (swingSword.getValue() && (event.getSoundEvent().equals(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE.id()))) {
            event.setCancelled(true);
            return;
        }
        if (elytra.getValue() && event.getSoundEvent().equals(SoundEvents.ITEM_ELYTRA_FLYING.id())) {
            event.setCancelled(true);
            return;
        }
        if (firework.getValue() && event.getSoundEvent().equals(SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH.id())) {
            event.setCancelled(true);
            return;
        }
        if (exp.getValue() && event.getSoundEvent().equals(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP.id())) {
            event.setCancelled(true);
            return;
        }
        if (queuePing.getValue() && event.getSoundEvent().equals(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP.id()) && mc.world.getRegistryKey().equals(World.END) && mc.player.isSpectator()) {
            event.setCancelled(true);
            return;
        }
        if (entityFallDamage.getValue() && (event.getSoundEvent().equals(SoundEvents.ENTITY_HOSTILE_BIG_FALL.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_HOSTILE_SMALL_FALL.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_GENERIC_BIG_FALL.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_GENERIC_SMALL_FALL.id()))) {
            event.setCancelled(true);
            return;
        }
        if (villager.getValue() && (event.getSoundEvent().equals(SoundEvents.ENTITY_VILLAGER_AMBIENT.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_VILLAGER_NO.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_VILLAGER_YES.id()))) {
            event.setCancelled(true);
            return;
        }
        if (pillager.getValue() && (event.getSoundEvent().equals(SoundEvents.ENTITY_PILLAGER_AMBIENT.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_PILLAGER_DEATH.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_PILLAGER_CELEBRATE.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_PILLAGER_HURT.id()))) {
            event.setCancelled(true);
            return;
        }
        if (warden.getValue() && (event.getSoundEvent().equals(SoundEvents.ENTITY_WARDEN_SNIFF.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_WARDEN_AGITATED.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_WARDEN_AMBIENT.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_WARDEN_HEARTBEAT.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_WARDEN_ROAR.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_WARDEN_LISTENING_ANGRY.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_WARDEN_LISTENING.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_WARDEN_NEARBY_CLOSE.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_WARDEN_NEARBY_CLOSER.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_WARDEN_NEARBY_CLOSEST.id()))) {
            event.setCancelled(true);
            return;
        }
        if (witch.getValue() && (event.getSoundEvent().equals(SoundEvents.ENTITY_WITCH_AMBIENT.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_WITCH_DEATH.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_WITCH_HURT.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_WITCH_CELEBRATE.id()))) {
            event.setCancelled(true);
            return;
        }
        if (fire.getValue() && event.getSoundEvent().equals(SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE.id())) {
            event.setCancelled(true);
            return;
        }
        if (phantom.getValue() && (event.getSoundEvent().equals(SoundEvents.ENTITY_PHANTOM_AMBIENT.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_PHANTOM_SWOOP.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_PHANTOM_BITE.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_PHANTOM_DEATH.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_PHANTOM_FLAP.id()) || event.getSoundEvent().equals(SoundEvents.ENTITY_PHANTOM_HURT.id()))) {
            event.setCancelled(true);
        }


        //System.out.println(mc.getSoundManager().getSoundDevices().size());

        int soundCount = mc.getSoundManager().soundSystem.soundEngine.streamingSources.getSourceCount();
        if (noSoundFix.getValue() && soundCount > 100) {
            if (Math.sqrt(mc.player.squaredDistanceTo(event.getPos())) > avgDistance && !event.getSoundEvent().equals(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER.id())) {
                event.setCancelled(true);
            }

        }

        if (ticked) {
            ticked = false;
            avgDistance = 0;
            avgDistanceTotal = 0;
            avgDistanceCount = 0;
        }

    }
}
