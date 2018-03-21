package org.insa.graphics.drawing;

import java.awt.Dimension;

import org.insa.graph.GraphStatistics.BoundingBox;

public class MercatorProjection implements Projection {

    public static final double MAX_LATITUDE = 82;

    public static final double MIN_LATITUDE = -MAX_LATITUDE;

    // From Wikipedia... for the above max/min latitude.
    private static final double IMAGE_WIDTH = 2058, IMAGE_HEIGHT = 1746;

    private static final double MAX_LATITUDE_PROJ = projectY(MAX_LATITUDE);
    private static final double MIN_LATITUDE_PROJ = projectY(MIN_LATITUDE);

    // Bounding box
    private final float minLatitude, minLongitude, maxLatitude, maxLongitude;

    // Projection of min and max latitude.
    private final double minLatitudeProj, maxLatitudeProj;

    // Dimension of the image
    private final double width, height;

    /**
     * Create a new MercatorProjection corresponding to the given BoundingBox and
     * maxSize.
     * 
     * @param boundingBox Box for this projection.
     * @param maxSize Maximum size of any side (width / height) of the image to
     *        which this projection should draw.
     */
    public MercatorProjection(BoundingBox boundingBox, int maxSize) {
        // Find minimum/maximum longitude and latitude.
        this.minLongitude = boundingBox.getTopLeftPoint().getLongitude();
        this.maxLongitude = boundingBox.getBottomRightPoint().getLongitude();
        this.minLatitude = boundingBox.getBottomRightPoint().getLatitude();
        this.maxLatitude = boundingBox.getTopLeftPoint().getLatitude();

        // Compute projection
        this.minLatitudeProj = projectY(this.minLatitude);
        this.maxLatitudeProj = projectY(this.maxLatitude);

        Dimension imageDimension = computeImageSize(maxSize);
        this.width = imageDimension.getWidth();
        this.height = imageDimension.getHeight();
    }

    /**
     * Compute the projection (without scaling) of the given latitude.
     * 
     * @param latitude Latitude to project.
     * 
     * @return Projection of the given latitude (without scaling).
     */
    private static double projectY(double latitude) {
        double sinLatitude = Math.sin(latitude * Math.PI / 180.0);
        return Math.log((1 + sinLatitude) / (1 - sinLatitude)) / 2;
    }

    /**
     * Compute the dimension required for drawing a projection of the given box on
     * an image, ensuring that none of the side of image is greater than maxSize.
     * 
     * @param maxSize Maximum side of any side of the image.
     * 
     * @return Dimension corresponding to the preferred size for the image.
     */
    protected Dimension computeImageSize(int maxSize) {
        double propWidth = (maxLongitude - minLongitude) * IMAGE_WIDTH / 360.0;
        double propHeight = (this.maxLatitudeProj - this.minLatitudeProj)
                / (MAX_LATITUDE_PROJ - MIN_LATITUDE_PROJ) * IMAGE_HEIGHT;

        return propWidth < propHeight
                ? new Dimension((int) (maxSize * propWidth / propHeight), maxSize)
                : new Dimension(maxSize, (int) (maxSize * propHeight / propWidth));
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
        return (int) ((this.maxLatitudeProj - projectY(latitude))
                / (this.maxLatitudeProj - this.minLatitudeProj) * this.height);
    }

    @Override
    public int longitudeToPixelX(float longitude) {
        return (int) (width * (longitude - minLongitude) / (maxLongitude - minLongitude));
    }

    @Override
    public float pixelYToLatitude(double py) {
        float y = (float) (this.maxLatitudeProj
                - (py / this.height) * (this.maxLatitudeProj - this.minLatitudeProj));
        return (float) (180 * (2 * Math.atan(Math.exp(y)) - Math.PI / 2) / Math.PI);
    }

    @Override
    public float pixelXToLongitude(double px) {
        return (float) ((px / this.width) * (this.maxLongitude - this.minLongitude)
                + this.minLongitude);
    }

}
