package org.insa.graph.io;

import java.io.IOException;

import org.insa.graph.Path;

public interface AbstractPathWriter {

    /**
     * Write a path.
     * 
     * @param path Path to write.
     * 
     * @throws Exception
     */
    public void writePath(Path path) throws IOException;

}
