package org.insa.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of Arc that represents a "backward" arc in a graph, i.e. an
 * arc that is the reverse of another one. This arc only holds a reference to
 * the original arc.
 *
 */
public class ArcBackward implements Arc {

    // Original arc
    private final ArcForward originalArc;

    /**
     * Create a new backward arc which corresponds to the reverse arc of the given
     * arc.
     * 
     * @param originalArc
     */
    public ArcBackward(ArcForward originalArc) {
        this.originalArc = originalArc;
        this.originalArc.getDestination().addSuccessor(this);
    }

    @Override
    public Node getOrigin() {
        return this.originalArc.getDestination();
    }

    @Override
    public Node getDestination() {
        return this.originalArc.getOrigin();
    }

    @Override
    public int getLength() {
        return this.originalArc.getLength();
    }

    @Override
    public double getMinimumTravelTime() {
        return this.originalArc.getMinimumTravelTime();
    }

    @Override
    public RoadInformation getRoadInformation() {
        return this.originalArc.getRoadInformation();
    }

    @Override
    public List<Point> getPoints() {
        List<Point> pts = new ArrayList<>(this.originalArc.getPoints());
        Collections.reverse(pts);
        return pts;
    }

}
