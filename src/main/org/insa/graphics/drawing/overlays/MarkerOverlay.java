package org.insa.graphics.drawing.overlays;

import java.awt.Color;

import org.insa.graph.Point;

public interface MarkerOverlay extends Overlay {

    /**
     * @return Color associated with this marker.
     */
    public Color getColor();

    /**
     * @return The point associated with this marker.
     */
    public Point getPoint();

    /**
     * 
     */
    public void moveTo(Point point);

}
