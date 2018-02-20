package org.insa.algo;

import org.insa.graph.Graph;

public abstract class AbstractData {

	protected Graph graph;
	
	/**
	 * Create a new abstract instance with the given graph.
	 * 
	 * @param graph
	 */
	protected AbstractData(Graph graph) {
		this.graph = graph;
	}
	
	public Graph getGraph() { return graph; }
	
}
