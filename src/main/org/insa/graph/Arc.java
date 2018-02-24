package org.insa.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Arc {

    // Destination node.
    private final Node origin, destination;

    // Length of the road (in meters).
    private final int length;

    // Road information.
    private final RoadInformation info;

    // Segments.
    private final ArrayList<Point> points;

    /**
     * @param dest
     * @param length
     * @param roadInformation
     * @param points
     */
    public Arc(Node origin, Node dest, int length, RoadInformation roadInformation) {
        this.origin = origin;
        this.destination = dest;
        this.length = length;
        this.info = roadInformation;
        this.points = new ArrayList<Point>();
        origin.addSuccessor(this);
    }

    /**
     * @param dest
     * @param length
     * @param roadInformation
     * @param points
     */
    public Arc(Node origin, Node dest, int length, RoadInformation roadInformation, ArrayList<Point> points) {
        this.origin = origin;
        this.destination = dest;
        this.length = length;
        this.info = roadInformation;
        this.points = points;
        origin.addSuccessor(this);
    }

    /**
     * @return Origin node of this arc.
     */
    public Node getOrigin() {
        return origin;
    }

    /**
     * @return Destination node of this arc.
     */
    public Node getDestination() {
        return destination;
    }

    /**
     * @return Length of this arc, in meters.
     */
    public int getLength() {
        return length;
    }

    /**
     * @return Minimum time required to travel this arc, in seconds.
     */
    public double getMinimumTravelTime() {
        return getLength() * 3600.0 / (info.getMaximumSpeed() * 1000.0);
    }

    /**
     * @return Road information for this arc.
     */
    public RoadInformation getInfo() {
        return info;
    }

    /**
     * @return Points representing segments of this arc. This function may return an
     *         empty ArrayList if the segments are stored in the reversed arc (for
     *         two-ways road).
     */
    public List<Point> getPoints() {
        return Collections.unmodifiableList(points);
    }

}
