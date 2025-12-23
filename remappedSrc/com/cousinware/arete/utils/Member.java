package com.cousinware.arete.utils;

import com.cousinware.arete.client.AreteClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Member {

    String name;
    ArrayList<Rank> rank = new ArrayList<>();
    ArrayList<String> aliases = new ArrayList<>();
    Formatting mainColor;


    public Member(String name, Rank rank, @Nullable String[] aliases) {
        this.name = name;
        this.rank.add(rank);
        this.mainColor = getRankColor(rank);
        this.aliases.add(name);
        if (aliases != null) {

        }
    }

    public Member(String name, String[] rank, @Nullable String[] aliases) {
        this.name = name;
        this.mainColor = getRankColor(getRank(rank[0]));
        for (String s : rank) {
            this.rank.add(getRank(s));
        }
        this.aliases.add(name);
        if (aliases != null) {
            this.aliases.addAll(Arrays.asList(aliases));
        }
    }

    public Formatting getMainColor() {
        return mainColor;
    }


    public ArrayList<Rank> getRanks() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public boolean isAlt() {
        String accountName = MinecraftClient.getInstance().player.getName().getString();
        return !Objects.equals(accountName, name);
    }

    @Override
    public String toString() {
        String specialValue = "";
        if (!this.rank.contains(Rank.Unknown)) {
            //was high when this was written


            for (int i = 0; i < rank.size(); i++) {
                specialValue += getRankColor(rank.get(i)) + rank.get(i).toString() + Formatting.GRAY;
                if (rank.size() > 1 && i != rank.size() - 1) specialValue += ", ";
            }
        }






        return  "Rank: " + specialValue + "  ->  Member: " + this.name; //+ "  -> Aliases: " + aliases;
    }

    @Nullable
    public static Member getMember(String name) {
        if (name == null) return null;
        for (Member member : AreteClient.members) {
            if (name.equalsIgnoreCase(member.getName())) return member;
            for (String alias: member.aliases) {
                if (name.equalsIgnoreCase(alias)) return member;
            }
        }
        return null;
    }

    public static Formatting getRankColor(Rank r) {
        return switch (r) {
            case Rank.InnerCircle -> Formatting.DARK_RED;
            case Rank.Omega -> Formatting.GOLD;
            case Rank.Delta -> Formatting.BLUE;
            case Rank.Zeta -> Formatting.AQUA;
            case Rank.Gamma -> Formatting.GREEN;
            case Rank.Alumni -> Formatting.YELLOW;
            case Rank.PvPChief -> Formatting.WHITE;
            case Rank.Dev -> Formatting.LIGHT_PURPLE;
            case Rank.GoonSquad -> Formatting.RED ;
            case Rank.Ally -> Formatting.GRAY;
            default -> Formatting.WHITE;
        };
    }

    public static Rank getRank(String s) {
        return switch (s) {
            case "InnerCircle" -> Rank.InnerCircle;
            case "Omega" -> Rank.Omega;
            case "Delta" -> Rank.Delta;
            case "Zeta" -> Rank.Zeta;
            case "Gamma" -> Rank.Gamma;
            case "Alumni" -> Rank.Alumni;
            case "PvPChief" -> Rank.PvPChief;
            case "Dev" -> Rank.Dev;
            case "GoonSquad" -> Rank.GoonSquad;
            case "Ally" -> Rank.Ally;
            default -> Rank.Unknown;
        };

    }


    public enum Rank {
        //main ranks
        InnerCircle,
        Omega,
        Delta,
        Zeta,
        Gamma,
        Alumni,
        Unknown,
        Ally,


        //fun ranks

        PvPChief,
        Dev,
        GoonSquad
    }
}
