package org.insa.graphics.drawing.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.GraphStatistics.BoundingBox;
import org.insa.graph.Node;
import org.insa.graph.Path;
import org.insa.graph.Point;
import org.insa.graphics.drawing.BasicGraphPalette;
import org.insa.graphics.drawing.Drawing;
import org.insa.graphics.drawing.DrawingClickListener;
import org.insa.graphics.drawing.GraphPalette;
import org.insa.graphics.drawing.MercatorProjection;
import org.insa.graphics.drawing.PlateCarreProjection;
import org.insa.graphics.drawing.Projection;
import org.insa.graphics.drawing.overlays.MarkerOverlay;
import org.insa.graphics.drawing.overlays.MarkerUtils;
import org.insa.graphics.drawing.overlays.Overlay;
import org.insa.graphics.drawing.overlays.PathOverlay;
import org.insa.graphics.drawing.overlays.PointSetOverlay;

/**
 * Cette implementation de la classe Dessin produit vraiment un affichage (au
 * contraire de la classe DessinInvisible).
 */

public class BasicDrawing extends JPanel implements Drawing {

    /**
     * 
     */
    private static final long serialVersionUID = 96779785877771827L;

    private abstract class BasicOverlay implements Overlay {

        // Visible?
        protected boolean visible;

        // Color
        protected Color color;

        public BasicOverlay(Color color) {
            this.visible = true;
            this.color = color;
        }

        /**
         * @return The Z level of this overlay (>= 1).
         */
        public abstract int getZLevel();

        @Override
        public void setColor(Color color) {
            this.color = color;
        }

        @Override
        public Color getColor() {
            return this.color;
        }

        @Override
        public void setVisible(boolean visible) {
            this.visible = visible;
            BasicDrawing.this.repaint();
        }

        @Override
        public boolean isVisible() {
            return this.visible;
        }

        @Override
        public void delete() {
            BasicDrawing.this.overlays.remove(this);
        }

        /**
         * Draw the given overlay.
         */
        public void draw(Graphics2D g) {
            if (this.visible) {
                drawImpl(g);
            }
        }

        public abstract void drawImpl(Graphics2D g);

        public void redraw() {
            BasicDrawing.this.repaint();
        }

    };

    private class BasicMarkerOverlay extends BasicOverlay implements MarkerOverlay {

        // Marker width and height
        public static final int MARKER_WIDTH = 30, MARKER_HEIGHT = 60;

        // Point of the marker.
        private Point point;

        // Image to draw
        private Image image;

        // Inner color and fill mode.
        private Color innerColor;
        private final AlphaMode alphaMode;

        public BasicMarkerOverlay(Point point, Color color, Color inner, AlphaMode alphaMode) {
            super(color);
            this.point = point;
            this.image = MarkerUtils.getMarkerForColor(color, inner, alphaMode);
            this.innerColor = inner;
            this.alphaMode = alphaMode;
        }

        public int getZLevel() {
            return 3;
        }

        @Override
        public Point getPoint() {
            return point;
        }

        @Override
        public void setColor(Color color) {
            this.innerColor = this.innerColor.equals(this.color) ? color : innerColor;
            super.setColor(color);
            this.image = MarkerUtils.getMarkerForColor(color, this.innerColor, alphaMode);
        }

        @Override
        public void moveTo(Point point) {
            this.point = point;
            BasicDrawing.this.repaint();
        }

        @Override
        public void drawImpl(Graphics2D graphics) {

            int px = projection.longitudeToPixelX(getPoint().getLongitude());
            int py = projection.latitudeToPixelY(getPoint().getLatitude());

            graphics.drawImage(this.image, px - MARKER_WIDTH / 2, py - MARKER_HEIGHT, MARKER_WIDTH,
                    MARKER_HEIGHT, BasicDrawing.this);
        }

    };

    private class BasicPathOverlay extends BasicOverlay implements PathOverlay {

        // List of points
        private final List<Point> points;

        // Origin / Destination markers.
        private BasicMarkerOverlay origin, destination;

