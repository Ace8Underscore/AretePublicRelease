package com.cousinware.arete.utils.rendering.nvg.string.coloredstring;

import java.util.Comparator;

public class ColoredStringComparator implements Comparator<ColoredString[]> {

    @Override
    public int compare(ColoredString[] o1, ColoredString[] o2) {

        int message1Width = 0;
        int message2Width = 0;

        for (ColoredString coloredString : o1) {
            message1Width += coloredString.getTextWidth();
        }

        for (ColoredString coloredString : o2) {
            message2Width += coloredString.getTextWidth();
        }
        return Integer.compare(message1Width, message2Width);
    }
}
