package org.insa.algo;

import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.GraphStatistics;

/**
 * Base class for algorithm input data classes. This class contains the basic
 * data that are required by most graph algorithms, i.e. a graph, a mode (time /
 * length) and a filter for the arc.
 *
 */
public abstract class AbstractInputData {

    /**
     * Enum specifying the top mode of the algorithms.
     * 
     * @see ArcInspector
     */
    public enum Mode {
        TIME, LENGTH
    }

    // Graph
    private final Graph graph;

    // Arc filter.
    protected final ArcInspector arcInspector;

    /**
     * Create a new AbstractInputData instance for the given graph, mode and filter.
     * 
     * @param graph Graph for this input data.
     * @param arcInspector Arc inspector for this input data.
     */
    protected AbstractInputData(Graph graph, ArcInspector arcInspector) {
        this.graph = graph;
        this.arcInspector = arcInspector;
    }

    /**
     * @return Graph associated with this input.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Retrieve the cost associated with the given arc according to the underlying
     * arc inspector.
     * 
     * @param arc Arc for which cost should be retrieved.
     * 
     * @return Cost for the given arc.
     * 
     * @see ArcInspector
     */
    public double getCost(Arc arc) {
        return this.arcInspector.getCost(arc);
    }

    /**
     * @return Mode associated with this input data.
     * 
     * @see Mode
     */
    public Mode getMode() {
        return this.arcInspector.getMode();
    }

    /**
     * Retrieve the maximum speed associated with this input data, or
     * {@link GraphStatistics#NO_MAXIMUM_SPEED} if none is associated. The maximum
     * speed associated with input data is different from the maximum speed
     * associated with graph (accessible via {@link Graph#getGraphInformation()}).
     * 
     * @return The maximum speed for this inspector, or
     *         {@link GraphStatistics#NO_MAXIMUM_SPEED} if none is set.
     */
    public int getMaximumSpeed() {
        return this.arcInspector.getMaximumSpeed();
    }

    /**
     * Check if the given arc is allowed for the filter corresponding to this input.
     * 
     * @param arc Arc to check.
     * 
     * @return true if the given arc is allowed.
     * 
     * @see ArcInspector
     */
    public boolean isAllowed(Arc arc) {
        return this.arcInspector.isAllowed(arc);
    }

}
