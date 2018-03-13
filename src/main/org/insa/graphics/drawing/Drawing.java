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
     * Available fill mode for the creation of markers, see the documentation of
     * each value for more details.
     */
    enum AlphaMode {

        /**
         * Do not use the original transparency of the inner part to fill it.
         */
        OPAQUE,

        /**
         * Use the original transparency of the inner part to fill it.
         */
        TRANSPARENT
    }

    /**
     * Add a listener to click to this drawing.
     * 
     * @param listener DrawingClickListener to add to this Drawing.
     */
    public void addDrawingClickListener(DrawingClickListener listener);

    /**
     * Remove the given listener from the drawing.
     * 
     * @param listener DrawingClickListener to remove from this Drawing.
     */
    public void removeDrawingClickListener(DrawingClickListener listener);

    /**
     * Clear the drawing (overlays and underlying graph/map).
     */
    public void clear();

    /**
     * Remove overlays from the drawing (do not remove the underlying graph/map).
     */
    public void clearOverlays();

    /**
     * Draw a marker at the given position using the given colors and according to
     * the given mode.
     * 
     * @param point Position of the marker to draw.
     * @param outer Color for the outer part of the marker to draw.
     * @param inner Color for the inner part of the marker to draw.
     * @param mode Mode for filling the inner par of the marker.
     * 
     * @return A MarkerOverlay instance representing the newly drawn marker.
     */
    public MarkerOverlay drawMarker(Point point, Color outer, Color inner, AlphaMode mode);

    /**
     * Create a new PointSetOverlay that can be used to add overlay points to this
     * drawing.
     * 
     * PointSetOverlay are heavy memory resources, do not use one for each point!
     * 
     * @return A new PointSetOverlay for this drawing.
     */
    public PointSetOverlay createPointSetOverlay();

    /**
     * Create a new PointSetOverlay with the given initial width and color that can
     * be used to add overlay points to this drawing.
     * 
     * PointSetOverlay are heavy memory resources, do not use one for each point!
     * 
     * @param width Initial width of points in the overlay.
     * @param color Initial width of points in the overlay.
     * 
     * @return A new PointSetOverlay for this drawing.
     */
    public PointSetOverlay createPointSetOverlay(int width, Color color);

    /**
     * Draw the given graph using the given palette.
     * 
     * @param graph Graph to draw.
     * @param palette Palette to use to draw the graph.
     * 
     * @see BasicGraphPalette
     * @see BlackAndWhiteGraphPalette
     */
    public void drawGraph(Graph graph, GraphPalette palette);

    /**
     * Draw the given graph using a default palette specific to the implementation.
     * 
     * @param graph Graph to draw.
     */
    public void drawGraph(Graph graph);

    /**
     * Draw a path using the given color.
     * 
     * @param path Path to draw.
     * @param color Color of the path to draw.
     * @param markers true to show origin and destination markers.
     * 
     * @return A PathOverlay instance representing the newly drawn path.
     */
    public PathOverlay drawPath(Path path, Color color, boolean markers);

    /**
     * Draw a path with both origin and destination markers using the given color.
     * 
     * @param path Path to draw.
     * @param color Color of the path to draw.
     * 
     * @return A PathOverlay instance representing the newly drawn path.
     * 
     * @see Drawing#drawPath(Path, Color, boolean)
     */
    public PathOverlay drawPath(Path path, Color color);

    /**
     * Draw a path using a default color specific to the implementation
     * 
     * @param path Path to draw.
     * @param markers true to show origin and destination markers.
     * 
     * @return A PathOverlay instance representing the newly drawn path.
     * 
     * @see Drawing#drawPath(Path, Color, boolean)
     */
    public PathOverlay drawPath(Path path, boolean markers);

    /**
     * Draw a path with both origin and destination markers using a default color
     * specific to the implementation
     * 
     * 
     * @param path Path to draw.
     * 
     * @return A PathOverlay instance representing the newly drawn path.
     * 
     * @see Drawing#drawPath(Path, Color, boolean)
     */
    public PathOverlay drawPath(Path path);

}
