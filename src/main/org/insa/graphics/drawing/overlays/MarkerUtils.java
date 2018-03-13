package org.insa.graphics.drawing.overlays;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;

import org.insa.graphics.drawing.Drawing.AlphaMode;

public class MarkerUtils {

    /**
     * Create an image to represent a marker using the given color for the outer and
     * inner part, and the given mode for the inner part.
     * 
     * @param outer Outer color of the marker.
     * @param inner Inner color of the marker.
     * @param mode Mode to use to fill the inner part of the marker.
     * 
     * @return An image representing a marker.
     * 
     * @see MarkerUtils#getMarkerForColor(Color, AlphaMode)
     */
    public static Image getMarkerForColor(Color outer, Color inner, AlphaMode mode) {
        // create image
        int[][] mask = readMarkerMask();
        BufferedImage image = new BufferedImage(mask[0].length, mask.length,
                BufferedImage.TYPE_4BYTE_ABGR);

        // Color[] map = getColorMapping(color);
        int outerRGB = outer.getRGB() & 0x00ffffff;
        int innerRGB = inner.getRGB() & 0x00ffffff;
        for (int i = 0; i < image.getHeight(); ++i) {
            for (int j = 0; j < image.getWidth(); ++j) {
                if (i >= MIN_Y_CENTER && i < MAX_Y_CENTER && j >= MIN_X_CENTER
                        && j < MAX_X_CENTER) {
                    image.setRGB(j, i, innerRGB
                            | ((mode == AlphaMode.OPAQUE ? MAXIMUM_MAX_VALUE : mask[i][j]) << 24));
                }
                else {
                    image.setRGB(j, i, outerRGB | (mask[i][j] << 24));
                }
            }
        }

        return image;
    }

    // Mask cache
    private static int[][] MASK_CACHE = null;

    // Hand-made... These defines the "center" of the marker, that can be filled
    // with a different color.
    private static final int MIN_X_CENTER = 40, MAX_X_CENTER = 101, MIN_Y_CENTER = 40,
            MAX_Y_CENTER = 100;
    private static final int MAXIMUM_MAX_VALUE = 249;

    /**
     * @return Retrieve the mask from the mask file or from the cache.
     */
    private static int[][] readMarkerMask() {
        if (MASK_CACHE == null) {
            try {
                DataInputStream dis = new DataInputStream(
                        MarkerUtils.class.getResourceAsStream("/marker_mask.bin"));

                int nrows = dis.readInt();
                int ncols = dis.readInt();

                MASK_CACHE = new int[nrows][ncols];
                for (int i = 0; i < nrows; ++i) {
                    for (int j = 0; j < ncols; ++j) {
                        MASK_CACHE[i][j] = dis.readUnsignedByte();
                    }
                }
                dis.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                MASK_CACHE = null;
            }
        }
        return MASK_CACHE;
    }

}
