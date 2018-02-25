package org.insa.graphics.drawing.overlays;

public interface Overlay {

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

}
