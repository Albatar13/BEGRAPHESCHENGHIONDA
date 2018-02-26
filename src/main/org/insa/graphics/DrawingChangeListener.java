package org.insa.graphics;

import org.insa.graphics.drawing.Drawing;

public interface DrawingChangeListener {

    /**
     * Event fired when a new drawing is loaded.
     * 
     * @param oldDrawing Old drawing, may be null if no drawing exits prior to this
     *        one.
     * @param newDrawing New drawing.
     */
    public void onDrawingLoaded(Drawing oldDrawing, Drawing newDrawing);

    /**
     * Event fired when a redraw request is emitted - This is typically emitted
     * after a onDrawingLoaded event, but not always, and request that elements are
     * drawn again on the new drawing.
     * 
     */
    public void onRedrawRequest();

}
