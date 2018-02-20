package org.insa.algo.weakconnectivity;

import java.util.ArrayList;

import org.insa.algo.AbstractSolution;
import org.insa.graph.Node;

public class WeaklyConnectedComponentsSolution extends AbstractSolution {
	
	// Components
	private ArrayList<ArrayList<Node>> components;

	protected WeaklyConnectedComponentsSolution(WeaklyConnectedComponentsData instance) {
		super(instance);
	}
	
	protected WeaklyConnectedComponentsSolution(WeaklyConnectedComponentsData instance, 
					   Status status, ArrayList<ArrayList<Node>> components) {
		super(instance, status);
		this.components = components;
	}
	
	/**
	 * @return Components of the solution, if any.
	 */
	public ArrayList<ArrayList<Node>> getComponents() { return components; }

}