        public BasicPathOverlay(List<Point> points, Color color, BasicMarkerOverlay origin,
                BasicMarkerOverlay destination) {
            super(color);
            this.points = points;
            this.origin = origin;
            this.destination = destination;
            this.color = color;
        }

        public int getZLevel() {
            return 2;
        }

        @Override
        public void setColor(Color color) {
            super.setColor(color);
            this.origin.setColor(color);
            this.destination.setColor(color);
        }

        @Override
        public void drawImpl(Graphics2D graphics) {

            if (!points.isEmpty()) {

                graphics.setStroke(new BasicStroke(2));
                graphics.setColor(getColor());

                Iterator<Point> itPoint = points.iterator();
                Point prev = itPoint.next();

                while (itPoint.hasNext()) {
                    Point curr = itPoint.next();

                    int x1 = projection.longitudeToPixelX(prev.getLongitude());
                    int x2 = projection.longitudeToPixelX(curr.getLongitude());
                    int y1 = projection.latitudeToPixelY(prev.getLatitude());
                    int y2 = projection.latitudeToPixelY(curr.getLatitude());

                    graphics.drawLine(x1, y1, x2, y2);

                    prev = curr;
                }

            }

            if (this.origin != null) {
                this.origin.draw(graphics);
            }
            if (this.destination != null) {
                this.destination.draw(graphics);
            }
        }

    };

    private class BasicPointSetOverlay extends BasicOverlay implements PointSetOverlay {

        // Default point width
        private static final int DEFAULT_POINT_WIDTH = 5;

        // Image for path / points
        private final BufferedImage image;
        private final Graphics2D graphics;

        private int width = DEFAULT_POINT_WIDTH;

        public BasicPointSetOverlay() {
            super(Color.BLACK);
            this.image = new BufferedImage(BasicDrawing.this.width, BasicDrawing.this.height,
                    BufferedImage.TYPE_4BYTE_ABGR);
            this.graphics = image.createGraphics();
            this.graphics.setBackground(new Color(0, 0, 0, 0));
        }

        public int getZLevel() {
            return 1;
        }

        @Override
        public void setColor(Color color) {
            super.setColor(color);
            this.graphics.setColor(color);
        }

        @Override
        public void setWidth(int width) {
            this.width = Math.max(2, width);
        }

        @Override
        public void setWidthAndColor(int width, Color color) {
            setWidth(width);
            setColor(color);
        }

        @Override
        public void addPoint(Point point) {
            int x = projection.longitudeToPixelX(point.getLongitude()) - this.width / 2;
            int y = projection.latitudeToPixelY(point.getLatitude()) - this.width / 2;
            this.graphics.fillOval(x, y, this.width, this.width);
            BasicDrawing.this.repaint();
        }

        @Override
        public void addPoint(Point point, int width) {
            setWidth(width);
            addPoint(point);
        }

        @Override
        public void addPoint(Point point, Color color) {
            setColor(color);
            addPoint(point);
        }

        @Override
        public void addPoint(Point point, int width, Color color) {
            setWidth(width);
            setColor(color);
            addPoint(point);
        }

        @Override
        public void drawImpl(Graphics2D g) {
            g.drawImage(this.image, 0, 0, BasicDrawing.this);
        }

    }

    /**
     * Class encapsulating a set of overlays.
     *
     */
    private class BasicOverlays {

        // List of overlays.
        private ArrayList<ArrayList<BasicOverlay>> overlays = new ArrayList<>();

        public synchronized void draw(Graphics2D g) {
            // Clear overlays.
            for (ArrayList<BasicOverlay> arr: this.overlays) {
                for (BasicOverlay overlay: arr) {
                    overlay.draw(g);
                }
            }
        }

        public synchronized void remove(BasicOverlay overlay) {
            overlays.get(overlay.getZLevel() - 1).remove(overlay);
            BasicDrawing.this.repaint();
        }

        public void clear() {
            clear(true);
        }

        public void clear(boolean repaint) {
            // Clear overlays.
            for (ArrayList<BasicOverlay> arr: this.overlays) {
                arr.clear();
            }
            // Repaint if requested.
            if (repaint) {
                BasicDrawing.this.repaint();
            }
        }

