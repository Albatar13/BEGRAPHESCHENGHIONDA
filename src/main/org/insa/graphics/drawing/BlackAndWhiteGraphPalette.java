package org.insa.graphics.drawing;

import java.awt.Color;

import org.insa.graph.Arc;

public class BlackAndWhiteGraphPalette extends BasicGraphPalette {

    // Road colors (index
    private final static Color[] ROAD_COLOR_FROM_WIDTH = { null, new Color(140, 140, 140),
            new Color(80, 80, 80), new Color(40, 40, 40), new Color(30, 30, 30) };

    @Override
    public Color getColorForArc(Arc arc) {
        int width = getWidthForArc(arc);
        return ROAD_COLOR_FROM_WIDTH[width];
    }

}
