package org.insa.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Graph {

    // Map identifier.
    private final int mapId;

    // Nodes of the graph.
    private final ArrayList<Node> nodes;

    /**
     * @param mapId ID of this graph.
     * @param nodes List of nodes for this graph.
     */
    public Graph(int mapId, ArrayList<Node> nodes) {
        this.mapId = mapId;
        this.nodes = nodes;
    }

    /**
     * @return Immutable list of nodes of this graph.
     */
    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    /**
     * Find the closet node to the given point.
     * 
     * @param point
     * 
     * @return Closest node to the given point.
     */
    public Node findClosestNode(Point point) {
        Node node = null;
        double minDis = Double.POSITIVE_INFINITY;
        for (int n = 0; n < nodes.size(); ++n) {
            double dis = point.distanceTo(nodes.get(n).getPoint());
            if (dis < minDis) {
                node = nodes.get(n);
                minDis = dis;
            }
        }
        return node;
    }

    /**
     * @return Map ID of this graph.
     */
    public int getMapId() {
        return mapId;
    }

    /**
     * @return Return the transpose graph of this graph.
     */
    public Graph transpose() {
        // TODO:
        return null;
    }

}