        public BasicOverlay add(BasicOverlay marker) {
            return add(marker, true);
        }

        public synchronized BasicOverlay add(BasicOverlay overlay, boolean repaint) {

            // Check if we have a level for this...
            for (int i = overlays.size(); i < overlay.getZLevel(); ++i) {
                overlays.add(new ArrayList<>());
            }

            // Add overlay to the given list.
            overlays.get(overlay.getZLevel() - 1).add(overlay);

            // Repaint if requested.
            if (repaint) {
                BasicDrawing.this.repaint();
            }

            return overlay;
        }

    };

    // Default path color.
    public static final Color DEFAULT_PATH_COLOR = new Color(66, 134, 244);

    // Default palette.
    public static final GraphPalette DEFAULT_PALETTE = new BasicGraphPalette();

    // Maximum width for the drawing (in pixels).
    private static final int MAXIMUM_DRAWING_WIDTH = 2000;

    private Projection projection;

    // Width and height of the image
    private int width, height;

    // Zoom controls
    private MapZoomControls zoomControls;
    private ZoomAndPanListener zoomAndPanListener;

    //
    private Image graphImage = null;
    private Graphics2D graphGraphics = null;

    // List of image for markers
    private BasicOverlays overlays = new BasicOverlays();

    // Mapping DrawingClickListener -> MouseEventListener
    private List<DrawingClickListener> drawingClickListeners = new ArrayList<>();

