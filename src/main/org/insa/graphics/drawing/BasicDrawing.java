package org.insa.graphics.drawing;

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
import org.insa.graphics.drawing.utils.MarkerUtils;

/**
 * Cette implementation de la classe Dessin produit vraiment un affichage (au
 * contraire de la classe DessinInvisible).
 */

public class BasicDrawing extends JPanel implements Drawing {

    /**
     * 
     */
    private static final long serialVersionUID = 96779785877771827L;

    public class BasicMarkerTracker implements MarkerTracker {

        // Point of the marker.
        private Point point;

        // Image of the marker
        protected BufferedImage image;

        public BasicMarkerTracker(Point point, BufferedImage image) {
            this.point = point;
            this.image = image;
        }

        @Override
        public Point getPoint() {
            return point;
        }

        @Override
        public void moveTo(Point point) {
            this.point = point;
            BasicDrawing.this.repaint();
        }

        @Override
        public void delete() {
            BasicDrawing.this.markers.remove(this);
            BasicDrawing.this.repaint();
        }

    };

    // Default path color.
    public static final Color DEFAULT_PATH_COLOR = new Color(255, 0, 255);

    // Default palette.
    public static final GraphPalette DEFAULT_PALETTE = new BasicGraphPalette();

    // Default marker width
    private static final int DEFAULT_MARKER_WIDTH = 10;

    private double long1, long2, lat1, lat2;

    // Width and height of the image
    private final int width, height;

    private ZoomAndPanListener zoomAndPanListener;

    //
    private Image graphImage;
    private final Graphics2D graphGraphics;

    // Image for path / points
    private Image overlayImage;
    private Graphics2D overlayGraphics;

    // List of image for markers
    private List<BasicMarkerTracker> markers = new ArrayList<>();

    // Mapping DrawingClickListener -> MouseEventListener
    private Map<DrawingClickListener, MouseListener> listenerMapping = new IdentityHashMap<>();

    /**
     * Create a new BasicDrawing.
     * 
     */
    public BasicDrawing() {

        this.zoomAndPanListener = new ZoomAndPanListener(this, ZoomAndPanListener.DEFAULT_MIN_ZOOM_LEVEL, 20, 1.2);
        this.addMouseListener(zoomAndPanListener);
        this.addMouseMotionListener(zoomAndPanListener);
        this.addMouseWheelListener(zoomAndPanListener);

        this.width = 2000;
        this.height = 1600;

        BufferedImage img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_3BYTE_BGR);
        this.graphImage = img;
        this.graphGraphics = img.createGraphics();
        this.graphGraphics.setBackground(Color.WHITE);

