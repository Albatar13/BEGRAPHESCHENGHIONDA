package org.insa.graph.io;

/**
 * Exception thrown when the version of the file is not at least the expected
 * one.
 *
 */
public class BadVersionException extends BadFormatException {

    /**
     * 
     */
    private static final long serialVersionUID = 7776317018302386042L;

    // Actual and expected version..
    private int actualVersion, expectedVersion;

    /**
     * 
     * @param actualVersion Actual version of the file.
     * @param expectedVersion Expected version of the file.
     */
    public BadVersionException(int actualVersion, int expectedVersion) {
        super();
        this.actualVersion = actualVersion;
        this.expectedVersion = expectedVersion;
    }

    /**
     * @return Actual version of the file.
     */
    public int getActualVersion() {
        return actualVersion;
    }

    /**
     * @return Expected (minimal) version of the file.
     */
    public int getExpectedVersion() {
        return expectedVersion;
    }
}
