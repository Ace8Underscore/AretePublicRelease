package com.cousinware.arete.module.misc;

import com.cousinware.arete.command.Command;
import com.cousinware.arete.events.event.LoadingIntoGameEvent;
import com.cousinware.arete.events.event.PacketEvent;
import com.cousinware.arete.events.event.PlayerJoinServerEvent;
import com.cousinware.arete.events.event.PlayerLeaveServerEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.Member;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class Welcomer extends Module {

    public Welcomer() {
        super("Welcomer", Category.Misc, -1, "Welcomes people to the game");
    }


    int joinedServerTicks = 0;

    public void onUpdate() {
        joinedServerTicks++;
    }

    @Subscribe
    public void playerJoinServer(PlayerJoinServerEvent event) {
        if (joinedServerTicks < 200) return;
        Member member = Member.getMember(event.getName());
        if (event.getName().equalsIgnoreCase(mc.player.getName().getString())) return;
        if (member == null) return;

        boolean containsPlayer = false;
        List<String> names = new ArrayList<>();
        mc.world.getPlayers().forEach(player -> names.add(player.getName().getString()));

        if (!names.contains(event.getName())) Command.sendClientSideMessage(Command.empPrefix(member) + member.getMainColor() + event.getName() + Formatting.GRAY + " joined the server ");
    }

    @Subscribe
    public void playerLeaveServer(PlayerLeaveServerEvent event) {
        Member member = Member.getMember(event.getName());
        if (member == null) return;
        Command.sendClientSideMessage(Command.empPrefix(member) + member.getMainColor() + event.getName() + Formatting.GRAY + " left the server ");
        //Command.sendClientSideMessage("invoked");
    }

    @Subscribe
    public void packetListener(PacketEvent.Send event) {
        if (event.getPacket() instanceof LoginHelloC2SPacket) {
            joinedServerTicks = 0;
        }

    }

    @Subscribe
    public void playerLoadIntoGame(LoadingIntoGameEvent event) {
        joinedServerTicks = 0;

    }

}
