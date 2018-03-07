package org.insa.algo.carpooling;

import org.insa.algo.AbstractInputData;
import org.insa.graph.Graph;

public class CarPoolingData extends AbstractInputData {

    protected CarPoolingData(Graph graph, Mode mode, ArcFilter arcFilter) {
        super(graph, mode, arcFilter);
    }

}