        img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_4BYTE_ABGR);
        this.overlayImage = img;
        this.overlayGraphics = img.createGraphics();
        this.overlayGraphics.setBackground(new Color(0, 0, 0, 0));

        this.zoomAndPanListener.setCoordTransform(this.graphGraphics.getTransform());

        this.long1 = -180;
        this.long2 = 180;
        this.lat1 = -90;
        this.lat2 = 90;

        this.clear();
        this.repaint();

    }

    @Override
    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setTransform(zoomAndPanListener.getCoordTransform());

        // Draw graph
        g.drawImage(graphImage, 0, 0, this);

        // Draw overlays (path, etc.)
        g.drawImage(overlayImage, 0, 0, this);

        // Draw markers
        for (BasicMarkerTracker mtracker: markers) {
            BufferedImage img = mtracker.image;
            int px = this.projx(mtracker.getPoint().getLongitude());
            int py = this.projy(mtracker.getPoint().getLatitude());
            g.drawImage(img, px - img.getWidth() / 2, py - img.getHeight(), this);
        }
    }

    protected void setBB(double long1, double long2, double lat1, double lat2) {

        if (long1 > long2 || lat1 > lat2) {
            throw new Error("DessinVisible.setBB : mauvaises coordonnees.");
        }

        this.long1 = long1;
        this.long2 = long2;
        this.lat1 = lat1;
        this.lat2 = lat2;

        double scale = 1 / Math.max(this.width / (double) this.getWidth(), this.height / (double) this.getHeight());

        this.zoomAndPanListener.getCoordTransform().setToIdentity();
        this.zoomAndPanListener.getCoordTransform().translate((this.getWidth() - this.width * scale) / 2,
                (this.getHeight() - this.height * scale) / 2);
        this.zoomAndPanListener.getCoordTransform().scale(scale, scale);
        this.zoomAndPanListener.setZoomLevel(0);
        this.repaint();

    }

    private int projx(double lon) {
        return (int) (width * (lon - this.long1) / (this.long2 - this.long1));
    }

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

    protected void setWidth(int width) {
        this.overlayGraphics.setStroke(new BasicStroke(width));
    }

    protected void setColor(Color col) {
        this.overlayGraphics.setColor(col);
    }

    @Override
    public void clear() {
        this.graphGraphics.clearRect(0, 0, this.width, this.height);
        this.overlayGraphics.clearRect(0, 0, this.width, this.height);
        this.markers.clear();
    }

    @Override
    public void drawLine(Point from, Point to) {
        int x1 = this.projx(from.getLongitude());
        int x2 = this.projx(to.getLongitude());
        int y1 = this.projy(from.getLatitude());
        int y2 = this.projy(to.getLatitude());

        overlayGraphics.drawLine(x1, y1, x2, y2);
        this.repaint();
    }

    @Override
    public void drawLine(Point from, Point to, int width) {
        setWidth(width);
        drawLine(from, to);
    }

    @Override
    public void drawLine(Point from, Point to, int width, Color color) {
        setWidth(width);
        setColor(color);
        drawLine(from, to);
    }

    @Override
    public MarkerTracker drawMarker(Point point) {
        return drawMarker(point, this.overlayGraphics.getColor());
    }

    @Override
    public MarkerTracker drawMarker(Point point, Color color) {
        /*
         * BufferedImage img = new BufferedImage(DEFAULT_MARKER_WIDTH,
         * DEFAULT_MARKER_WIDTH, BufferedImage.TYPE_4BYTE_ABGR); Graphics2D gr =
         * img.createGraphics(); gr.setColor(color); gr.fillOval(0, 0,
         * DEFAULT_MARKER_WIDTH, DEFAULT_MARKER_WIDTH);
         */
        BufferedImage img = MarkerUtils.getMarkerForColor(color);
        Graphics2D gr = img.createGraphics();
        gr.scale(0.3, 0.3);
        BasicMarkerTracker marker = new BasicMarkerTracker(point, img);
        this.markers.add(marker);
        this.repaint();
        return marker;
    }

    @Override
    public void drawPoint(Point point, int width, Color color) {
        setWidth(width);
        setColor(color);
        int x = this.projx(point.getLongitude()) - DEFAULT_MARKER_WIDTH / 2;
        int y = this.projy(point.getLatitude()) - DEFAULT_MARKER_WIDTH / 2;
        overlayGraphics.fillOval(x, y, DEFAULT_MARKER_WIDTH, DEFAULT_MARKER_WIDTH);
        this.repaint();
    }

    /**
     * Draw the given arc.
     * 
     * @param arc Arc to draw.
     * @param palette Palette to use to retrieve color and width for arc, or null to
     *        use current settings.
     */
    public void drawArc(Arc arc, GraphPalette palette) {
        List<Point> pts = arc.getPoints();
        if (!pts.isEmpty()) {
            if (palette != null) {
                setColor(palette.getColorForType(arc.getInfo().getType()));
                setWidth(palette.getWidthForType(arc.getInfo().getType()));
            }
            Iterator<Point> it1 = pts.iterator();
            Point prev = it1.next();
            while (it1.hasNext()) {
                Point curr = it1.next();
                drawLine(prev, curr);
                prev = curr;
            }
        }
    }

    /**
     * Initialize the drawing for the given graph.
     * 
     * @param graph
     */
    public void initialize(Graph graph) {
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

        double deltaLon = 0.02 * (maxLon - minLon), deltaLat = 0.02 * (maxLat - minLat);

        setBB(minLon - deltaLon, maxLon + deltaLon, minLat - deltaLat, maxLat + deltaLat);
    }

    @Override
    public void drawGraph(Graph graph, GraphPalette palette) {
        clear();
        Graphics2D oldOverlayGraphics = this.overlayGraphics;
        this.overlayGraphics = this.graphGraphics;
        initialize(graph);
        for (Node node: graph.getNodes()) {
            for (Arc arc: node.getSuccessors()) {
                drawArc(arc, palette);
            }
        }
        this.overlayGraphics = oldOverlayGraphics;
    }

    @Override
    public void drawGraph(Graph graph) {
        drawGraph(graph, DEFAULT_PALETTE);
    }

    @Override
    public void drawPath(Path path, Color color, boolean markers) {
        setColor(color);
        setWidth(2);
        for (Arc arc: path.getArcs()) {
            drawArc(arc, null);
        }
        if (markers) {
            drawMarker(path.getOrigin().getPoint(), color);
            drawMarker(path.getDestination().getPoint(), color);
        }
    }

    @Override
    public void drawPath(Path path, Color color) {
        drawPath(path, color, true);
    }

    @Override
    public void drawPath(Path path) {
        drawPath(path, DEFAULT_PATH_COLOR);
    }

    @Override
    public void drawPath(Path path, boolean markers) {
        drawPath(path, DEFAULT_PATH_COLOR, markers);
    }

    @SuppressWarnings("unused")
    private void putText(Point point, String txt) {
        int x = this.projx(point.getLongitude());
        int y = this.projy(point.getLatitude());
        graphGraphics.drawString(txt, x, y);
        this.repaint();
    }

}
