package org.insa.graphs.model.io;

import java.io.Closeable;
import java.io.IOException;

import org.insa.graphs.model.Graph;

/**
 * Base interface for classes that can read graph.
 *
 */
public interface GraphReader extends Closeable {

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
     * @throws IOException if an exception occurs while reading the graph.
     * 
     */
    public Graph read() throws IOException;

    /**
     * Close this graph reader.
     * 
     * @throws IOException if an exception occurs while closing the reader.
     * 
     */
    public void close() throws IOException;

}
