package org.insa.algo;

import org.insa.graph.Arc;
import org.insa.graph.Graph;

public abstract class AbstractInputData {

    public enum Mode {
        TIME, LENGTH
    }

    /**
     * 
     *
     */
    public interface ArcFilter {

        /**
         * @param arc
         * 
         * @return true if the given arc is allowed.
         */
        public boolean isAllowed(Arc arc);

    }

    // Graph
    protected Graph graph;

    // Mode for the computation of the costs.
    private final AbstractInputData.Mode mode;

    // Arc filter.
    private final AbstractInputData.ArcFilter arcFilter;

    /**
     * Create a new AbstractInputData instance for the given graph, mode and filter.
     * 
     * @param graph
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
     * @param mode
     */
    protected AbstractInputData(Graph graph) {
        this(graph, Mode.LENGTH, new AbstractInputData.ArcFilter() {
            @Override
            public boolean isAllowed(Arc arc) {
                return true;
            }
        });
    }

    /**
     * @return Graph associated with this input.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @return Mode of the algorithm (time or length).
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * @return true if the given arc is allowed.
     */
    public boolean isAllowed(Arc arc) {
        return this.arcFilter.isAllowed(arc);
    }

}
