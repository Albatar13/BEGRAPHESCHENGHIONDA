package org.insa.graph;

/**
 * Utility class that stores some statistics of graphs that are not easy to
 * access.
 * 
 * This class is used to provide O(1) access to information in graph that do not
 * change, and that usually require O(n) to compute.
 *
 */
public class GraphStatistics {

    // Maximum speed on this graph (in kmph).
    private final int maximumSpeed;

    // Maximum length of any arc on this graph.
    private final float maximumLength;

    /**
     * Create a new GraphStatistics instance with the given value.
     * 
     * @param maximumSpeed Maximum speed of any road of the graph. A value of 0 may
     * be used to indicate that this graph has no maximum limitation.
     * @param maximumLength Maximum length of any arc of the graph.
     */
    public GraphStatistics(int maximumSpeed, float maximumLength) {
        this.maximumLength = maximumLength;
        this.maximumSpeed = maximumSpeed;
    }

    /**
     * @return Maximum speed of any arc in the graph, or 0 if some road have no
     * speed limitations.
     */
    public int getMaximumSpeed() {
        return this.maximumSpeed;
    }

    /**
     * @return Maximum length of any arc in the graph.
     */
    public float getMaximumLength() {
        return this.maximumLength;
    }

}
