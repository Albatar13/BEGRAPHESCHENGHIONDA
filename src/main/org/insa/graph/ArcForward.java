package org.insa.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of Arc that represents a "forward" arc in a graph, this is the
 * arc implementation that stores data relative to the arc.
 *
 */
class ArcForward implements Arc {

    // Destination node.
    private final Node origin, destination;

    // Length of the road (in meters).
    private final int length;

    // Road information.
    private final RoadInformation info;

    // Segments.
    private final ArrayList<Point> points;

    /**
     * Create a new ArcForward with the given attributes.
     * 
     * @param origin Origin of this arc.
     * @param dest Destination of this arc.
     * @param length Length of this arc (in meters).
     * @param roadInformation Road information for this arc.
     * @param points Points representing this arc.
     */
    protected ArcForward(Node origin, Node dest, int length, RoadInformation roadInformation,
            ArrayList<Point> points) {
        this.origin = origin;
        this.destination = dest;
        this.length = length;
        this.info = roadInformation;
        this.points = points;
    }

    @Override
    public Node getOrigin() {
        return origin;
    }

    @Override
    public Node getDestination() {
        return destination;
    }

    @Override

    public int getLength() {
        return length;
    }

    @Override

    public double getMinimumTravelTime() {
        return getLength() * 3600.0 / (info.getMaximumSpeed() * 1000.0);
    }

    @Override

    public RoadInformation getRoadInformation() {
        return info;
    }

    @Override
    public List<Point> getPoints() {
        return Collections.unmodifiableList(points);
    }

}
