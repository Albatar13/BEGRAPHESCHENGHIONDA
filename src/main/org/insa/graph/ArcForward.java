package org.insa.graph;

import java.util.Collections;
import java.util.List;

/**
 * Implementation of Arc that represents a "forward" arc in a graph, this is the
 * arc implementation that stores data relative to the arc.
 *
 */
class ArcForward extends Arc {

    // Destination node.
    private final Node origin, destination;

    // Length of the road (in meters).
    private final float length;

    // Road information.
    private final RoadInformation info;

    // Segments.
    private final List<Point> points;

    /**
     * Create a new ArcForward with the given attributes.
     * 
     * @param origin Origin of this arc.
     * @param dest Destination of this arc.
     * @param length Length of this arc (in meters).
     * @param roadInformation Road information for this arc.
     * @param points Points representing this arc.
     */
    protected ArcForward(Node origin, Node dest, float length, RoadInformation roadInformation,
            List<Point> points) {
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
    public float getLength() {
        return length;
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
