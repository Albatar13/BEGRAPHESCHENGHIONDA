package org.insa.algo.strongconnectivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import org.insa.algo.AbstractSolution.Status;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;

public class TarjanAlgorithm extends StronglyConnectedComponentsAlgorithm {
	
	private final static int UNDEFINED = -1;

	// Stack of nodes and flags.
	private Stack<Node> stack;
	private boolean[] inStack;
	
	// Current index.
	private int index;
	
	// Information of nodes
	private int[] indexes;
	private int[] lowlink;	
	
	// Array of strongly connected components
	ArrayList<ArrayList<Node>> components;

	public TarjanAlgorithm(StronglyConnectedComponentsData instance) {
		super(instance);
	}
	
	/**
	 * Push the given node to the stack.
	 * 
	 * @param node
	 */
	protected void pushNode(Node node) {
		stack.push(node);
		inStack[node.getId()] = true;
	}
	
	/**
	 * Pop and return a node from the stack.
	 * 
	 * @return Node popped from the stack
	 */
	protected Node popNode() {
		Node top = stack.pop();
		inStack[top.getId()] = false;
		return top;
	}
	
	/**
	 * Check if the given node is in the stack.
	 * 
	 * @param node
	 * 
	 * @return true if the given node is in the stack, false otherwize.
	 */
	protected boolean isInStack(Node node) {
		return inStack[node.getId()];
	}
	
	/**
	 * Find the strong component containing the given node.
	 * 
	 * @param node 
	 * 
	 * @return The strong component containing the given node.
	 */
	protected void findAndAddStrongComponent(Node v) {
		
		// Update node info, index and push the node.
		indexes[v.getId()] = index;
		lowlink[v.getId()] = index;
		index += 1;
		pushNode(v);
		
		for (Arc a: v.getSuccessors()) {
			Node w = a.getDestination();
			if (!hasBeenVisited(w)) {
				findAndAddStrongComponent(w);
				lowlink[v.getId()] = Math.min(lowlink[v.getId()], lowlink[w.getId()]);
			}
			else if (isInStack(w)) {
				lowlink[v.getId()] = Math.min(lowlink[v.getId()], indexes[w.getId()]);
			}
		}
		
		// Compute the component (if any)
		if (lowlink[v.getId()] == indexes[v.getId()]) {
			ArrayList<Node> component = new ArrayList<Node>();
			Node w;
			do {
				w = popNode();
				component.add(w);
			} while (!w.equals(v));
			components.add(component);
			System.out.println("Size of the stack: " + stack.size());
		}
				
	}
	
	/**
	 * Check if the given node has not been visited yet.
	 * 
	 * @return true if the node has been visited.
	 */
	protected boolean hasBeenVisited(Node node) { 
		return this.indexes[node.getId()] != UNDEFINED;
	}

	@Override
	protected StronglyConnectedComponentsSolution doRun() {
		Graph graph = getInstance().getGraph();
		
		components = new ArrayList<ArrayList<Node>>();
		
		// Initialize everything
		final int nbNodes = graph.getNodes().size();
		stack = new Stack<Node>();
		inStack = new boolean[nbNodes];
		
		// Current index.
		index = 0;
		
		// Information of nodes
		indexes = new int[nbNodes];
		Arrays.fill(indexes, UNDEFINED);
		lowlink = new int[nbNodes];
		
		// Find components
		for (Node node: graph.getNodes()) {
			if (!hasBeenVisited(node)) {
				findAndAddStrongComponent(node);
			}
		}
		
		return new StronglyConnectedComponentsSolution(getInstance(), Status.OPTIMAL, components);
	}

}
