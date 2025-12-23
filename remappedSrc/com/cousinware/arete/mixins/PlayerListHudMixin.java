package com.cousinware.arete.mixins;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.command.Command;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.module.misc.ExtraTab;
import com.cousinware.arete.utils.Member;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
    public void getPlayerName(PlayerListEntry playerListEntry, CallbackInfoReturnable<Text> info) {

        if (AreteClient.moduleManager.getModuleByName("ExtraTab").isEnabled()) {
            String name = playerListEntry.getProfile().getName();
            Member member = Member.getMember(name);
            if (member != null) {
                info.setReturnValue(Text.of(Formatting.RED + Command.empPrefix(member) + member.getMainColor() + playerListEntry.getProfile().getName()));
            }
        }
    }

    @ModifyConstant(constant = @Constant(longValue = 80L), method = "collectPlayerEntries")
    private long modifyCount(long count) {
        Module module = AreteClient.moduleManager.getModuleByName("ExtraTab");

        return module.isEnabled() ? ExtraTab.playersShown.getValue() : count;
    }


}