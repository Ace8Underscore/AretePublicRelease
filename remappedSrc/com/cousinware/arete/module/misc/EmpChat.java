package com.cousinware.arete.module.misc;

import com.cousinware.arete.events.event.RecieveMessageEvent;
import com.cousinware.arete.module.Module;
import com.cousinware.arete.utils.Member;
import com.cousinware.arete.utils.settings.BoolSetting;
import com.cousinware.arete.utils.settings.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class EmpChat extends Module {

    public static ModeSetting mode = new ModeSetting();
    public static BoolSetting verify = new BoolSetting();
    public static BoolSetting showRanks = new BoolSetting();

    public EmpChat() {
        super("EmpChat", Category.Misc, 3995745);
        verify.setName("Verify").setValue(true).build(this);
        showRanks.setName("ShowRanks").setValue(false).build(this);
        mode.setName("TextMode").setValue("<>").setModes("<>", "{}", "[]").build(this);
    }
    @Subscribe
    private void onMessageReceive(RecieveMessageEvent event) {

        String sender = getSender(event.getText().getString());
        if (Member.getMember(sender) == null) return;
        Text message = event.getText();
        Member member = Member.getMember(sender);


        Text prefix = Text.literal(mode.getValue().substring(0, 1) + Formatting.RED + "Ω" + Formatting.WHITE + mode.getValue().substring(1)).formatted(Formatting.WHITE);

        if (verify.getValue() && !mc.isInSingleplayer()) {
            if (!member.isAlt()) prefix = Text.of(prefix.getString() + "[" + Formatting.GREEN + "✔" + Formatting.WHITE + "]");
        }

        MutableText prefixDOS = Text.empty();
        if (showRanks.getValue()) member.getRanks().forEach(rank -> prefixDOS.append("<" + Member.getRankColor(rank) + rank.name() + Formatting.WHITE + ">"));

        message = Text.of(message.getString().substring(0, 1) + member.getMainColor() + message.getString().substring(1, sender.length() + 1));
        message = Text.empty().append(prefix).append(prefixDOS).append(message).append(event.getText().getString().substring(sender.length() + 1));
        event.setMessage(message);

    }

    private String getSender(String s) {
        //if (s == null) return "no";

        try {
            String sender;
            sender = s.split("<")[1];
            return sender.split(">")[0];
        }catch (Exception e) {

        }
        return s;
    }






}
