package org.insa.graphics.drawing.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;
import org.insa.graph.Point;
import org.insa.graphics.drawing.BasicGraphPalette;
import org.insa.graphics.drawing.Drawing;
import org.insa.graphics.drawing.DrawingClickListener;
import org.insa.graphics.drawing.GraphPalette;
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

        public BasicOverlay() {
            this.visible = true;
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
            synchronized (overlays) {
                BasicDrawing.this.overlays.remove(this);
            }
            BasicDrawing.this.repaint();
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

    };

    private class BasicMarkerOverlay extends BasicOverlay implements MarkerOverlay {

        // Default marker width
        private static final int DEFAULT_MARKER_WIDTH = 20;

        // Point of the marker.
        private Point point;

        // Color of the marker.
        private final Color color;

        public BasicMarkerOverlay(Point point, Color color) {
            super();
            this.point = point;
            this.color = color;
        }

        @Override
        public Point getPoint() {
            return point;
        }

        @Override
        public Color getColor() {
            return color;
        }

        @Override
        public void moveTo(Point point) {
            this.point = point;
            BasicDrawing.this.repaint();
        }

        @Override
        public void drawImpl(Graphics2D graphics) {

            int px = BasicDrawing.this.projx(getPoint().getLongitude());
            int py = BasicDrawing.this.projy(getPoint().getLatitude());

            BufferedImage img = MarkerUtils.getMarkerForColor(color);
            Graphics2D gr = img.createGraphics();
            double scale = DEFAULT_MARKER_WIDTH / (double) img.getHeight();
            gr.scale(scale, scale);

            graphics.drawImage(img, px - img.getWidth() / 2, py - img.getHeight(), BasicDrawing.this);
        }

    };

    private class BasicPathOverlay extends BasicOverlay implements PathOverlay {

        // List of points
        private final List<Point> points;

        // Color for the path
        private Color color;

        // Origin / Destination markers.
        private BasicMarkerOverlay origin, destination;

        public BasicPathOverlay(List<Point> points, Color color, BasicMarkerOverlay origin,
                BasicMarkerOverlay destination) {
            this.points = points;
            this.origin = origin;
            this.destination = destination;
        }

        @Override
        public void drawImpl(Graphics2D graphics) {

            if (!points.isEmpty()) {

                graphics.setStroke(new BasicStroke(2));
                graphics.setColor(color);

                Iterator<Point> itPoint = points.iterator();
                Point prev = itPoint.next();

                while (itPoint.hasNext()) {
                    Point curr = itPoint.next();

                    int x1 = BasicDrawing.this.projx(prev.getLongitude());
                    int x2 = BasicDrawing.this.projx(curr.getLongitude());
                    int y1 = BasicDrawing.this.projy(prev.getLatitude());
                    int y2 = BasicDrawing.this.projy(curr.getLatitude());

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
            this.image = new BufferedImage(BasicDrawing.this.width, BasicDrawing.this.height,
                    BufferedImage.TYPE_4BYTE_ABGR);
            this.graphics = image.createGraphics();
            this.graphics.setBackground(new Color(0, 0, 0, 0));
        }

        @Override
        public void setColor(Color color) {
            this.graphics.setColor(color);
        }

        @Override
        public void setWidth(int width) {
            this.width = width;
        }

        @Override
        public void setWidthAndColor(int width, Color color) {
            setWidth(width);
            setColor(color);
        }

        @Override
        public void addPoint(Point point) {
            this.width = 5;
            int x = BasicDrawing.this.projx(point.getLongitude()) - this.width / 2;
            int y = BasicDrawing.this.projy(point.getLatitude()) - this.width / 2;
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

    // Default path color.
    public static final Color DEFAULT_PATH_COLOR = new Color(255, 0, 255);

    // Default palette.
    public static final GraphPalette DEFAULT_PALETTE = new BasicGraphPalette();

    // Maximum width for the drawing (in pixels).
    private static final int MAXIMUM_DRAWING_WIDTH = 2000;

    private double long1, long2, lat1, lat2;

    // Width and height of the image
    private int width, height;

    private ZoomAndPanListener zoomAndPanListener;

    //
    private Image graphImage = null;
    private Graphics2D graphGraphics = null;

    // List of image for markers
    private List<BasicOverlay> overlays = Collections.synchronizedList(new ArrayList<BasicOverlay>());

    // Mapping DrawingClickListener -> MouseEventListener
    private Map<DrawingClickListener, MouseListener> listenerMapping = new IdentityHashMap<>();

    /**
     * Create a new BasicDrawing.
     * 
     */
    public BasicDrawing() {
        this.zoomAndPanListener = new ZoomAndPanListener(this, ZoomAndPanListener.DEFAULT_MIN_ZOOM_LEVEL, 20, 1.2);
    }

    @Override
    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setTransform(zoomAndPanListener.getCoordTransform());

        if (graphImage != null) {
            // Draw graph
            g.drawImage(graphImage, 0, 0, this);
        }

        // Draw markers
        synchronized (overlays) {
            for (BasicOverlay overlay: overlays) {
                overlay.draw(g);
            }
        }
    }

    /**
     * @param lon
     * @return
     */
    private int projx(double lon) {
        return (int) (width * (lon - this.long1) / (this.long2 - this.long1));
    }

    /**
     * @param lat
     * @return
     */
    private int projy(double lat) {
        return (int) (height * (1 - (lat - this.lat1) / (this.lat2 - this.lat1)));
    }

    /**
     * Return the longitude and latitude corresponding to the given position of the
     * MouseEvent.
     * 
     * @param event
     * 
     * @return
     */
    public Point getLongitudeLatitude(MouseEvent event) throws NoninvertibleTransformException {
        // Get the point using the inverse transform of the Zoom/Pan object, this gives
        // us
        // a point within the drawing box (between [0, 0] and [width, height]).
        Point2D ptDst = this.zoomAndPanListener.getCoordTransform().inverseTransform(event.getPoint(), null);

        // Inverse the "projection" on x/y to get longitude and latitude.
        double lon = ptDst.getX();
        double lat = ptDst.getY();
        lon = (lon / this.width) * (this.long2 - this.long1) + this.long1;
        lat = (1 - lat / this.height) * (this.lat2 - this.lat1) + this.lat1;

        // Return a new point.
        return new Point(lon, lat);
    }

    @Override
    public void addDrawingClickListener(DrawingClickListener listener) {
        MouseListener mListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                try {
                    listener.mouseClicked(getLongitudeLatitude(evt));
                }
                catch (NoninvertibleTransformException e) {
                    e.printStackTrace();
                }
            }
        };
        this.addMouseListener(mListener);
        this.listenerMapping.put(listener, mListener);
    }

    @Override
    public void removeDrawingClickListener(DrawingClickListener listener) {
        this.removeMouseListener(this.listenerMapping.get(listener));
    }

    @Override
    public void clear() {
        if (this.graphGraphics != null) {
            this.graphGraphics.clearRect(0, 0, this.width, this.height);
        }
        synchronized (overlays) {
            this.overlays.clear();
        }
    }

    public BasicMarkerOverlay createMarker(Point point, Color color) {
        return new BasicMarkerOverlay(point, color);
    }

    @Override
    public MarkerOverlay drawMarker(Point point, Color color) {
        BasicMarkerOverlay marker = createMarker(point, color);
        synchronized (overlays) {
            this.overlays.add(marker);
        }
        this.repaint();
        return marker;
    }

    @Override
    public PointSetOverlay createPointSetOverlay() {
        BasicPointSetOverlay ps = new BasicPointSetOverlay();
        synchronized (overlays) {
            this.overlays.add(ps);
        }
        return ps;
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

                int x1 = this.projx(prev.getLongitude());
                int x2 = this.projx(curr.getLongitude());
                int y1 = this.projy(prev.getLatitude());
                int y2 = this.projy(curr.getLatitude());

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

        // Find minimum/maximum longitude and latitude.
        double minLon = Double.POSITIVE_INFINITY, minLat = Double.POSITIVE_INFINITY, maxLon = Double.NEGATIVE_INFINITY,
                maxLat = Double.NEGATIVE_INFINITY;
        for (Node node: graph.getNodes()) {
            Point pt = node.getPoint();
            if (pt.getLatitude() < minLat) {
                minLat = pt.getLatitude();
            }
            if (pt.getLatitude() > maxLat) {
                maxLat = pt.getLatitude();
            }
            if (pt.getLongitude() < minLon) {
                minLon = pt.getLongitude();
            }
            if (pt.getLongitude() > maxLon) {
                maxLon = pt.getLongitude();
            }
        }

        // Add a little delta to avoid drawing on the edge...
        double diffLon = maxLon - minLon, diffLat = maxLat - minLat;
        double deltaLon = 0.01 * diffLon, deltaLat = 0.01 * diffLat;

        this.long1 = minLon - deltaLon;
        this.long2 = maxLon + deltaLon;
        this.lat1 = minLat - deltaLat;
        this.lat2 = maxLat + deltaLat;

        // Compute width/height for the image

        if (diffLat < diffLon) {
            this.width = MAXIMUM_DRAWING_WIDTH;
            this.height = (int) (this.width * diffLat / diffLon);
        }
        else {
            this.height = MAXIMUM_DRAWING_WIDTH;
            this.width = (int) (this.height * diffLon / diffLat);
        }

        // Create the image
        BufferedImage img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_3BYTE_BGR);
        this.graphImage = img;
        this.graphGraphics = img.createGraphics();
        this.graphGraphics.setBackground(Color.WHITE);
        this.graphGraphics.clearRect(0, 0, this.width, this.height);

        // Set the zoom and pan listener

        double scale = 1 / Math.max(this.width / (double) this.getWidth(), this.height / (double) this.getHeight());

        this.zoomAndPanListener.setCoordTransform(this.graphGraphics.getTransform());
        this.zoomAndPanListener.getCoordTransform().translate((this.getWidth() - this.width * scale) / 2,
                (this.getHeight() - this.height * scale) / 2);
        this.zoomAndPanListener.getCoordTransform().scale(scale, scale);
        this.zoomAndPanListener.setZoomLevel(0);

        // Repaint
        this.repaint();
    }

    @Override
    public void drawGraph(Graph graph, GraphPalette palette) {
        int repaintModulo = graph.getNodes().size() / 100;

        // Initialize the buffered image

        this.initialize(graph);

        // Remove zoom and pan listener
        this.removeMouseListener(zoomAndPanListener);
        this.removeMouseMotionListener(zoomAndPanListener);
        this.removeMouseWheelListener(zoomAndPanListener);

        for (Node node: graph.getNodes()) {
            for (Arc arc: node.getSuccessors()) {
                if (arc.getRoadInformation().isOneWay() || arc.getOrigin().compareTo(arc.getDestination()) < 0) {
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
            origin = createMarker(path.getOrigin().getPoint(), color);
            destination = createMarker(path.getDestination().getPoint(), color);
        }
        BasicPathOverlay overlay = new BasicPathOverlay(points, color, origin, destination);
        synchronized (overlays) {
            this.overlays.add(overlay);
        }
        this.repaint();
        return overlay;
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
