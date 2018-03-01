package org.insa.graphics.drawing;

import java.awt.Color;

import org.insa.graph.Arc;

public interface GraphPalette {

    /**
     * @return The default point width for this palette.
     */
    public int getDefaultPointWidth();

    /**
     * @return The default point color for this palette.
     */
    public Color getDefaultPointColor();

    /**
     * @param arc
     * 
     * @return Color associated with the given arc.
     */
    public Color getColorForArc(Arc arc);

    /**
     * @param arc
     * 
     * @return Width associated with the given arc.
     */
    public int getWidthForArc(Arc arc);

}
