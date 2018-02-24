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
     * Create a new arc and automatically link it with the given origin.
     * 
     * @param origin Origin of this arc.
     * @param dest Destination of this arc.
     * @param length Length of this arc (in meters).
     * @param roadInformation Road information for this arc.
     * @param points Points representing this arc.
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
     * @return Points representing segments of this arc.
     */
    public List<Point> getPoints() {
        return Collections.unmodifiableList(points);
    }

}
