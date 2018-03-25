package org.insa.algo.weakconnectivity;

import org.insa.algo.AbstractInputData;
import org.insa.graph.Graph;

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
