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

    /**
     * Class representing a bounding box for a graph (a rectangle that contains all
     * nodes in the graph).
     *
     */
    public static class BoundingBox {

        private final Point topLeft, bottomRight;

        /**
         * Create a new BoundingBox represented by the given top-left and bottom-right
         * points.
         * 
         * @param topLeft Top left corner of the bounding box.
         * @param bottomRight Bottom right corner of the bounding box.
         */
        public BoundingBox(Point topLeft, Point bottomRight) {
            this.topLeft = topLeft;
            this.bottomRight = bottomRight;
        }

        /**
         * @return Bottom-right point of this boundin box.
         */
        public Point getBottomRightPoint() {
            return bottomRight;
        }

        /**
         * @return Top-left point of this bounding box.
         */
        public Point getTopLeftPoint() {
            return topLeft;
        }

    }

    // Bounding box for this graph.
    private final BoundingBox boundingBox;

    // Maximum speed on this graph (in kmph).
    private final int maximumSpeed;

    // Maximum length of any arc on this graph.
    private final float maximumLength;

    /**
     * Create a new GraphStatistics instance with the given value.
     * 
     * @param maximumSpeed Maximum speed of any road of the graph. A value of 0 may
     *        be used to indicate that this graph has no maximum limitation.
     * @param maximumLength Maximum length of any arc of the graph.
     */
    public GraphStatistics(BoundingBox boundingBox, int maximumSpeed, float maximumLength) {
        this.boundingBox = boundingBox;
        this.maximumLength = maximumLength;
        this.maximumSpeed = maximumSpeed;
    }

    /**
     * @return The bounding box for this graph.
     */
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    /**
     * @return Maximum speed of any arc in the graph, or 0 if some road have no
     *         speed limitations.
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
