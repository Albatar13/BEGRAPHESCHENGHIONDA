package org.insa.graph;

/**
 * Utility class that stores some information for a graph that are no easy to
 * access quickly.
 *
 */
public class GraphInformation {

    // Maximum speed on this graph (in kmph).
    private final int maximumSpeed;

    // Maximum length of any arc on this graph.
    private final int maximumLength;

    /**
     * @param maximumSpeed
     * @param maximumLength
     */
    public GraphInformation(int maximumSpeed, int maximumLength) {
        this.maximumLength = maximumLength;
        this.maximumSpeed = maximumSpeed;
    }

    /**
     * @return Maximum speed of any arc in the graph.
     */
    public int getMaximumSpeed() {
        return this.maximumSpeed;
    }

    /**
     * @return Maximum length of any arc in the graph.
     */
    public int getMaximumLength() {
        return this.maximumLength;
    }

}
