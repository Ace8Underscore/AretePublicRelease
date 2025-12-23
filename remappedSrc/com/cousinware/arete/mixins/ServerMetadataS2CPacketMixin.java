package com.cousinware.arete.mixins;

import net.minecraft.network.packet.s2c.play.ServerMetadataS2CPacket;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerMetadataS2CPacket.class)
public class ServerMetadataS2CPacketMixin {


//    @Inject(method = "isSecureChatEnforced", at = @At("HEAD"), cancellable = true)
//    public void getReportButtonTooltip(CallbackInfoReturnable<Boolean> cir) {
//        if (NoRender.noChatWarning.getValue() && AreteClient.moduleManager.getModuleByName("NoRender").isEnabled())
//            cir.setReturnValue(true);
//    }

}
