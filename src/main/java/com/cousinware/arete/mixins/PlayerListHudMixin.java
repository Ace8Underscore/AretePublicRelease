package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.module.misc.ExtraTab;
import com.cousinware.arete.module.player.Tracker;
import com.cousinware.arete.utils.rendering.nvg.string.RevealingString;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerListHud.class)
public class PlayerListHudMixin {

    @Unique
    RevealingString trackingText;
    @Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
    public void getPlayerName(PlayerListEntry playerListEntry, CallbackInfoReturnable<Text> info) {

        if (trackingText == null) trackingText = new RevealingString("Tracking");
        //Tracker
        String name = playerListEntry.getProfile().getName();
        if (AreteClient.moduleManager.getModuleByName("Tracker").isEnabled() && !Tracker.trackingNames.isEmpty()) {
            if (Tracker.trackingNames.contains(name)) {
                info.setReturnValue(Text.of(Formatting.RED + "[" + (Tracker.tabDisplay.getValue().equalsIgnoreCase("Normal") ? trackingText.getDisplayString() : "T") + "]" + name));
            }
            trackingText.getTextAndTick();
        }

    }

    @ModifyConstant(constant = @Constant(intValue = 20), method = "render")
    private int modifyRow(int constant) {
        Module module = AreteClient.moduleManager.getModuleByName("ExtraTab");
        return module.isEnabled() ? ExtraTab.rowLength.getValue() : constant;
    }

    @Inject(method = "renderLatencyIcon", at = @At("HEAD"), cancellable = true)
    private void showPing(DrawContext context, int width, int x, int y, PlayerListEntry entry, CallbackInfo ci) {
        if (!ExtraTab.showPing.getValue()) ci.cancel();
    }


    //meteor is gay and i could care less to fix
//    @ModifyConstant(constant = @Constant(longValue = 80L), method = "collectPlayerEntries")
//    private long modifyCount(long count) {
//        Module module = AreteClient.moduleManager.getModuleByName("ExtraTab");
//
//        return module.isEnabled() ? ExtraTab.playersShown.getValue() : count;
//    }


}