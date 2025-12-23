package com.cousinware.arete.utils.game;

import net.minecraft.client.util.InputUtil;

public class InputUtils {

    public static String convertScanKeyToLetter(int key, int modifiers) {
        try {
            String translation = String.valueOf(InputUtil.fromKeyCode(key, 0).getLocalizedText());
            String s = translation.split("\\{")[1].substring(0, translation.split("\\{")[1].length() - 1).toLowerCase();
            if (s.contains("space")) return " ";

            if (modifiers == 1) s = s.toUpperCase();
            if (s.length() > 2) return "";
            return s;
        } catch (Exception ignored) {
        }
        return "";
    }
}
