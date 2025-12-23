package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.player.AntiVoid;
import com.cousinware.arete.module.world.NoBlockRotation;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {

    @Shadow
    @Final
    private int luminance;

    @Inject(method = "blocksMovement", at = @At("HEAD"), cancellable = true)
    public void blocksMoveWeb(CallbackInfoReturnable<Boolean> cir) {
        if (AreteClient.moduleManager.getModuleByName("Avoid").isEnabled() && AntiVoid.web.getValue())
            cir.setReturnValue(false);

    }

    @Inject(method = "getRenderingSeed", at = @At("HEAD"), cancellable = true)
    public void disableSeedBasedOnPosition(BlockPos pos, CallbackInfoReturnable<Long> cir) {

        if (AreteClient.moduleManager.getModuleByName("BlockSpoof").isEnabled()) {
            if (NoBlockRotation.mode.getValue().equalsIgnoreCase("Constant")) cir.setReturnValue(42L);
            else if (NoBlockRotation.mode.getValue().equalsIgnoreCase("Randomize"))
                cir.setReturnValue(NoBlockRotation.trueRandomness());
            else cir.setReturnValue((long) ((pos.getX() + pos.getY() + pos.getZ())) + NoBlockRotation.randomLong);

        }
    }
}
