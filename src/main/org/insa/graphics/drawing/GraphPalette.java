package org.insa.graphics.drawing;

import java.awt.Color;

import org.insa.graph.Arc;

public interface GraphPalette {

    /**
     * @param arc Arc for which color should be retrieved.
     * 
     * @return Color associated with the given arc.
     */
    public Color getColorForArc(Arc arc);

    /**
     * @param arc Arc for which width should be retrieved.
     * 
     * @return Width associated with the given arc.
     */
    public int getWidthForArc(Arc arc);

}
