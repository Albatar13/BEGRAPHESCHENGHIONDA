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
     * Special value used to indicate that the graph has no maximum speed limit
     * (some roads are not limited).
     */
    public static final int NO_MAXIMUM_SPEED = -1;

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

        /**
         * Create a new bounding box by extending the current one according to the given
         * value for each side.
         * 
         * @param left Extra size to add to the left of the box.
         * @param top Extra size to add to the top of the box.
         * @param right Extra size to add to the right of the box.
         * @param bottom Extra size to add to the bottom of the box.
         * 
         * @return New bounding box corresponding to an extension of the current one.
         */
        public BoundingBox extend(float left, float top, float right, float bottom) {
            return new BoundingBox(
                    new Point(this.topLeft.getLongitude() - left, this.topLeft.getLatitude() + top),
                    new Point(this.bottomRight.getLongitude() + right,
                            this.bottomRight.getLatitude() - bottom));
        }

        /**
         * Create a new bounding box by extending the current one according by the given
         * value on each side.
         * 
         * @param size Extra size to add to each side of this box.
         * 
         * @return New bounding box corresponding to an extension of the current one.
         */
        public BoundingBox extend(float size) {
            return this.extend(size, size, size, size);
        }

        /**
         * @param point Point to check
         * 
         * @return true if this box contains the given point.
         */
        public boolean contains(Point point) {
            return this.bottomRight.getLatitude() <= point.getLatitude()
                    && this.topLeft.getLatitude() >= point.getLatitude()
                    && this.topLeft.getLongitude() <= point.getLongitude()
                    && this.bottomRight.getLongitude() >= point.getLongitude();
        }

        /**
         * @param other Box to intersect.
         * 
         * @return true if this box contains the given box.
         */
        public boolean contains(BoundingBox other) {
            return this.contains(other.bottomRight) && this.contains(other.topLeft);
        }

        @Override
        public String toString() {
            return "BoundingBox(topLeft=" + this.topLeft + ", bottomRight=" + this.bottomRight
                    + ")";
        }

    }

    // Bounding box for this graph.
    private final BoundingBox boundingBox;

    // Number of roads
    private final int nbRoadOneWay, nbRoadTwoWays;

    // Maximum speed on this graph (in kmph).
    private final int maximumSpeed;

    // Maximum length of any arc on this graph.
    private final float maximumLength;

    /**
     * Create a new GraphStatistics instance with the given value.
     * 
     * @param boundingBox Bounding-box for the graph.
     * @param nbRoadOneWay Number of one-way roads in the graph.
     * @param nbRoadTwoWays Number of two-ways roads in the graph.
     * @param maximumSpeed Maximum speed of any road of the graph. You can use
     *        {@link #NO_MAXIMUM_SPEED} to indicate that the graph has no maximum
     *        speed limit.
     * @param maximumLength Maximum length of any arc of the graph.
     */
    public GraphStatistics(BoundingBox boundingBox, int nbRoadOneWay, int nbRoadTwoWays,
            int maximumSpeed, float maximumLength) {
        this.boundingBox = boundingBox;
        this.nbRoadOneWay = nbRoadOneWay;
        this.nbRoadTwoWays = nbRoadTwoWays;
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
     * @return Amount of one-way roads in this graph.
     */
    public int getOneWayRoadCount() {
        return this.nbRoadOneWay;
    }

    /**
     * @return Amount of two-ways roads in this graph.
     */
    public int getTwoWaysRoadCount() {
        return this.nbRoadTwoWays;
    }

    /**
     * @return Number of arcs in this graph.
     * 
     * @see #getOneWayRoadCount()
     * @see #getTwoWaysRoadCount()
     */
    public int getArcCount() {
        return getOneWayRoadCount() + 2 * getTwoWaysRoadCount();
    }

    /**
     * @return true if this graph has a maximum speed limit, false otherwise.
     */
    public boolean hasMaximumSpeed() {
        return this.maximumLength != NO_MAXIMUM_SPEED;
    }

    /**
     * @return Maximum speed of any arc in the graph, or {@link #NO_MAXIMUM_SPEED}
     *         if some roads have no speed limitation.
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
