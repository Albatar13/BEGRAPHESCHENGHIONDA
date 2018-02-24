package org.insa.graphics.drawing.utils;

import java.awt.Color;

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;

public class PaintUtils {

    // Graphic factory.
    private static final GraphicFactory GRAPHIC_FACTORY = AwtGraphicFactory.INSTANCE;

    /**
     * @param color
     * @return
     */
    public static int convertColor(Color color) {
        return GRAPHIC_FACTORY.createColor(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * @param width
     * @return
     */
    public static int getStrokeWidth(int width, byte zoomLevel) {
        int mul = 2;
        if (zoomLevel < 8) {
            mul = 1;
        }
        else {
            mul += 2 * (zoomLevel - 8) / 3;
        }
        return width * mul;
    }
}
