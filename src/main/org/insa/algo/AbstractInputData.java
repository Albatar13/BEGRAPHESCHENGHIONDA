package org.insa.algo;

import org.insa.graph.Arc;
import org.insa.graph.Graph;

/**
 * Base class for algorithm input data classes. This class contains the basic
 * data that are required by most graph algorithms, i.e. a graph, a mode (time /
 * length) and a filter for the arc.
 *
 */
public abstract class AbstractInputData {

    /**
     * Mode for computing costs on the arc (time or length).
     *
     */
    public enum Mode {
        TIME, LENGTH
    }

    /**
     * Filtering inteface for arcs - This class can be used to indicate to an
     * algorithm which arc can be used.
     *
     */
    public interface ArcFilter {

        /**
         * Check if the given arc can be used (is allowed).
         * 
         * @param arc Arc to check.
         * 
         * @return true if the given arc is allowed.
         */
        public boolean isAllowed(Arc arc);

    }

    // Graph
    private final Graph graph;

    // Mode for the computation of the costs.
    private final Mode mode;

    // Arc filter.
    private final ArcFilter arcFilter;

    /**
     * Create a new AbstractInputData instance for the given graph, mode and filter.
     * 
     * @param graph
     * @parma mode
     * @param arcFilter
     */
    protected AbstractInputData(Graph graph, Mode mode, ArcFilter arcFilter) {
        this.graph = graph;
        this.mode = mode;
        this.arcFilter = arcFilter;
    }

    /**
     * Create a new AbstractInputData instance for the given graph and mode, with no
     * filtering on the arc.
     * 
     * @param graph
     * @param mode
     */
    protected AbstractInputData(Graph graph, Mode mode) {
        this(graph, mode, new AbstractInputData.ArcFilter() {
            @Override
            public boolean isAllowed(Arc arc) {
                return true;
            }
        });
    }

    /**
     * Create a new AbstractInputData instance for the given graph, with default
     * mode (LENGHT), with no filtering on the arc.
     * 
     * @param graph
     */
    protected AbstractInputData(Graph graph) {
        this(graph, Mode.LENGTH);
    }

    /**
     * @return Graph associated with this input.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @return Mode of the algorithm (time or length).
     * 
     * @see Mode
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Check if the given arc is allowed for the filter corresponding to this input.
     * 
     * @param arc Arc to check.
     * 
     * @return true if the given arc is allowed.
     * 
     * @see ArcFilter
     */
    public boolean isAllowed(Arc arc) {
        return this.arcFilter.isAllowed(arc);
    }

}
