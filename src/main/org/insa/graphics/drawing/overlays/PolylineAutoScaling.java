package org.insa.graphics.drawing.overlays;

import java.awt.Color;
import java.util.List;

import org.insa.graph.Point;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;
import org.mapsforge.map.layer.overlay.Polyline;

public class PolylineAutoScaling extends Polyline {

    // Graphic factory.
    private static final GraphicFactory GRAPHIC_FACTORY = AwtGraphicFactory.INSTANCE;

    // Original width of the polyline.
    private final int width;

    /**
     * @param width
     * @param color
     */
    public PolylineAutoScaling(int width, Color color) {
        super(GRAPHIC_FACTORY.createPaint(), GRAPHIC_FACTORY);
        getPaintStroke().setColor(PaintUtils.convertColor(color));
        getPaintStroke().setStyle(Style.STROKE);
        this.width = width;
    }

    /**
     * @param point
     */
    public void add(Point point) {
        getLatLongs().add(new LatLong(point.getLatitude(), point.getLongitude()));
    }

    /**
     * @param points
     */
    public void add(List<Point> points) {
        for (Point point: points) {
            add(point);
        }
    }

    @Override
    public synchronized void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas,
            org.mapsforge.core.model.Point topLeftPoint) {

        // Update paint stroke with width for level
        this.getPaintStroke().setStrokeWidth(PaintUtils.getStrokeWidth(width, zoomLevel));

        super.draw(boundingBox, zoomLevel, canvas, topLeftPoint);
    }

}
