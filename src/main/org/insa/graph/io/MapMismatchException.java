package org.insa.graph.io;

import java.io.IOException;

public class MapMismatchException extends IOException {

    /**
     * 
     */
    private static final long serialVersionUID = 3076730078387819138L;
    // Actual and expected magic numbers.
    private String actualMapId, expectedMapId;

    /**
     * 
     * @param actualVersion
     * @param expectedVersion
     */
    public MapMismatchException(String actualMapId, String expectedMapId) {
        super();
        this.actualMapId = actualMapId;
        this.expectedMapId = expectedMapId;
    }

    /**
     * @return
     */
    public String getActualMapId() {
        return actualMapId;
    }

    /**
     * @return
     */
    public String getExpectedMapId() {
        return expectedMapId;
    }
}
