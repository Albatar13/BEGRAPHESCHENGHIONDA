package org.insa.graph.io;

import java.io.IOException;

import org.insa.graph.Graph;

public interface GraphReader {

    /**
     * Add a new observer to this graph reader.
     * 
     * @param observer
     */
    public void addObserver(GraphReaderObserver observer);

    /**
     * Read a graph an returns it.
     * 
     * @return Graph.
     * @throws Exception
     * 
     */
    public Graph read() throws IOException;

}
