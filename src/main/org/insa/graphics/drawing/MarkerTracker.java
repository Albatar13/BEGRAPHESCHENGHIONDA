package org.insa.graphics.drawing;

import org.insa.graph.Point;

public interface MarkerTracker {

    /**
     * @return The point associated with this marker.
     */
    public Point getPoint();

    /**
     * 
     */
    public void moveTo(Point point);

    /**
     * Delete this marker.
     */
    public void delete();

}
