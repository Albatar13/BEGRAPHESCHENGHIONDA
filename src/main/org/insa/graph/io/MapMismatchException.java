package org.insa.graph.io;

import java.io.IOException;

/**
 * Exception thrown when there is mismatch between the expected map ID and the
 * actual map ID when reading a graph.
 *
 */
public class MapMismatchException extends IOException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Actual and expected map ID.
    private String actualMapId, expectedMapId;

    /**
     * Create a new MapMismatchException with the given IDs.
     * 
     * @param actualMapId Actual map ID found when reading the path.
     * @param expectedMapId Expected map ID from the graph.
     */
    public MapMismatchException(String actualMapId, String expectedMapId) {
        super();
        this.actualMapId = actualMapId;
        this.expectedMapId = expectedMapId;
    }

    /**
     * @return Actual ID of the map (read from the path).
     */
    public String getActualMapId() {
        return actualMapId;
    }

    /**
     * @return Expected ID of the map.
     */
    public String getExpectedMapId() {
        return expectedMapId;
    }
}
