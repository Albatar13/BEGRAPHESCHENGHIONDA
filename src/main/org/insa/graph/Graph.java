package org.insa.graph;

import java.util.Collections;
import java.util.List;

public class Graph {

    // Map identifier.
    private final String mapId;

    // Map name
    private final String mapName;

    // Nodes of the graph.
    private final List<Node> nodes;

    // Graph information of this graph.
    private final GraphInformation graphInfo;

    /**
     * @param mapId ID of this graph.
     * @param list List of nodes for this graph.
     */
    public Graph(String mapId, String mapName, List<Node> list, GraphInformation graphInformation) {
        this.mapId = mapId;
        this.mapName = mapName;
        this.nodes = list;
        this.graphInfo = graphInformation;
    }

    /**
     * @return GraphInformation of this graph.
     */
    public GraphInformation getGraphInformation() {
        return this.graphInfo;
    }

    /**
     * @return Immutable list of nodes of this graph.
     */
    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    /**
     * @return Map ID of this graph.
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
     * @return Return the transpose graph of this graph.
     */
    public Graph transpose() {
        // TODO:
        return null;
    }

}
