package org.insa.graph.io;

import java.io.IOException;

import org.insa.graph.Graph;

/**
 * Base interface for classes that can read graph.
 *
 */
public interface GraphReader {

    /**
     * Add a new observer to this graph reader.
     * 
     * @param observer Observer to add.
     */
    public void addObserver(GraphReaderObserver observer);

    /**
     * Read a graph an returns it.
     * 
     * @return The graph read.
     * 
     * @throws IOException When an exception occurs while reading the graph.
     * 
     */
    public Graph read() throws IOException;

}
