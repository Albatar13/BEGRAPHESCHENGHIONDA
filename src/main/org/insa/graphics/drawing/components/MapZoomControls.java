package org.insa.graphics.drawing.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class MapZoomControls {

    // Default ID for action events
    private static final int ZOOM_IN_ACTION_ID = 0x1;
    private static final String ZOOM_IN_ACTION_NAME = "ZoomIn";

    private static final int ZOOM_OUT_ACTION_ID = 0x2;
    private static final String ZOOM_OUT_ACTION_NAME = "ZoomOut";

    // Height
    private static final int DEFAULT_HEIGHT = 20;

    // Default spacing between mark
    private static final int DEFAULT_SPACING = 4;

    // Zoom ticks ratio from height (not the current one)
    private static final double ZOOM_TICK_HEIGHT_RATIO = 0.3;

    // Zoom ticks color
    private static final Color ZOOM_TICK_COLOR = Color.GRAY;

    // Current zoom ticks ratio from height
    private static final double CURRENT_ZOOM_TICK_HEIGHT_RATIO = 0.8;

    // Zoom ticks color
    private static final Color CURRENT_ZOOM_TICK_COLOR = new Color(25, 25, 25);

    // Use half mark or not
    private boolean halfMark = true;

    private int currentLevel = 0;
    private final int minLevel, maxLevel;

    // Zoom in/out image and their rectangles.
    private final Image zoomIn, zoomOut;
    private final Rectangle zoomInRect = new Rectangle(0, 0, 0, 0),
            zoomOutRect = new Rectangle(0, 0, 0, 0);

    // List of listeners
    private final List<ActionListener> zoomInListeners = new ArrayList<>();
    private final List<ActionListener> zoomOutListeners = new ArrayList<>();

    public MapZoomControls(Component component, final int defaultZoom, final int minZoom,
            final int maxZoom) throws IOException {

        zoomIn = ImageIO.read(getClass().getResourceAsStream("/zoomIn.png"))
                .getScaledInstance(DEFAULT_HEIGHT, DEFAULT_HEIGHT, Image.SCALE_SMOOTH);
        zoomOut = ImageIO.read(getClass().getResourceAsStream("/zoomOut.png"))
                .getScaledInstance(DEFAULT_HEIGHT, DEFAULT_HEIGHT, Image.SCALE_SMOOTH);

        this.currentLevel = defaultZoom;
        this.minLevel = minZoom;
        this.maxLevel = maxZoom;

        component.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (zoomInRect.contains(e.getPoint()) || zoomOutRect.contains(e.getPoint())) {
                    component.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                else {
                    component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (zoomInRect.contains(e.getPoint()) && currentLevel < maxLevel) {
                    currentLevel += 1;
                    for (ActionListener al: zoomInListeners) {
                        al.actionPerformed(
                                new ActionEvent(this, ZOOM_IN_ACTION_ID, ZOOM_IN_ACTION_NAME));
                    }
                }
                else if (zoomOutRect.contains(e.getPoint()) && currentLevel > minLevel) {
                    currentLevel -= 1;
                    for (ActionListener al: zoomOutListeners) {
                        al.actionPerformed(
                                new ActionEvent(this, ZOOM_OUT_ACTION_ID, ZOOM_OUT_ACTION_NAME));
                    }
                }
                component.repaint();
            }
        });
    }

    /**
     * Add a zoom-in listener.
     * 
     * @param listener Zoom-in listener to add to this MapZoomControls instance.
     */
    public void addZoomInListener(ActionListener listener) {
        this.zoomInListeners.add(listener);
    }

    /**
     * Add a zoom-out listener.
     * 
     * @param listener Zoom-out listener to add to this MapZoomControls instance.
     */
    public void addZoomOutListener(ActionListener listener) {
        this.zoomOutListeners.add(listener);
    }

    /**
     * @return the current zoom level.
     */
    public int getZoomLevel() {
        return this.currentLevel;
    }

    /**
     * Set the current zoom level without requesting a redraw.
     * 
     * @param level Zoom level to set.
     */
    public void setZoomLevel(int level) {
        this.currentLevel = level;
    }

    /**
     * @return Height of this "component" when drawn.
     */
    public int getHeight() {
        return DEFAULT_HEIGHT;
    }

    /**
     * @return Width of this "component" when drawn.
     */
    public int getWidth() {
        return DEFAULT_HEIGHT + 2 + (this.maxLevel - this.minLevel) * DEFAULT_SPACING + 1 + 2
                + DEFAULT_HEIGHT;
    }

    /**
     * Check if a point is contained inside an element of this zoom controls, useful
     * to avoid spurious click listeners.
     * 
     * @param point Point to check.
     * 
     * @return true if the given point correspond to an element of this zoom
     *         controls.
     */
    public boolean contains(Point point) {
        return zoomInRect.contains(point) || zoomOutRect.contains(point);
    }

    protected void draw(Graphics2D g, int xoffset, int yoffset, ImageObserver observer) {

        int height = getHeight();

        // Draw icon
        g.drawImage(zoomOut, xoffset, yoffset, observer);
        zoomOutRect.setBounds(xoffset, yoffset, DEFAULT_HEIGHT, DEFAULT_HEIGHT);

        g.setStroke(new BasicStroke(1));

        // Draw ticks
        xoffset += DEFAULT_HEIGHT + 2;
        g.setColor(ZOOM_TICK_COLOR);
        g.drawLine(xoffset, yoffset + height / 2,
                xoffset + (this.maxLevel - this.minLevel) * DEFAULT_SPACING + 1,
                yoffset + height / 2);
        for (int i = 0; i <= (this.maxLevel - this.minLevel); i += halfMark ? 2 : 1) {
            g.drawLine(xoffset + i * DEFAULT_SPACING,
                    yoffset + (int) (height * (1 - ZOOM_TICK_HEIGHT_RATIO) / 2),
                    xoffset + i * DEFAULT_SPACING,
                    yoffset + (int) (height * (1 + ZOOM_TICK_HEIGHT_RATIO) / 2));
        }

        // Draw current ticks
        g.setColor(CURRENT_ZOOM_TICK_COLOR);
        g.drawLine(xoffset + (currentLevel - this.minLevel) * DEFAULT_SPACING,
                yoffset + (int) (height * (1 - CURRENT_ZOOM_TICK_HEIGHT_RATIO) / 2),
                xoffset + (currentLevel - this.minLevel) * DEFAULT_SPACING,
                yoffset + (int) (height * (1 + CURRENT_ZOOM_TICK_HEIGHT_RATIO) / 2));

        xoffset += (this.maxLevel - this.minLevel) * DEFAULT_SPACING + 1 + 2;

        g.drawImage(zoomIn, xoffset, yoffset, observer);
        zoomInRect.setBounds(xoffset, yoffset, DEFAULT_HEIGHT, DEFAULT_HEIGHT);

    }

}
