package org.insa.graph.io;

import java.io.IOException;

import org.insa.graph.Graph;

public interface GraphReader {

    /**
     * Read a graph an returns it.
     * 
     * @return Graph.
     * @throws Exception
     * 
     */
    public Graph read() throws IOException;

}
