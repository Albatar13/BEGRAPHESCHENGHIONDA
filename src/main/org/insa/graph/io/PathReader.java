package org.insa.graph.io;

import java.io.IOException;

import org.insa.graph.Graph;
import org.insa.graph.Path;

/**
 * Base interface that should be implemented by class used to read paths.
 *
 */
public interface PathReader {

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

}
