package org.insa.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Main graph class.
 * 
 * This class acts as a object-oriented adjacency list for a graph, i.e. it
 * holds a list of nodes and each node holds a list of its successors.
 *
 */
public final class Graph implements Iterable<Node> {

    // Map identifier.
    private final String mapId;

    // Map name
    private final String mapName;

    // Nodes of the graph.
    private final List<Node> nodes;

    // Graph information of this graph.
    private final GraphStatistics graphStatistics;

    /**
     * Create a new graph with the given ID, name, nodes and information.
     * 
     * @param mapId ID of the map corresponding to this graph.
     * @param mapName Name of the map corresponding to this graph.
     * @param nodes List of nodes for this graph.
     * @param graphStatistics Information for this graph.
     */
    public Graph(String mapId, String mapName, List<Node> nodes, GraphStatistics graphStatistics) {
        this.mapId = mapId;
        this.mapName = mapName;
        this.nodes = Collections.unmodifiableList(nodes);
        this.graphStatistics = graphStatistics;
    }

    /**
     * @return The GraphStatistics instance associated with this graph.
     */
    public GraphStatistics getGraphInformation() {
        return this.graphStatistics;
    }

    /**
     * Fetch the node with the given ID.
     * 
     * Complexity: O(1).
     * 
     * @param id ID of the node to fetch.
     * 
     * @return Node with the given ID.
     */
    public Node get(int id) {
        return this.nodes.get(id);
    }

    /**
     * @return Number of nodes in this graph.
     */
    public int size() {
        return this.nodes.size();
    }

    @Override
    public Iterator<Node> iterator() {
        return this.nodes.iterator();
    }

    /**
     * @return ID of the map associated with this graph.
     */
    public String getMapId() {
        return mapId;
    }

    /**
     * @return Name of the map associated with this graph.
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * @return Transpose graph of this graph.
     */
    public Graph transpose() {
        ArrayList<Node> trNodes = new ArrayList<>(nodes.size());
        for (Node node: nodes) {
            trNodes.add(new Node(node.getId(), node.getPoint()));
        }
        for (Node node: nodes) {
            Node orig = trNodes.get(node.getId());
            for (Arc arc: node) {
                if (arc.getRoadInformation().isOneWay()) {
                    Node dest = trNodes.get(arc.getDestination().getId());
                    dest.addSuccessor(new ArcBackward(new ArcForward(orig, dest, arc.getLength(),
                            arc.getRoadInformation(), arc.getPoints())));
                }
                else if (arc instanceof ArcForward) {
                    Node dest = trNodes.get(arc.getDestination().getId());
                    Arc newArc = new ArcForward(orig, dest, arc.getLength(),
                            arc.getRoadInformation(), arc.getPoints());
                    dest.addSuccessor(new ArcBackward(newArc));
                    orig.addSuccessor(newArc);
                }
            }
        }
        return new Graph("R/" + mapId, mapName, trNodes, graphStatistics);
    }

}
