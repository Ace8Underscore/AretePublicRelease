package com.cousinware.arete.utils.rendering.nvg.string.coloredstring;

import com.cousinware.arete.client.AreteClient;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.Formatting;

import java.awt.*;

@Setter
@Getter
public class ColoredString {

    Color color;
    String text;
    Formatting formatting;

    public ColoredString(Color color, String text) {
        this.color = color;
        this.text = text;
        this.formatting = colorToFormat(color);
    }


    public static ColoredString of(Color color, String text) {
        return new ColoredString(color, text);
    }


    private static Formatting colorToFormat(Color color) {
        if (Color.RED == color) return Formatting.RED;
        else if (Color.DARK_GRAY == color) return Formatting.DARK_GRAY;
        else if (Color.BLACK == color) return Formatting.BLACK;
        else if (Color.BLUE == color) return Formatting.BLUE;
        else if (Color.PINK == color) return Formatting.LIGHT_PURPLE;
        else if (Color.MAGENTA == color) return Formatting.DARK_PURPLE;
        else if (Color.ORANGE == color) return Formatting.GOLD;
        else if (Color.GREEN == color) return Formatting.GREEN;
        else if (Color.LIGHT_GRAY == color) return Formatting.LIGHT_PURPLE;
        else if (Color.WHITE == color) return Formatting.WHITE;
        else if (Color.YELLOW == color) return Formatting.YELLOW;
        else if (Color.cyan == color) return Formatting.AQUA;
        return Formatting.byColorIndex(color.getRGB());


    }

    public static String getString(ColoredString[] coloredStrings) {
        String s = "";

        for (ColoredString string : coloredStrings) {
            s += string.getText();
        }
        return s;
    }

    @Override
    public String toString() {

        return text;
    }

    public float getTextWidth() {
        return AreteClient.fontManager.getStringWidth(text, AreteClient.fontManager.selectedFont);
    }


}
