package org.insa.algo.weakconnectivity;

import java.time.Duration;
import java.util.ArrayList;

import org.insa.algo.AbstractSolution;
import org.insa.graph.Node;

public class WeaklyConnectedComponentsSolution extends AbstractSolution {
	
	// Components
	private ArrayList<ArrayList<Node>> components;

	protected WeaklyConnectedComponentsSolution(WeaklyConnectedComponentsInstance instance) {
		super(instance);
	}
	
	protected WeaklyConnectedComponentsSolution(WeaklyConnectedComponentsInstance instance, 
					   Duration solvingTime, Status status, ArrayList<ArrayList<Node>> components) {
		super(instance, solvingTime, status);
		this.components = components;
	}
	
	/**
	 * @return Components of the solution, if any.
	 */
	public ArrayList<ArrayList<Node>> getComponents() { return components; }

}
