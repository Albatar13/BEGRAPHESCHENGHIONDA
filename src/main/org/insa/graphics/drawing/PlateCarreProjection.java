package org.insa.graphics.drawing;

import org.insa.graph.GraphStatistics.BoundingBox;

public class PlateCarreProjection implements Projection {

    // Bounding box
    private final float minLatitude, minLongitude, maxLatitude, maxLongitude;

    // Dimension of the image
    private final double width, height;

    /**
     * Create a new PlateCarreProjection corresponding to the given BoundingBox and
     * maxSize.
     * 
     * @param boundingBox Box for this projection.
     * @param maxSize Maximum size of any side (width / height) of the image to
     *        which this projection should draw.
     */
    public PlateCarreProjection(BoundingBox boundingBox, int maxSize) {
        // Find minimum/maximum longitude and latitude.
        this.minLongitude = boundingBox.getTopLeftPoint().getLongitude();
        this.maxLongitude = boundingBox.getBottomRightPoint().getLongitude();
        this.minLatitude = boundingBox.getBottomRightPoint().getLatitude();
        this.maxLatitude = boundingBox.getTopLeftPoint().getLatitude();

        float diffLon = maxLongitude - minLongitude, diffLat = maxLatitude - minLatitude;

        this.width = diffLon < diffLat ? (int) (maxSize * diffLon / diffLat) : maxSize;
        this.height = diffLon < diffLat ? maxSize : (int) (maxSize * diffLat / diffLon);
    }

    @Override
    public double getImageWidth() {
        return this.width;
    }

    @Override
    public double getImageHeight() {
        return this.height;
    }

    @Override
    public int latitudeToPixelY(float latitude) {
        return (int) (this.height * (this.maxLatitude - latitude)
                / (this.maxLatitude - this.minLatitude));
    }

    @Override
    public int longitudeToPixelX(float longitude) {
        return (int) (this.width * (longitude - this.minLongitude)
                / (this.maxLongitude - this.minLongitude));
    }

    @Override
    public float pixelYToLatitude(double py) {
        return (float) (this.maxLatitude
                - py / this.height * (this.maxLatitude - this.minLatitude));
    }

    @Override
    public float pixelXToLongitude(double px) {
        return (float) (px / this.width * (this.maxLongitude - this.minLongitude)
                + this.minLongitude);
    }

}
