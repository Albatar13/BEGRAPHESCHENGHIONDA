package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.insa.algo.AbstractSolution.Status;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class BellmanFordAlgorithm extends ShortestPathAlgorithm {

	public BellmanFordAlgorithm(ShortestPathData data) {
		super(data);
	}

	@Override
	protected ShortestPathSolution doRun() {

		// Retrieve the graph.
		ShortestPathData data = getInputData();
		Graph graph = data.getGraph();

		final int nbNodes = graph.size();

		// Initialize array of distances.
		double[] distances = new double[nbNodes];
		Arrays.fill(distances, Double.POSITIVE_INFINITY);
		distances[data.getOrigin().getId()] = 0;

		// Notify observers about the first event (origin processed).
		notifyOriginProcessed(data.getOrigin());

		// Initialize array of predecessors.
		Arc[] predecessorArcs = new Arc[nbNodes];

		// Actual algorithm, we will assume the graph does not contain negative
		// cycle...
		boolean found = false;
		for (int i = 0; !found && i < nbNodes; ++i) {
			found = true;
			for (Node node : graph) {
				for (Arc arc : node) {

					// Small test to check allowed roads...
					if (!data.isAllowed(arc)) {
						continue;
					}

					// Retrieve weight of the arc.
					double w = data.getCost(arc);
					double oldDistance = distances[arc.getDestination().getId()];
					double newDistance = distances[node.getId()] + w;

					if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
						notifyNodeReached(arc.getDestination());
					}

					// Check if new distances would be better, if so update...
					if (newDistance < oldDistance) {
						found = false;
						distances[arc.getDestination().getId()] = distances[node.getId()] + w;
						predecessorArcs[arc.getDestination().getId()] = arc;
					}
				}
			}
		}

		ShortestPathSolution solution = null;

		// Destination has no predecessor, the solution is infeasible...
		if (predecessorArcs[data.getDestination().getId()] == null) {
			solution = new ShortestPathSolution(data, Status.INFEASIBLE);
		} else {

			// The destination has been found, notify the observers.
			notifyDestinationReached(data.getDestination());

			// Create the path from the array of predecessors...
			ArrayList<Arc> arcs = new ArrayList<>();
			Arc arc = predecessorArcs[data.getDestination().getId()];
			while (arc != null) {
				arcs.add(arc);
				arc = predecessorArcs[arc.getOrigin().getId()];
			}

			// Reverse the path...
			Collections.reverse(arcs);

			// Create the final solution.
			solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
		}

		return solution;
	}

}
