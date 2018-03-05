package org.insa.graphics.drawing.overlays;

import java.awt.Color;

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;

public class PaintUtils {

    // Graphic factory.
    private static final GraphicFactory GRAPHIC_FACTORY = AwtGraphicFactory.INSTANCE;

    /**
     * Convert the given AWT color to a mapsforge compatible color.
     * 
     * @param color
     * @return
     */
    public static int convertColor(Color color) {
        return GRAPHIC_FACTORY.createColor(color.getAlpha(), color.getRed(), color.getGreen(),
                color.getBlue());
    }

    /**
     * Convert the given mapsforge color to an AWT Color.
     * 
     * @param color
     * @return
     */
    public static Color convertColor(int color) {
        return new Color(color, true);
    }

    /**
     * @param width
     * @return
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
