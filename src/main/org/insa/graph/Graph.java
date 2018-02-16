package org.insa.graph ;

import java.util.ArrayList;

public class Graph {

	// Map identifier.
	private int mapId;
	
	// Nodes of the graph.
	private ArrayList<Node> nodes;

	/**
	 * @param mapId
	 * @param nodes
	 */
	public Graph(int mapId, ArrayList<Node> nodes) {
		this.mapId = mapId;
		this.nodes = nodes;
	}
	
	/**
	 * @return Nodes of this graph.
	 */
	public ArrayList<Node> getNodes() { return nodes; }
	
	/**
	 * @return Map ID of this graph.
	 */
	public int getMapId() { return mapId; }
	
	/**
	 * @return Return the transpose graph of this graph.
	 */
	public Graph transpose() {
		// TODO: 
		return null;
	}

}
