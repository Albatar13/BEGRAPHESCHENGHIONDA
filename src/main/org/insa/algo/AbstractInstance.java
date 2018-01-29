package org.insa.algo;

import org.insa.graph.Graph;

public abstract class AbstractInstance {

	protected Graph graph;
	
	/**
	 * Create a new abstract instance with the given graph.
	 * 
	 * @param graph
	 */
	protected AbstractInstance(Graph graph) {
		this.graph = graph;
	}
	
	public Graph getGraph() { return graph; }
	
}
