package org.insa.graph;

import java.util.Collections;
import java.util.List;

/**
 * Main graph class.
 * 
 * This class acts as a object-oriented adjacency list for a graph, i.e. it
 * holds a list of nodes and each node holds a list of its successors.
 *
 */
public class Graph {

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
        this.nodes = nodes;
        this.graphStatistics = graphStatistics;
    }

    /**
     * @return The GraphStatistics instance associated with this graph.
     */
    public GraphStatistics getGraphInformation() {
        return this.graphStatistics;
    }

    /**
     * @return Immutable view of the list of nodes of this graph.
     */
    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
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
        // TODO:
        return null;
    }

}
