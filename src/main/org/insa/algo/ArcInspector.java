package org.insa.algo;

import org.insa.algo.AbstractInputData.Mode;
import org.insa.graph.Arc;
import org.insa.graph.GraphStatistics;

/**
 * This class can be used to indicate to an algorithm which arcs can be used and
 * the costs of the usable arcs..
 *
 */
public interface ArcInspector {

    /**
     * Check if the given arc can be used (is allowed).
     * 
     * @param arc Arc to check.
     * 
     * @return true if the given arc is allowed.
     */
    public boolean isAllowed(Arc arc);

    /**
     * Find the cost of the given arc.
     * 
     * @param arc Arc for which the cost should be returned.
     * 
     * @return Cost of the arc.
     */
    public double getCost(Arc arc);

    /**
     * @return The maximum speed for this inspector, or
     *         {@link GraphStatistics#NO_MAXIMUM_SPEED} if none is set.
     */
    public int getMaximumSpeed();

    /**
     * @return Mode for this arc inspector.
     */
    public Mode getMode();

}