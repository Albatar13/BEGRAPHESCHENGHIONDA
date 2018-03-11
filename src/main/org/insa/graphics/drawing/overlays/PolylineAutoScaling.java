package org.insa.graphics.drawing.overlays;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import org.insa.graph.Point;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;
import org.mapsforge.map.layer.overlay.Polyline;

/**
 * Class extending the default Mapsforge's {@link Polyline} with auto-scaling.
 * 
 * Mapsforge's Polylines do not scale with zoom level, this class aims at
 * correcting this. When a redraw is requested, the width of the line is
 * recomputed for the current zoom level.
 * 
 * @see PaintUtils#getStrokeWidth(int, byte)
 */
public class PolylineAutoScaling extends Polyline {

    // Graphic factory.
    private static final GraphicFactory GRAPHIC_FACTORY = AwtGraphicFactory.INSTANCE;

    // Original width of the polyline.
    private final int width;

    /**
     * Create a new PolylineAutoScaling with the given width and color.
     * 
     * @param width Original width of the line (independent of the zoom level).
     * @param color Color of the line.
     * 
     * @see PaintUtils#getStrokeWidth(int, byte)
     */
    public PolylineAutoScaling(int width, Color color) {
        super(GRAPHIC_FACTORY.createPaint(), GRAPHIC_FACTORY);
        getPaintStroke().setColor(PaintUtils.convertColor(color));
        getPaintStroke().setStyle(Style.STROKE);
        this.width = width;
    }

    /**
     * Set the color for this polyline.
     * 
     * @param color New color for this polyline.
     */
    public void setColor(Color color) {
        getPaintStroke().setColor(PaintUtils.convertColor(color));
    }

    /**
     * @return Color of this polyline.
     */
    public Color getColor() {
        return PaintUtils.convertColor(getPaintStroke().getColor());
    }

    /**
     * @param point Point to add to this line.
     */
    public void add(Point point) {
        getLatLongs().add(new LatLong(point.getLatitude(), point.getLongitude()));
    }

    /**
     * @param points Points to add to this line.
     */
    public void addAll(Collection<? extends Point> points) {
        ArrayList<LatLong> latlongs = new ArrayList<>(points.size());
        for (Point point: points) {
            latlongs.add(new LatLong(point.getLatitude(), point.getLongitude()));
        }
        getLatLongs().addAll(latlongs);
    }

    @Override
    public synchronized void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas,
            org.mapsforge.core.model.Point topLeftPoint) {

        // Update paint stroke with width for level
        this.getPaintStroke().setStrokeWidth(PaintUtils.getStrokeWidth(width, zoomLevel));

        super.draw(boundingBox, zoomLevel, canvas, topLeftPoint);
    }

}
