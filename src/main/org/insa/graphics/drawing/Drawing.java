package org.insa.graphics.drawing;

import java.awt.Color;

import org.insa.graph.Graph;
import org.insa.graph.Path;
import org.insa.graph.Point;
import org.insa.graphics.drawing.overlays.MarkerOverlay;
import org.insa.graphics.drawing.overlays.PathOverlay;
import org.insa.graphics.drawing.overlays.PointSetOverlay;

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
     * Draw the given point with the given color.
     * 
     * @param point
     */
    public MarkerOverlay drawMarker(Point point, Color color);

    /**
     * Create a new PointSetOverlay that can be used to add overlay points to this
     * drawing.
     * 
     * PointSetOverlay are heavy memory resources, do not use one for each point!
     * 
     */
    public PointSetOverlay createPointSetOverlay();

    /**
     * Create a new PointSetOverlay with the original width and color that can be
     * used to add overlay points to this drawing.
     * 
     * PointSetOverlay are heavy memory resources, do not use one for each point!
     * 
     */
    public PointSetOverlay createPointSetOverlay(int width, Color color);

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
     * @param markers Show origin and destination markers.
     * @return
     */
    public PathOverlay drawPath(Path path, Color color, boolean markers);

    /**
     * Draw a path using the given color with markers.
     * 
     * @param path
     * @param color
     */
    public PathOverlay drawPath(Path path, Color color);

    /**
     * Draw a path using a default color specific to the implementation
     * 
     * 
     * @param path
     * @param markers Show origin and destination markers.
     */
    public PathOverlay drawPath(Path path, boolean markers);

    /**
     * Draw a path using a default color specific to the implementation
     * 
     * 
     * @param path
     */
    public PathOverlay drawPath(Path path);

}
