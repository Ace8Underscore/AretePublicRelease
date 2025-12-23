package com.cousinware.arete.utils;

import java.awt.*;

public class ColorUtils {

    public static Color convertAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static String[] colors() {
        return new String[]{"White", "Blue", "Black", "Cyan", "DarkGray", "Gray", "Green", "LightGray", "Magenta", "Orange", "Pink", "Yellow", "Red"};
    }

    public static Color stringToColor(String color) {
        return switch (color) {
            case "Blue" -> Color.BLUE;
            case "Black" -> Color.BLACK;
            case "Cyan" -> Color.CYAN;
            case "DarkGray" -> Color.DARK_GRAY;
            case "Gray" -> Color.GRAY;
            case "Green" -> Color.GREEN;
            case "LightGray" -> Color.LIGHT_GRAY;
            case "Magenta" -> Color.MAGENTA;
            case "Orange" -> Color.ORANGE;
            case "Pink" -> Color.PINK;
            case "Yellow" -> Color.YELLOW;
            case "Red" -> Color.RED;
            default -> Color.WHITE;
        };
    }

    public enum Colors {
        White,
        Blue,
        Black,
        Cyan,
        DarkGray,
        Gray,
        Green,
        LightGray,
        Magenta,
        Orange,
        Pink,
        Yellow,
        Red
    }


}
