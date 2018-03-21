package org.insa.graphics.drawing;

public interface Projection {

    /**
     * @return Image width for this projection to work properly.
     */
    public double getImageWidth();

    /**
     * @return Image weight for this projection to work properly.
     */
    public double getImageHeight();

    /**
     * Project the given latitude on the image.
     * 
     * @param latitude Latitude to project.
     * 
     * @return Projected position of the latitude on the image.
     */
    public int latitudeToPixelY(float latitude);

    /**
     * Project the given longitude on the image.
     * 
     * @param longitude Longitude to project.
     * 
     * @return Projected position of the longitude on the image.
     */
    public int longitudeToPixelX(float longitude);

    /**
     * Retrieve the latitude associated to the given projected point.
     * 
     * @param py Projected y-position for which latitude should be retrieved.
     * 
     * @return The original latitude of the point.
     */
    public float pixelYToLatitude(double py);

    /**
     * Retrieve the longitude associated to the given projected point.
     * 
     * @param px Projected x-position for which longitude should be retrieved.
     * 
     * @return The original longitude of the point.
     */
    public float pixelXToLongitude(double px);

}
