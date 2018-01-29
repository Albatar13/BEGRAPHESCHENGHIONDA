package org.insa.graph.io;

import org.insa.graph.Graph;
import org.insa.graph.Path;

public interface AbstractPathReader {

	/**
	 * Read a path of the given graph and returns it.
	 * 
	 * @param graph Graph of the path.
	 * 
	 * @return A new path.
	 * @throws Exception 
	 */
	public Path readPath(Graph graph) throws Exception;
	
}
