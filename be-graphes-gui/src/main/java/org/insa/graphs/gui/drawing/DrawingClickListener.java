package org.insa.graphs.gui.drawing;

import org.insa.graphs.model.Point;

public interface DrawingClickListener {

    /**
     * Event triggered when a click is made on the map.
     * 
     * @param point Position (on the map) of the mouse click.
     */
    public void mouseClicked(Point point);

}
