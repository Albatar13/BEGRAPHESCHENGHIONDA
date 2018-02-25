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
     * Show or hide this marker - A marker should be visible when created.
     * 
     * @param visible true to show the marker, false to hide.
     */
    public void setVisible(boolean visible);

    /**
     * Delete this marker.
     */
    public void delete();

}
