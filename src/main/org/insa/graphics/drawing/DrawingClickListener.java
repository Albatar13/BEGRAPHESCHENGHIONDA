package org.insa.graphics.drawing;

import org.insa.graph.Point;

public interface DrawingClickListener {

    /**
     * Event triggered when a click is made on the map.
     * 
     * @param point Position (on the map) of the mouse click.
     */
    public void mouseClicked(Point point);

}
