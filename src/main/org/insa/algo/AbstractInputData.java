package org.insa.algo;

import org.insa.graph.Graph;

public abstract class AbstractInputData {

    protected Graph graph;

    /**
     * Create a new AbstractInputData instance with the given graph.
     * 
     * @param graph
     */
    protected AbstractInputData(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

}
