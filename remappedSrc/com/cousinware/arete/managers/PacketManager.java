package com.cousinware.arete.managers;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.RotationSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;

import java.util.ArrayList;

public class PacketManager {

    ArrayList<Packet> quePackets = new ArrayList<>();
    public final MinecraftClient mc = MinecraftClient.getInstance();

    public PacketManager() {
        AreteClient.eventBus.register(this);
    }

    public void onPreUpdate() {
        //This is ran so at then end of every tick and everything has been calcualted we set rotation back to real MC one so next tick if not needed to be modified its fixed to where player is looking

    }

    public boolean shouldModifyPacket(Module module) {
        if (module.rotationSystem == null) {
            //Command.sendClientSideMessage("Packet For Module" + module.getName() + "  Was allowed to be changed");
            return true;
        }
        for (Module m : AreteClient.moduleManager.modules) {
            if (!m.isEnabled()) continue;
            if (m.rotationSystem == null) continue;
            if (!m.rotationSystem.getCurrentAction().equals(RotationSystem.ACTION.Rotating)) continue;
            if (module.priority < m.getPriority()) return false;
        }
        //Command.sendClientSideMessage("Packet For Module" + module.getName() + "  Was allowed to be changed");
        return true;
    }


    public void queuePacket(Packet packet) {
        quePackets.add(packet);
    }
}
