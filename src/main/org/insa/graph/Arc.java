package org.insa.graph;

import java.util.List;

/**
 * Interface representing an arc in the graph - Arc is an interface and not a
 * class to allow us to represent two-ways roads in a memory efficient manner
 * (without having to duplicate attributes).
 * 
 * Arc should never be created manually but always using the
 * {@link Node#linkNodes(Node, Node, int, RoadInformation, java.util.ArrayList)}
 * method to ensure proper instantiation of the {@link ArcForward} and
 * {@link ArcBackward} classes.
 * 
 * @see ArcForward
 * @see ArcBackward
 *
 */
public interface Arc {

    /**
     * @return Origin node of this arc.
     */
    public Node getOrigin();

    /**
     * @return Destination node of this arc.
     */
    public Node getDestination();

    /**
     * @return Length of this arc, in meters.
     */
    public int getLength();

    /**
     * @return Minimum time required to travel this arc, in seconds.
     */
    public double getMinimumTravelTime();

    /**
     * @return Road information for this arc.
     */
    public RoadInformation getRoadInformation();

    /**
     * @return Points representing segments of this arc.
     */
    public List<Point> getPoints();
}
