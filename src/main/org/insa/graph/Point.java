package org.insa.graph;

/**
 * Class representing a point on Earth.
 *
 */
public class Point {

    // Earth radius, in meters;
    private static final double EARTH_RADIUS = 6378137.0;

    /**
     * Compute the distance in meters between the two given points.
     * 
     * @param p1, p2
     * 
     * @return Distance between the two given points, in meters.
     */
    public static double distance(Point p1, Point p2) {
        double sinLat = Math.sin(Math.toRadians(p1.getLatitude())) * Math.sin(Math.toRadians(p2.getLatitude()));
        double cosLat = Math.cos(Math.toRadians(p1.getLatitude())) * Math.cos(Math.toRadians(p2.getLatitude()));
        double cosLong = Math.cos(Math.toRadians(p2.getLongitude() - p1.getLongitude()));
        return EARTH_RADIUS * Math.acos(sinLat + cosLat * cosLong);
    }

    // Longitude and latitude of the point.
    private final double longitude, latitude;

    /**
     * 
     * @param longitude Longitude of the point, in degrees.
     * @param latitude Latitude of the point, in degrees.
     */
    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * @return Longitude of this point (in degrees).
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return Latitude of this point (in degrees).
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Compute the distance from this point to the given point
     * 
     * @param target Target point.
     * 
     * @return Distance between this point and the target point, in meters.
     */
    public double distanceTo(Point target) {
        return distance(this, target);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Point(%f, %f)", getLongitude(), getLatitude());
    }
}
