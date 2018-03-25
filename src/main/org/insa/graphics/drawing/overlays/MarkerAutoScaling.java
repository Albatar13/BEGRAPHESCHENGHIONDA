package org.insa.graphics.drawing.overlays;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.awt.graphics.AwtBitmap;
import org.mapsforge.map.layer.overlay.Marker;

/**
 * Class extending the default Mapsforge's {@link Marker} with auto-scaling.
 * 
 * Mapsforge's Markers do not scale with zoom level, this class aims at
 * correcting this. Internally, this image stores an {@link Image} instance and
 * scale it when a redraw is requested.
 * 
 * @see MarkerUtils#getMarkerForColor(java.awt.Color, java.awt.Color,
 *      org.insa.graphics.drawing.Drawing.AlphaMode)
 * @see PaintUtils#getStrokeWidth(int, byte)
 */
public class MarkerAutoScaling extends Marker {

    // Original image.
    private Image image;

    /**
     * Create a new MarkerAutoScaling at the given position with the given image.
     * 
     * @param latLong Initial position of the marker.
     * @param image Image for this marker.
     */
    public MarkerAutoScaling(LatLong latLong, Image image) {
        super(latLong, null, 0, 0);
        this.image = image;
    }

    /**
     * Set a new image for this marker overlay
     * 
     * @param image New image to set.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return Current image (marker) of this overlay.
     */
    public Image getImage() {
        return image;
    }

    @Override
    public synchronized void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas,
            Point topLeftPoint) {
        int width = (int) PaintUtils.getStrokeWidth(8, zoomLevel),
                height = (int) PaintUtils.getStrokeWidth(16, zoomLevel);
        BufferedImage bfd = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = bfd.createGraphics();
        g.drawImage(
                this.image.getScaledInstance(bfd.getWidth(), bfd.getHeight(), Image.SCALE_SMOOTH),
                0, 0, null);
        setBitmap(new AwtBitmap(bfd));

        setVerticalOffset(-height / 2);
        super.draw(boundingBox, zoomLevel, canvas, topLeftPoint);
        g.dispose();
    }
}
