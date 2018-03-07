package org.insa.graph.io;

import java.io.IOException;

/**
 * Exception thrown when a format-error is detected when reading a graph (e.g.,
 * non-matching check bytes).
 *
 */
public class BadFormatException extends IOException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public BadFormatException() {
        super();
    }

}
