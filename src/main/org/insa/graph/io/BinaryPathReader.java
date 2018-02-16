package org.insa.graph.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class BinaryPathReader extends BinaryReader implements AbstractPathReader {

	// Map version and magic number targeted for this reader.
	private static final int VERSION = 1;
	private static final int MAGIC_NUMBER = 0xdecafe;

	public BinaryPathReader(DataInputStream dis) {
		super(MAGIC_NUMBER, VERSION, dis);
	}

	@Override
	public Path readPath(Graph graph) throws Exception {
		
		// Read and check magic number and version.
		checkMagicNumberOrThrow(dis.readInt());
		checkVersionOrThrow(dis.readInt());
		
		// Read map ID and check against graph.
		int mapId = dis.readInt();
		
		if (mapId != graph.getMapId()) {
			throw new MapMismatchException(mapId, graph.getMapId());
		}
		
		// Number of nodes in the path (without first and last).
		int nbNodes = dis.readInt();
		
		ArrayList<Arc> arcs = new ArrayList<Arc>();
		
		// Skip (duplicate) first and last node
		readNode(graph);
		readNode(graph);
		
		// Read intermediate nodes:
		ArrayList<Node> nodes = new ArrayList<Node>();
		for (int i = 0; i < nbNodes; ++i) {
			nodes.add(readNode(graph));
		}
		
		Node current = nodes.get(0);
		for (int i = 1; i < nodes.size(); ++i) {
			Node node = nodes.get(i);
			Arc minArc = null;
			for (Arc arc: current.getSuccessors()) {
				if (arc.getDestination().equals(node)
					&& (minArc == null || arc.getMinimumTravelTime() < minArc.getMinimumTravelTime())) {
					minArc = arc;
				}
			}
			arcs.add(minArc);
			if (minArc == null) {
				System.out.println("No arc found between nodes " + current.getId() + " and " + node.getId() + "\n");
			}
			current = node;
		}
		
		return new Path(graph, nodes.get(0), arcs);
	}

	/**
	 * Read a node from the stream and returns id.
	 * 
	 * @return
	 * @throws IOException 
	 */
	protected Node readNode(Graph graph) throws IOException {
		// Discard zone.
		dis.readUnsignedByte();
		
		return graph.getNodes().get(read24bits());
	}

}
