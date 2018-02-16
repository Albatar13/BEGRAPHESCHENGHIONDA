package org.insa.algo.weakconnectivity;

import java.util.ArrayList;

import org.insa.algo.AbstractObserver;
import org.insa.graph.Node;

public abstract class WeaklyConnectedComponentObserver extends AbstractObserver {

	/**
	 * {@inheritDoc}
	 */
	protected WeaklyConnectedComponentObserver(boolean isGraphic) {
		super(isGraphic);
	}
	
	/**
	 * Notify that the algorithm is entering a new component.
	 * 
	 * @param curNode Starting node for the component.
	 */
	public abstract void notifyStartComponent(Node curNode);
	
	/**
	 * Notify that a new node has been found for the current component.
	 * 
	 * @param node New node found for the current component.
	 */
	public abstract void notifyNewNodeInComponent(Node node);
	
	/**
	 * Notify that the algorithm has computed a new component.
	 * 
	 * @param nodes List of nodes in the component.
	 */
	public abstract void notifyEndComponent(ArrayList<Node> nodes);

}
