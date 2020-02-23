package org.insa.graphs.algorithm.weakconnectivity;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.model.Graph;

public class WeaklyConnectedComponentsData extends AbstractInputData {

    /**
     * @param graph Graph for which components should be retrieved.
     */
    public WeaklyConnectedComponentsData(Graph graph) {
        super(graph, null);
    }

    @Override
    public String toString() {
        return "Weakly-connected components from #0.";
    }

}
