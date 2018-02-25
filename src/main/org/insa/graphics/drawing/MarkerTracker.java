package org.insa.graphics.drawing;

import org.insa.graph.Point;

public interface MarkerTracker extends OverlayTracker {

    /**
     * @return The point associated with this marker.
     */
    public Point getPoint();

    /**
     * 
     */
    public void moveTo(Point point);

}
