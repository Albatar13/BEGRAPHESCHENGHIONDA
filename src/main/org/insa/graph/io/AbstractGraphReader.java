package org.insa.graph.io;

import org.insa.graph.Graph;

public interface AbstractGraphReader {
	
	/**
	 * Read a graph an returns it.
	 * 
	 * @return Graph.
	 * @throws Exception 
	 * 
	 */
	public Graph read() throws Exception;

}
