package org.insa.graphics.drawing.overlays;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.FileInputStream;

public class MarkerUtils {

    private static final String MARKER_MASK_FILE = "res/marker_mask.bin";

    /**
     * Create an Image representing a marker of the given color.
     * 
     * @param color Color of the marker.
     * 
     * @return A new Image representing a marker with the given color.
     */
    public static Image getMarkerForColor(Color color) {
        // create image
        int[][] mask = readMarkerMask();
        BufferedImage image = new BufferedImage(mask[0].length, mask.length,
                BufferedImage.TYPE_4BYTE_ABGR);

        // Color[] map = getColorMapping(color);
        int rgb = color.getRGB() & 0x00ffffff;
        for (int i = 0; i < image.getHeight(); ++i) {
            for (int j = 0; j < image.getWidth(); ++j) {
                // image.setRGB(j, i, map[MARKER_MASK[i][j]].getRGB());
                image.setRGB(j, i, rgb | (mask[i][j] << 24));
            }
        }

        return image;
    }

    // Mask cache
    private static int[][] MASK_CACHE = null;

    /**
     * @return Retrieve the mask from the mask file or from the cache.
     */
    private static int[][] readMarkerMask() {
        if (MASK_CACHE == null) {
            try {
                DataInputStream dis = new DataInputStream(new FileInputStream(MARKER_MASK_FILE));

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
