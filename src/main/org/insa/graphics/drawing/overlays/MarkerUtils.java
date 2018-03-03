package org.insa.graphics.drawing.overlays;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.FileInputStream;

public class MarkerUtils {

    private static final String MARKER_MASK_FILE = "res/marker_mask.bin";

    /**
     * Create a Bitmap representing a marker of the given color.
     * 
     * @param color
     * @return
     */
    public static BufferedImage getMarkerForColor(Color color) {
        // create image
        int[][] mask = readMarkerMask();
        BufferedImage image = new BufferedImage(mask[0].length, mask.length, BufferedImage.TYPE_4BYTE_ABGR);

        // Color[] map = getColorMapping(color);
        int rgb = color.getRGB() & 0x00ffffff;
        for (int i = 0; i < image.getHeight(); ++i) {
            for (int j = 0; j < image.getWidth(); ++j) {
                // image.setRGB(j, i, map[MARKER_MASK[i][j]].getRGB());
                image.setRGB(j, i, rgb | (mask[i][j] << 24));
            }
        }

        BufferedImage scaleImage = new BufferedImage(28, 48, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = scaleImage.createGraphics();
        graphics.drawImage(image.getScaledInstance(28, 48, Image.SCALE_SMOOTH), 0, 0, null);
        graphics.dispose();

        // Create Bitmap and return it.
        return scaleImage;
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
