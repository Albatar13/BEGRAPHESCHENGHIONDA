package org.insa.algo.weakconnectivity;

import java.io.PrintStream;
import java.util.ArrayList;

import org.insa.graph.Node;

public class WeaklyConnectedComponentTextObserver implements WeaklyConnectedComponentObserver {
	
	// Number of the current component.
	private int numComponent = 1;
	
	// Output stream
	PrintStream stream;

	public WeaklyConnectedComponentTextObserver(PrintStream stream) {
		this.stream = stream;
	}

	@Override
	public void notifyStartComponent(Node curNode) {
		stream.print("Entering component #" + numComponent + " from node #" + curNode.getId() + "... ");
	}

	@Override
	public void notifyNewNodeInComponent(Node node) {
	}

	@Override
	public void notifyEndComponent(ArrayList<Node> nodes) {
		stream.println(nodes.size() + " nodes found.");
		stream.flush();
		numComponent += 1;
	}

}
