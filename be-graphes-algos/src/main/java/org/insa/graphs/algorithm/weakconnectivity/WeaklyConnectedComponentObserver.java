package org.insa.graphs.algorithm.weakconnectivity;

import java.util.ArrayList;

import org.insa.graphs.model.Node;

public interface WeaklyConnectedComponentObserver {

	/**
	 * Notify that the algorithm is entering a new component.
	 * 
	 * @param curNode Starting node for the component.
	 */
	public void notifyStartComponent(Node curNode);
	
	/**
	 * Notify that a new node has been found for the current component.
	 * 
	 * @param node New node found for the current component.
	 */
	public void notifyNewNodeInComponent(Node node);
	
	/**
	 * Notify that the algorithm has computed a new component.
	 * 
	 * @param nodes List of nodes in the component.
	 */
	public void notifyEndComponent(ArrayList<Node> nodes);

}
