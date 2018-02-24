package org.insa.graphics.drawing;

import org.insa.graph.Point;

public interface DrawingClickListener {

    /**
     * Event triggered when a click is made on the map.
     * 
     * @param point
     */
    public void mouseClicked(Point point);

}
