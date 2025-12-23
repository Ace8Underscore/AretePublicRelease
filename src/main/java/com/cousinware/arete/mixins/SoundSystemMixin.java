package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.events.event.SoundEvent;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"), cancellable = true)
    public void playSound(SoundInstance sound, CallbackInfo ci) {
        Vec3d pos = new Vec3d(sound.getX(), sound.getY(), sound.getZ());
        SoundEvent soundEvent = new SoundEvent(sound.getId(), pos);
        AreteClient.eventBus.post(soundEvent);
        if (soundEvent.isCancelled()) {
            ci.cancel();
        }
    }


    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;I)V", at = @At("HEAD"), cancellable = true)
    public void playSoundDelay(SoundInstance sound, int delay, CallbackInfo ci) {
        Vec3d pos = new Vec3d(sound.getX(), sound.getY(), sound.getZ());
        SoundEvent soundEvent = new SoundEvent(sound.getId(), pos);

        AreteClient.eventBus.post(soundEvent);
        if (soundEvent.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "playNextTick", at = @At("HEAD"), cancellable = true)
    public void playSoundDelay2(TickableSoundInstance sound, CallbackInfo ci) {
        Vec3d pos = new Vec3d(sound.getX(), sound.getY(), sound.getZ());
        SoundEvent soundEvent = new SoundEvent(sound.getId(), pos);
        AreteClient.eventBus.post(soundEvent);
        if (soundEvent.isCancelled()) {
            ci.cancel();
        }
    }
}
