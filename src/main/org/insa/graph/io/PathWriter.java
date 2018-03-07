package org.insa.graph.io;

import java.io.IOException;

import org.insa.graph.Path;

/**
 * Base interface that should be implemented by class used to write paths.
 *
 */
public interface PathWriter {

    /**
     * Write the given path.
     * 
     * @param path Path to write.
     * 
     * @throws IOException When an error occurs while writing the path.
     */
    public void writePath(Path path) throws IOException;

}
