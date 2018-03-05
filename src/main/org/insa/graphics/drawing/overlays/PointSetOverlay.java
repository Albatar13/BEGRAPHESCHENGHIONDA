package org.insa.graphics.drawing.overlays;

import java.awt.Color;

import org.insa.graph.Point;

public interface PointSetOverlay extends Overlay {

    /**
     * Set the width of this overlay for future addPoint().
     * 
     * @param width
     */
    public void setWidth(int width);

    /**
     * Set color and width for this overlay for future addPoint().
     * 
     * @param width
     * @param color
     */
    public void setWidthAndColor(int width, Color color);

    /**
     * Add a new point using the current width and color.
     * 
     * @param point
     */
    public void addPoint(Point point);

    /**
     * Set the current width and then add a new point.
     * 
     * @param point
     * @param width
     */
    public void addPoint(Point point, int width);

    /**
     * Set the current color and then add a new point.
     * 
     * @param point
     * @param color
     */
    public void addPoint(Point point, Color color);

    /**
     * Add a new point to this set at the given location, with the given color and
     * width, and update the current color.
     * 
     * @param point
     * @param width
     * @param color
     */
    public void addPoint(Point point, int width, Color color);

}
