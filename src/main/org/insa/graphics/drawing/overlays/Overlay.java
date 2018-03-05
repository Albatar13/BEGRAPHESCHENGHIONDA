package org.insa.graphics.drawing.overlays;

import java.awt.Color;

public interface Overlay {

    /**
     * Set the color of this overlay.
     * 
     * @param color New color for the overlay.
     */
    public void setColor(Color color);

    /**
     * @return The current color of this overlay.
     */
    public Color getColor();

    /**
     * Show or hide this marker - A marker should be visible when created.
     * 
     * @param visible true to show the marker, false to hide.
     */
    public void setVisible(boolean visible);

    /**
     * @return true if this overlay is visible.
     */
    public boolean isVisible();

    /**
     * Delete this marker.
     */
    public void delete();

    /**
     * Request a redraw of this overlay - This can start a full redraw of the inner
     * drawing.
     */
    public void redraw();

}
