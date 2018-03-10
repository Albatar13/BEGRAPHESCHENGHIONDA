package org.insa.graphics.drawing.overlays;

import java.awt.Color;

import org.insa.graph.Point;

public interface PointSetOverlay extends Overlay {

    /**
     * Set the width of this overlay for future addPoint().
     * 
     * @param width New default width for this overlay.
     */
    public void setWidth(int width);

    /**
     * Set color and width for this overlay for future addPoint().
     * 
     * @param width New default width for this overlay.
     * @param color New default color for this overlay.
     */
    public void setWidthAndColor(int width, Color color);

    /**
     * Add a new point using the current width and color.
     * 
     * @param point Position of the point to add.
     * 
     * @see #setWidth(int)
     * @see #setColor(Color)
     */
    public void addPoint(Point point);

    /**
     * Set the current width and then add a new point.
     * 
     * @param point Position of the point to add.
     * @param width New default width for this overlay.
     * 
     * @see #setWidth(int)
     * @see PointSetOverlay#addPoint(Point)
     */
    public void addPoint(Point point, int width);

    /**
     * Set the current color and then add a new point.
     * 
     * @param point Position of the point to add.
     * @param color New default color for this overlay.
     * 
     * @see #setColor(Color)
     * @see PointSetOverlay#addPoint(Point)
     */
    public void addPoint(Point point, Color color);

    /**
     * Add a new point at the given location, with the given color and width, and
     * update the current width and color.
     * 
     * @param point Position of the point to add.
     * @param width New default width for this overlay.
     * @param color New default color for this overlay.
     * 
     * @see #setWidth(int)
     * @see #setColor(Color)
     * @see PointSetOverlay#addPoint(Point)
     */
    public void addPoint(Point point, int width, Color color);

}
