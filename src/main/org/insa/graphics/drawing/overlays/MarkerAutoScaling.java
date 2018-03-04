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

public class MarkerAutoScaling extends Marker {

    // Original image.
    private final Image originalImage;

    public MarkerAutoScaling(LatLong latLong, Image image) {
        super(latLong, null, 0, 0);
        this.originalImage = image;
    }

    /**
     * @return
     */
    public Image getImage() {
        return originalImage;
    }

    @Override
    public synchronized void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        int width = (int) PaintUtils.getStrokeWidth(8, zoomLevel),
                height = (int) PaintUtils.getStrokeWidth(16, zoomLevel);
        BufferedImage bfd = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = bfd.createGraphics();
        g.drawImage(this.originalImage.getScaledInstance(bfd.getWidth(), bfd.getHeight(), Image.SCALE_SMOOTH), 0, 0,
                null);
        setBitmap(new AwtBitmap(bfd));

        setVerticalOffset(-height / 2);
        super.draw(boundingBox, zoomLevel, canvas, topLeftPoint);
        g.dispose();
    }
}
