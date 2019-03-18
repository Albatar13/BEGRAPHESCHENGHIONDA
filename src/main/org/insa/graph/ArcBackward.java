package org.insa.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of Arc that represents a "backward" arc in a graph, i.e., an
 * arc that is the reverse of another one. This arc only holds a reference to
 * the original arc.
 *
 */
class ArcBackward extends Arc {

    // Original arc
    private final Arc originalArc;

    /**
     * Create a new backward arc which corresponds to the reverse arc of the given
     * arc.
     * 
     * @param originalArc Original forwarc arc corresponding to this backward arc.
     */
    protected ArcBackward(Arc originalArc) {
        this.originalArc = originalArc;
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
    public float getLength() {
        return this.originalArc.getLength();
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
