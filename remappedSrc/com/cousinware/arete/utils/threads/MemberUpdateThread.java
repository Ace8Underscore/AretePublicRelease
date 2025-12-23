package com.cousinware.arete.utils.threads;

import com.cousinware.arete.client.AreteClient;
import com.cousinware.arete.utils.Member;
import com.cousinware.arete.utils.Timer;
import com.cousinware.arete.utils.Users;
import net.minecraft.client.MinecraftClient;

public class MemberUpdateThread implements Runnable {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    public static boolean enabled = false;
    public static String value;

    Timer timer = new Timer(2500);


    @Override
    public void run() {
        System.out.println(this.getClass().getName() + " Started");

        while (true) {
            if (!timer.canTick()) continue;
            update();



        }
    }

    public void update() {
        AreteClient.users = new Users();
        AreteClient.userList = AreteClient.users.getUsers();


        //UPDATE TITLE
        if (mc.player == null) return;

        Member member = Member.getMember(MinecraftClient.getInstance().player.getName().getString());

        if (member == null) return;

        AreteClient.title = member.getRanks().getFirst() + "  --  " + member.getName();



    }

}
