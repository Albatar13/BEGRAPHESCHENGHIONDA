package org.insa.graphs.model.io;

import java.io.Closeable;
import java.io.IOException;

import org.insa.graphs.model.Path;

/**
 * Base interface that should be implemented by class used to write paths.
 *
 */
public interface PathWriter extends Closeable {

    /**
     * Write the given path.
     * 
     * @param path Path to write.
     * 
     * @throws IOException When an error occurs while writing the path.
     */
    public void writePath(Path path) throws IOException;

    /**
     * Close this graph reader.
     * 
     * @throws IOException if an exception occurs while closing the reader.
     * 
     */
    public void close() throws IOException;

}
