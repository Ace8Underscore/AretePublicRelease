package com.cousinware.arete.utils.rendering.nvg.string.coloredstring;

public class ColoredStringHelper {

    public static float getWidth(ColoredString[] coloredStrings) {
        float messageWidth = 0;
        for (ColoredString coloredString : coloredStrings) {
            messageWidth += coloredString.getTextWidth();
            //messageWidth += AreteClient.fontManager.getStringWidth(" ", AreteClient.fontManager.selectedFont);
        }
        return messageWidth;
    }
}
