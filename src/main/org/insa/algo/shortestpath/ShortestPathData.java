package org.insa.algo.shortestpath;

import org.insa.algo.AbstractInputData;
import org.insa.algo.ArcInspector;
import org.insa.graph.Graph;
import org.insa.graph.Node;

public class ShortestPathData extends AbstractInputData {

    // Origin and destination nodes.
    private final Node origin, destination;

    /**
     * Construct a new instance of ShortestPathInputData with the given parameters.
     * 
     * @param graph Graph in which the path should be looked for.
     * @param origin Origin node of the path.
     * @param destination Destination node of the path.
     * @param arcInspector Filter for arcs (used to allow only a specific set of
     *        arcs in the graph to be used).
     */
    public ShortestPathData(Graph graph, Node origin, Node destination, ArcInspector arcInspector) {
        super(graph, arcInspector);
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * @return Origin node for the path.
     */
    public Node getOrigin() {
        return origin;
    }

    /**
     * @return Destination node for the path.
     */
    public Node getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "Shortest-path from #" + origin.getId() + " to #" + destination.getId() + " ["
                + this.arcInspector.toString().toLowerCase() + "]";
    }
}
