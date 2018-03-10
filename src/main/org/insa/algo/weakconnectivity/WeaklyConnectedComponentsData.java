package org.insa.algo.weakconnectivity;

import org.insa.algo.AbstractInputData;
import org.insa.graph.Graph;

public class WeaklyConnectedComponentsData extends AbstractInputData {

    /**
     * 
     * @param graph
     */
    public WeaklyConnectedComponentsData(Graph graph) {
        super(graph);
    }

    @Override
    public String toString() {
        return "Weakly-connected components from #0.";
    }

}
