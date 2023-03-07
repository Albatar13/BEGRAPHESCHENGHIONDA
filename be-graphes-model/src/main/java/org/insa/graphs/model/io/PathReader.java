package org.insa.graphs.model.io;

import java.io.Closeable;
import java.io.IOException;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;

/**
 * Base interface that should be implemented by class used to read paths.
 *
 */
public interface PathReader extends Closeable {

    /**
     * Read a path of the given graph and returns it.
     * 
     * @param graph Graph of the path.
     * 
     * @return Path read.
     * 
     * @throws IOException When an error occurs while reading the path.
     */
    public Path readPath(Graph graph) throws IOException;

    /**
     * Close this graph reader.
     * 
     * @throws IOException if an exception occurs while closing the reader.
     * 
     */
    public void close() throws IOException;

}
