package org.insa.algo.weakconnectivity;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashSet;

import org.insa.algo.AbstractAlgorithm;
import org.insa.algo.AbstractObserver;
import org.insa.algo.AbstractSolution;
import org.insa.algo.AbstractSolution.Status;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;

public class WeaklyConnectedComponentsAlgorithm extends AbstractAlgorithm {

	/**
	 * 
	 * @param instance
	 * @param logOutput
	 */
	public WeaklyConnectedComponentsAlgorithm(WeaklyConnectedComponentsInstance instance) {
		super(instance);
	}
	
	/**
	 * @return An adjacency list for the undirected graph equivalent to the stored graph.
	 */
	protected ArrayList<HashSet<Integer>> createUndirectedGraph() {
		int nNodes = getInstance().getGraph().getNodes().size();
		ArrayList<HashSet<Integer>> res = new ArrayList<HashSet<Integer>>(nNodes);
		for (int i = 0; i < nNodes; ++i) {
			res.add(new HashSet<Integer>());
		}		
		
		for (Node node: getInstance().getGraph().getNodes()) {
			for (Arc arc: node.getSuccessors()) {
				res.get(node.getId()).add(arc.getDestination().getId());
				if (arc.getInfo().isOneWay()) {
					res.get(arc.getDestination().getId()).add(node.getId());
				}
			}
		}
		
		return res;
	}
	
	/**
	 * Apply a breadth first search algorithm on the given undirected graph (adjacency list),
	 * starting at node cur, and marking nodes in marked.
	 * 
	 * @param marked
	 * @param cur
	 * 
	 * @return
	 */
	protected ArrayList<Node> bfs(ArrayList<HashSet<Integer>> ugraph, boolean[] marked, int cur) {
		ArrayList<Node> nodes = getInstance().getGraph().getNodes();
		ArrayList<Node> component = new ArrayList<Node>();
		
		// Using a queue because we are doing a BFS
		Queue<Integer> queue = new LinkedList<Integer>();

		for (AbstractObserver obs: getObservers()) {
			((WeaklyConnectedComponentObserver)obs).notifyStartComponent(nodes.get(cur));
		}
		
		// Add original node and loop until the queue is empty.
		queue.add(cur);
		marked[cur] = true;
		while (!queue.isEmpty()) {
			Node node = nodes.get(queue.remove());
			component.add(node);
			
			// notify observers
			for (AbstractObserver obs: getObservers()) ((WeaklyConnectedComponentObserver)obs).notifyNewNodeInComponent(node);
			
			for (Integer destId: ugraph.get(node.getId())) {
				Node dest = nodes.get(destId);
				if (!marked[dest.getId()]) {
					queue.add(destId);
					marked[destId] = true;
				}
			}
		}
		
		for (AbstractObserver obs: getObservers()) {
			((WeaklyConnectedComponentObserver)obs).notifyEndComponent(component);
		}
		
		return component;
	}

	@Override
	protected AbstractSolution doRun() {
		
		Instant start = Instant.now();

		Graph graph = getInstance().getGraph();
		ArrayList<HashSet<Integer>> ugraph = createUndirectedGraph();
		boolean[] marked = new boolean[graph.getNodes().size()];
		Arrays.fill(marked, false);
		
		ArrayList<ArrayList<Node>> components = new ArrayList<ArrayList<Node>>();
		
		// perform algorithm
		int cur = 0;
		while (cur < marked.length) {
			// Apply BFS
			components.add(this.bfs(ugraph, marked, cur));
			
			// Find next non-marked
			for (; cur < marked.length && marked[cur]; ++cur);
		}
		
		Duration solvingTime = Duration.between(start, Instant.now());
		
		return new WeaklyConnectedComponentsSolution((WeaklyConnectedComponentsInstance)getInstance(), 
				solvingTime, Status.OPTIMAL, components);
	}

}
