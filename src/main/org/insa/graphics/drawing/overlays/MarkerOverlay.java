package org.insa.graphics.drawing.overlays;

import org.insa.graph.Point;

public interface MarkerOverlay extends Overlay {

    /**
     * @return The point associated with this marker.
     */
    public Point getPoint();

    /**
     * 
     */
    public void moveTo(Point point);

}
