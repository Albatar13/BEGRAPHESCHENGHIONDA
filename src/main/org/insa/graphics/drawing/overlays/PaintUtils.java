package org.insa.graphics.drawing.overlays;

import java.awt.Color;

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;

public class PaintUtils {

    // Graphic factory.
    private static final GraphicFactory GRAPHIC_FACTORY = AwtGraphicFactory.INSTANCE;

    /**
     * Convert the given AWT color to a mapsforge compatible color.
     * 
     * @param color AWT color to convert.
     * 
     * @return Integer value representing a corresponding mapsforge color.
     */
    public static int convertColor(Color color) {
        return GRAPHIC_FACTORY.createColor(color.getAlpha(), color.getRed(), color.getGreen(),
                color.getBlue());
    }

    /**
     * Convert the given mapsforge color to an AWT Color.
     * 
     * @param color Integer value representing a mapsforge color.
     * 
     * @return AWT color corresponding to the given value.
     */
    public static Color convertColor(int color) {
        return new Color(color, true);
    }

    /**
     * Compute an updated value for the given width at the given zoom level. This
     * function can be used to automatically scale {@link Polyline} or
     * {@link Marker} when zooming (which is not done by default in Mapsforge).
     * 
     * @param width Original width to convert.
     * @param zoomLevel Zoom level for which the width should be computed.
     * 
     * @return Actual width at the given zoom level.
     */
    public static float getStrokeWidth(int width, byte zoomLevel) {
        float mul = 1;
        if (zoomLevel < 6) {
            mul = 1;
        }
        else if (zoomLevel < 10) {
            mul += (zoomLevel - 5) * 0.5;
        }
        else if (zoomLevel < 13) {
            mul = 3.5f;
        }
        else {
            mul += 2 * (zoomLevel - 8) / 3;
        }
        return width * mul;
    }

}
