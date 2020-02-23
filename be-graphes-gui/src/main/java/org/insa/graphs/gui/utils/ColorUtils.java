package org.insa.graphs.gui.utils;

import java.awt.Color;

public class ColorUtils {

    private static final Color[] COLORS = { // List of available colors
            new Color(57, 172, 115), // Forest (Green)
            new Color(246, 67, 63), // Red
            new Color(110, 56, 172), // Purple
            new Color(53, 191, 179), // Cyan
            new Color(219, 136, 48), // Orange / Brown
            new Color(110, 110, 110), // Gray
            new Color(56, 104, 172) // Blue
    };

    public static Color getColor(int i) {
        return COLORS[i % COLORS.length];
    }

}
