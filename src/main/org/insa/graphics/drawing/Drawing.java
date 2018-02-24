package org.insa.graphics.drawing;

import java.awt.Color;

import org.insa.graph.Graph;
import org.insa.graph.Path;
import org.insa.graph.Point;

public interface Drawing {

    /**
     * Add a listener to click to this drawing.
     * 
     * @param listener
     */
    public void addDrawingClickListener(DrawingClickListener listener);

    /**
     * Remove the given listener from the drawing.
     * 
     * @param listener
     */
    public void removeDrawingClickListener(DrawingClickListener listener);

    /**
     * Clear the drawing.
     */
    public void clear();

    /**
     * Draw a line between the two given points with the default color and width.
     * 
     * @param from
     * @param to
     */
    public void drawLine(Point from, Point to);

    /**
     * Draw a line between the two given points with the default color and the given
     * width.
     * 
     * @param from
     * @param to
     * @param width
     */
    public void drawLine(Point from, Point to, int width);

    /**
     * Draw a line between the two given points with the given color and the given
     * width.
     * 
     * @param from
     * @param to
     * @param width
     * @param color
     */
    public void drawLine(Point from, Point to, int width, Color color);

    /**
     * Draw a marker at the given point with the default color.
     * 
     * @param point
     */
    public void drawMarker(Point point);

    /**
     * Draw the given point with the given color.
     * 
     * @param point
     */
    public void drawMarker(Point point, Color color);

    /**
     * Draw a point width the given width and color. Do not use this to mark
     * location, use drawMarker.
     * 
     * @param point
     * @param width
     * @param color
     */
    public void drawPoint(Point point, int width, Color color);

    /**
     * Draw the given graph using the given palette.
     * 
     * @param graph
     * @param palette
     */
    public void drawGraph(Graph graph, GraphPalette palette);

    /**
     * Draw the given graph using a default palette specific to the implementation.
     * 
     * @param graph
     */
    public void drawGraph(Graph graph);

    /**
     * Draw a path using the given color.
     * 
     * @param path
     * @param color
     * @param markers
     *            Show origin and destination markers.
     */
    public void drawPath(Path path, Color color, boolean markers);

    /**
     * Draw a path using the given color with markers.
     * 
     * @param path
     * @param color
     */
    public void drawPath(Path path, Color color);

    /**
     * Draw a path using a default color specific to the implementation
     * 
     * 
     * @param path
     * @param markers
     *            Show origin and destination markers.
     */
    public void drawPath(Path path, boolean markers);

    /**
     * Draw a path using a default color specific to the implementation
     * 
     * 
     * @param path
     */
    public void drawPath(Path path);

}