    /**
     * Create a new BasicDrawing.
     * 
     */
    public BasicDrawing() {
        setLayout(null);
        this.setBackground(new Color(245, 245, 245));
        this.zoomAndPanListener = new ZoomAndPanListener(this,
                ZoomAndPanListener.DEFAULT_MIN_ZOOM_LEVEL, 20, 1.2);

        // Try...
        try {
            this.zoomControls = new MapZoomControls(this, 0,
                    ZoomAndPanListener.DEFAULT_MIN_ZOOM_LEVEL, 20);
            this.zoomControls.addZoomInListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    zoomAndPanListener.zoomIn();
                }
            });
            this.zoomControls.addZoomOutListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    zoomAndPanListener.zoomOut();
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (zoomControls.contains(evt.getPoint())) {
                    return;
                }
                Point lonlat = null;
                try {
                    lonlat = getLongitudeLatitude(evt);
                }
                catch (NoninvertibleTransformException e) {
                    return;
                }
                for (DrawingClickListener listener: drawingClickListeners) {
                    listener.mouseClicked(lonlat);
                }
            }

        });
    }

    @Override
    public void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;
        AffineTransform sTransform = g.getTransform();
        g.setColor(this.getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setTransform(zoomAndPanListener.getCoordTransform());

        if (graphImage != null) {
            // Draw graph
            g.drawImage(graphImage, 0, 0, this);
        }

        // Draw markers
        this.overlays.draw(g);

        g.setTransform(sTransform);
        if (this.zoomControls != null) {
            this.zoomControls.setZoomLevel(this.zoomAndPanListener.getZoomLevel());
            this.zoomControls.draw(g, getWidth() - this.zoomControls.getWidth() - 20,
                    this.getHeight() - this.zoomControls.getHeight() - 10, this);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.insa.graphics.drawing.Drawing#clear()
     */
    @Override
    public void clear() {
        if (this.graphGraphics != null) {
            this.graphGraphics.clearRect(0, 0, this.width, this.height);
        }
        this.overlays.clear(false);
        this.repaint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.insa.graphics.drawing.Drawing#clearOverlays()
     */
    @Override
    public void clearOverlays() {
        this.overlays.clear();
    }

    /**
     * @return The current ZoomAndPanListener associated with this drawing.
     */
    public ZoomAndPanListener getZoomAndPanListener() {
        return this.zoomAndPanListener;
    }

    /**
     * Return the longitude and latitude corresponding to the given position of the
     * MouseEvent.
     * 
     * @param event MouseEvent from which longitude/latitude should be retrieved.
     * 
     * @return Point representing the projection of the MouseEvent position in the
     *         graph/map.
     * 
     * @throws NoninvertibleTransformException if the actual transformation is
     *         invalid.
     */
    protected Point getLongitudeLatitude(MouseEvent event) throws NoninvertibleTransformException {
        // Get the point using the inverse transform of the Zoom/Pan object, this gives
        // us
        // a point within the drawing box (between [0, 0] and [width, height]).
        Point2D ptDst = this.zoomAndPanListener.getCoordTransform()
                .inverseTransform(event.getPoint(), null);

        // Inverse the "projection" on x/y to get longitude and latitude.
        return new Point(projection.pixelXToLongitude(ptDst.getX()),
                projection.pixelYToLatitude(ptDst.getY()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.insa.graphics.drawing.Drawing#addDrawingClickListener(org.insa.graphics.
     * drawing.DrawingClickListener)
     */
    @Override
    public void addDrawingClickListener(DrawingClickListener listener) {
        this.drawingClickListeners.add(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.insa.graphics.drawing.Drawing#removeDrawingClickListener(org.insa.
     * graphics.drawing.DrawingClickListener)
     */
    @Override
    public void removeDrawingClickListener(DrawingClickListener listener) {
        this.drawingClickListeners.remove(listener);
    }

    public BasicMarkerOverlay createMarker(Point point, Color outer, Color inner, AlphaMode mode) {
        return new BasicMarkerOverlay(point, outer, inner, mode);
    }

    @Override
    public MarkerOverlay drawMarker(Point point, Color outer, Color inner, AlphaMode mode) {
        return (MarkerOverlay) this.overlays.add(createMarker(point, outer, inner, mode));
    }

    @Override
    public PointSetOverlay createPointSetOverlay() {
        return (PointSetOverlay) this.overlays.add(new BasicPointSetOverlay(), false);
    }

    @Override
    public PointSetOverlay createPointSetOverlay(int width, Color color) {
        PointSetOverlay ps = createPointSetOverlay();
        ps.setWidthAndColor(width, color);
        return ps;
    }

    /**
     * Draw the given arc.
     * 
     * @param arc Arc to draw.
     * @param palette Palette to use to retrieve color and width for arc, or null to
     *        use current settings.
     */
    protected void drawArc(Arc arc, GraphPalette palette, boolean repaint) {
        List<Point> pts = arc.getPoints();
        if (!pts.isEmpty()) {
            if (palette != null) {
                this.graphGraphics.setColor(palette.getColorForArc(arc));
                this.graphGraphics.setStroke(new BasicStroke(palette.getWidthForArc(arc)));
            }
            Iterator<Point> it1 = pts.iterator();
            Point prev = it1.next();
            while (it1.hasNext()) {
                Point curr = it1.next();

                int x1 = projection.longitudeToPixelX(prev.getLongitude());
                int x2 = projection.longitudeToPixelX(curr.getLongitude());
                int y1 = projection.latitudeToPixelY(prev.getLatitude());
                int y2 = projection.latitudeToPixelY(curr.getLatitude());

                graphGraphics.drawLine(x1, y1, x2, y2);
                prev = curr;
            }
        }
        if (repaint) {
            this.repaint();
        }
    }

    /**
     * Initialize the drawing for the given graph.
     * 
     * @param graph
     */
    protected void initialize(Graph graph) {

        // Clear everything.
        this.clear();

        BoundingBox box = graph.getGraphInformation().getBoundingBox();

        // Find minimum/maximum longitude and latitude.
        float minLon = box.getTopLeftPoint().getLongitude(),
                maxLon = box.getBottomRightPoint().getLongitude(),
                minLat = box.getBottomRightPoint().getLatitude(),
                maxLat = box.getTopLeftPoint().getLatitude();

        // Add a little delta to avoid drawing on the edge...
        float diffLon = maxLon - minLon, diffLat = maxLat - minLat;
        float deltaLon = 0.01f * diffLon, deltaLat = 0.01f * diffLat;

        // Create the projection and retrieve width and height for the box.
        BoundingBox extendedBox = box.extend(deltaLon, deltaLat, deltaLon, deltaLat);

        // Special projection for non-realistic maps...
        if (graph.getMapId().startsWith("0x")) {
            projection = new PlateCarreProjection(extendedBox, MAXIMUM_DRAWING_WIDTH / 4);
        }
        else {
            projection = new MercatorProjection(extendedBox, MAXIMUM_DRAWING_WIDTH);
        }
        this.width = (int) projection.getImageWidth();
        this.height = (int) projection.getImageHeight();

        // Create the image
        BufferedImage img = new BufferedImage(this.width, this.height,
                BufferedImage.TYPE_3BYTE_BGR);
        this.graphImage = img;
        this.graphGraphics = img.createGraphics();
        this.graphGraphics.setBackground(this.getBackground());
        this.graphGraphics.clearRect(0, 0, this.width, this.height);

        // Set the zoom and pan listener

        double scale = 1 / Math.max(this.width / (double) this.getWidth(),
                this.height / (double) this.getHeight());

        this.zoomAndPanListener.setCoordTransform(this.graphGraphics.getTransform());
        this.zoomAndPanListener.getCoordTransform().translate(
                (this.getWidth() - this.width * scale) / 2,
                (this.getHeight() - this.height * scale) / 2);
        this.zoomAndPanListener.getCoordTransform().scale(scale, scale);
        this.zoomAndPanListener.setZoomLevel(0);
        this.zoomControls.setZoomLevel(0);

        // Repaint
        this.repaint();
    }

    @Override
    public void drawGraph(Graph graph, GraphPalette palette) {
        int repaintModulo = Math.max(1, graph.size() / 100);

        // Initialize the buffered image

        this.initialize(graph);

        // Remove zoom and pan listener
        this.removeMouseListener(zoomAndPanListener);
        this.removeMouseMotionListener(zoomAndPanListener);
        this.removeMouseWheelListener(zoomAndPanListener);

        for (Node node: graph) {
            for (Arc arc: node) {
                // Draw arcs only if there are one-way arcs or if origin is lower than
                // destination, avoid drawing two-ways arc twice.
                if (arc.getRoadInformation().isOneWay()
                        || arc.getOrigin().compareTo(arc.getDestination()) < 0) {
                    drawArc(arc, palette, false);
                }
            }
            if (node.getId() % repaintModulo == 0) {
                this.repaint();
            }
        }
        this.repaint();

        // Re-add zoom and pan listener
        this.addMouseListener(zoomAndPanListener);
        this.addMouseMotionListener(zoomAndPanListener);
        this.addMouseWheelListener(zoomAndPanListener);
    }

    @Override
    public void drawGraph(Graph graph) {
        drawGraph(graph, DEFAULT_PALETTE);
    }

    @Override
    public PathOverlay drawPath(Path path, Color color, boolean markers) {
        List<Point> points = new ArrayList<Point>();
        if (!path.isEmpty()) {
            points.add(path.getOrigin().getPoint());
            for (Arc arc: path.getArcs()) {
                Iterator<Point> itPoint = arc.getPoints().iterator();
                // Discard origin each time
                itPoint.next();
                while (itPoint.hasNext()) {
                    points.add(itPoint.next());
                }
            }
        }
        BasicMarkerOverlay origin = null, destination = null;
        if (markers && !path.isEmpty()) {
            origin = createMarker(path.getOrigin().getPoint(), color, color, AlphaMode.TRANSPARENT);
            destination = createMarker(path.getDestination().getPoint(), color, color,
                    AlphaMode.TRANSPARENT);
        }
        return (PathOverlay) this.overlays
                .add(new BasicPathOverlay(points, color, origin, destination));
    }

    @Override
    public PathOverlay drawPath(Path path, Color color) {
        return drawPath(path, color, true);
    }

    @Override
    public PathOverlay drawPath(Path path) {
        return drawPath(path, DEFAULT_PATH_COLOR);
    }

    @Override
    public PathOverlay drawPath(Path path, boolean markers) {
        return drawPath(path, DEFAULT_PATH_COLOR, markers);
    }

}
