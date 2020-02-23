package org.insa.graphs.gui.drawing.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.DrawingClickListener;
import org.insa.graphs.gui.drawing.GraphPalette;
import org.insa.graphs.gui.drawing.overlays.MarkerAutoScaling;
import org.insa.graphs.gui.drawing.overlays.MarkerOverlay;
import org.insa.graphs.gui.drawing.overlays.MarkerUtils;
import org.insa.graphs.gui.drawing.overlays.Overlay;
import org.insa.graphs.gui.drawing.overlays.PathOverlay;
import org.insa.graphs.gui.drawing.overlays.PointSetOverlay;
import org.insa.graphs.gui.drawing.overlays.PolylineAutoScaling;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Point;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.core.util.Parameters;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;
import org.mapsforge.map.awt.util.AwtUtil;
import org.mapsforge.map.awt.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polygon;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

/**
 *
 */
public class MapViewDrawing extends MapView implements Drawing {

    /**
     * 
     */
    private static final long serialVersionUID = 8606967833704938092L;

    /**
     * Base Overlay for MapViewDrawing overlays.
     *
     */
    private abstract class MapViewOverlay implements Overlay {

        // Marker associated.
        protected Layer[] layers;

        // Current color
        protected Color color;

        public MapViewOverlay(Layer[] layers, Color color) {
            this.layers = layers;
            for (Layer layer: this.layers) {
                MapViewDrawing.this.getLayerManager().getLayers().add(layer);
            }
            this.color = color;
        }

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
            for (Layer layer: layers) {
                layer.setVisible(visible);
            }
        }

        @Override
        public boolean isVisible() {
            if (this.layers.length == 0) {
                return true;
            }
            return this.layers[0].isVisible();
        }

        @Override
        public void delete() {
            Layers mlayers = MapViewDrawing.this.getLayerManager().getLayers();
            for (Layer layer: layers) {
                mlayers.remove(layer);
            }
        }

        @Override
        public void redraw() {
            MapViewDrawing.this.getLayerManager().redrawLayers();
        }
    };

    /**
     * MarkerOverlay for MapViewDrawing.
     *
     */
    private class MapViewMarkerOverlay extends MapViewOverlay implements MarkerOverlay {

        private final AlphaMode alphaMode;
        private Color innerColor;

        public MapViewMarkerOverlay(Marker marker, Color outer, Color innerColor,
                AlphaMode alphaMode) {
            super(new Layer[] { marker }, outer);
            this.innerColor = innerColor;
            this.alphaMode = alphaMode;
        }

        @Override
        public Point getPoint() {
            Marker marker = (Marker) super.layers[0];
            return new Point((float) marker.getLatLong().getLongitude(),
                    (float) marker.getLatLong().getLatitude());
        }

        @Override
        public void setColor(Color outer) {
            this.innerColor = this.innerColor.equals(this.color) ? outer : this.innerColor;
            super.setColor(color);
            MarkerAutoScaling marker = (MarkerAutoScaling) super.layers[0];
            marker.setImage(MarkerUtils.getMarkerForColor(color, this.innerColor, this.alphaMode));
        }

        @Override
        public void moveTo(Point point) {
            MarkerAutoScaling marker = (MarkerAutoScaling) this.layers[0];
            this.delete();
            marker = new MarkerAutoScaling(convertPoint(point), marker.getImage());
            this.layers[0] = marker;
            MapViewDrawing.this.getLayerManager().getLayers().add(marker);
        }

    };

    /**
     * PathOverlay for MapViewDrawing.
     *
     */
    private class MapViewPathOverlay extends MapViewOverlay implements PathOverlay {

        public MapViewPathOverlay(PolylineAutoScaling path, MarkerAutoScaling origin,
                MarkerAutoScaling destination) {
            super(new Layer[] { path, origin, destination }, path.getColor());
        }

        public MapViewPathOverlay(PolylineAutoScaling path) {
            super(new Layer[] { path }, path.getColor());
        }

        @Override
        public void setColor(Color color) {
            super.setColor(color);
            ((PolylineAutoScaling) this.layers[0]).setColor(color);
            ((MarkerAutoScaling) this.layers[1])
                    .setImage(MarkerUtils.getMarkerForColor(color, color, AlphaMode.TRANSPARENT));
            ((MarkerAutoScaling) this.layers[2])
                    .setImage(MarkerUtils.getMarkerForColor(color, color, AlphaMode.TRANSPARENT));
        }

    }

    /**
     * PointSetOverlay for MapViewDrawing - Not currently implemented.
     *
     */
    private class MapViewPointSetOverlay extends MapViewOverlay implements PointSetOverlay {

        private List<Point> points = new ArrayList<>();
        private final Polygon polygon;

        private List<Point> convexHull(List<Point> p) {
            if (p.isEmpty()) {
                return new ArrayList<>();
            }
            p.sort((p1, p2) -> Float.compare(p1.getLongitude(), p2.getLongitude()));
            List<Point> h = new ArrayList<>();

            // lower hull
            for (Point pt: p) {
                while (h.size() >= 2 && !ccw(h.get(h.size() - 2), h.get(h.size() - 1), pt)) {
                    h.remove(h.size() - 1);
                }
                h.add(pt);
            }

            // upper hull
            int t = h.size() + 1;
            for (int i = p.size() - 1; i >= 0; i--) {
                Point pt = p.get(i);
                while (h.size() >= t && !ccw(h.get(h.size() - 2), h.get(h.size() - 1), pt)) {
                    h.remove(h.size() - 1);
                }
                h.add(pt);
            }

            h.remove(h.size() - 1);
            return h;
        }

        // ccw returns true if the three points make a counter-clockwise turn
        private boolean ccw(Point a, Point b, Point c) {
            return ((b.getLongitude() - a.getLongitude())
                    * (c.getLatitude() - a.getLatitude())) > ((b.getLatitude() - a.getLatitude())
                            * (c.getLongitude() - a.getLongitude()));
        }

        public MapViewPointSetOverlay() {
            super(new Layer[] { new Polygon(GRAPHIC_FACTORY.createPaint(), null, GRAPHIC_FACTORY) },
                    Color.BLACK);
            polygon = (Polygon) this.layers[0];
        }

        @Override
        public void setColor(Color color) {
            super.setColor(color);
            polygon.getPaintFill().setColor(GRAPHIC_FACTORY.createColor(100, color.getRed(),
                    color.getGreen(), color.getBlue()));
        }

        @Override
        public void setWidth(int width) {
        }

        @Override
        public void setWidthAndColor(int width, Color color) {
            setWidth(width);
            setColor(color);
        }

        @Override
        public void addPoint(Point point) {
            points.add(point);
            this.points = convexHull(points);
            polygon.setPoints(this.points.stream().map(MapViewDrawing.this::convertPoint)
                    .collect(Collectors.toList()));
            polygon.requestRedraw();
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

    };

    // Default path color.
    public static final Color DEFAULT_PATH_COLOR = new Color(66, 134, 244);

    // Graphic factory.
    private static final GraphicFactory GRAPHIC_FACTORY = AwtGraphicFactory.INSTANCE;

    // Default tile size.
    private static final int DEFAULT_TILE_SIZE = 512;

    // List of listeners.
    private ArrayList<DrawingClickListener> drawingClickListeners = new ArrayList<>();

    // Tile size
    private int tileSize;

    // Zoom controls
    private MapZoomControls zoomControls;

    public MapViewDrawing() {
        super();
        Parameters.NUMBER_OF_THREADS = 2;
        Parameters.SQUARE_FRAME_BUFFER = false;

        getMapScaleBar().setVisible(true);
        DisplayModel model = getModel().displayModel;
        this.tileSize = DEFAULT_TILE_SIZE;
        model.setFixedTileSize(this.tileSize);

        this.setZoomLevelMin((byte) 0);
        this.setZoomLevelMax((byte) 20);

        // Try...
        try {
            this.zoomControls = new MapZoomControls(this, 0, 0, 20);
            this.zoomControls.addZoomInListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getModel().mapViewPosition.zoomIn();
                }
            });
            this.zoomControls.addZoomOutListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getModel().mapViewPosition.zoomOut();
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mapsforge.map.awt.view.MapView#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (this.zoomControls != null) {
            this.zoomControls.setZoomLevel(this.getModel().mapViewPosition.getZoomLevel());
            this.zoomControls.draw((Graphics2D) graphics,
                    getWidth() - this.zoomControls.getWidth() - 20,
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
        getLayerManager().getLayers().clear();
        repaint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.insa.graphics.drawing.Drawing#clearOverlays()
     */
    @Override
    public void clearOverlays() {
        Layers layers = getLayerManager().getLayers();
        for (Layer layer: layers) {
            if (layer instanceof PolylineAutoScaling || layer instanceof MarkerAutoScaling) {
                getLayerManager().getLayers().remove(layer, false);
            }
        }
        repaint();
    }

    protected LatLong convertPoint(Point point) {
        return new LatLong(point.getLatitude(), point.getLongitude());
    }

    private TileRendererLayer createTileRendererLayer(TileCache tileCache,
            MapDataStore mapDataStore, IMapViewPosition mapViewPosition,
            HillsRenderConfig hillsRenderConfig) {
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                mapViewPosition, false, true, false, GRAPHIC_FACTORY, hillsRenderConfig) {
            @Override
            public boolean onTap(LatLong tapLatLong, org.mapsforge.core.model.Point layerXY,
                    org.mapsforge.core.model.Point tapXY) {
                if (zoomControls.contains(new java.awt.Point((int) tapXY.x, (int) tapXY.y))) {
                    return false;
                }
                Point pt = new Point((float) tapLatLong.getLongitude(),
                        (float) tapLatLong.getLatitude());
                for (DrawingClickListener listener: MapViewDrawing.this.drawingClickListeners) {
                    listener.mouseClicked(pt);
                }
                return true;
            }
        };
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);
        return tileRendererLayer;
    }

    @Override
    public void addDrawingClickListener(DrawingClickListener listener) {
        this.drawingClickListeners.add(listener);
    }

    @Override
    public void removeDrawingClickListener(DrawingClickListener listener) {
        this.drawingClickListeners.remove(listener);
    }

    protected MarkerAutoScaling createMarker(Point point, Color outer, Color inner,
            AlphaMode mode) {
        Image image = MarkerUtils.getMarkerForColor(outer, inner, mode);
        return new MarkerAutoScaling(convertPoint(point), image);
    }

    @Override
    public MarkerOverlay drawMarker(Point point, Color outer, Color inner, AlphaMode mode) {
        return new MapViewMarkerOverlay(createMarker(point, outer, inner, mode), outer, inner,
                mode);
    }

    @Override
    public PointSetOverlay createPointSetOverlay() {
        return new MapViewPointSetOverlay();
    }

    @Override
    public PointSetOverlay createPointSetOverlay(int width, Color color) {
        PointSetOverlay ps = new MapViewPointSetOverlay();
        ps.setWidthAndColor(width, color);
        return ps;
    }

    public void drawGraph(File file) {

        // Tile cache
        TileCache tileCache = AwtUtil.createTileCache(tileSize,
                getModel().frameBufferModel.getOverdrawFactor(), 1024,
                new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString()));

        // Layers
        Layers layers = getLayerManager().getLayers();

        MapDataStore mapDataStore = new MapFile(file);
        TileRendererLayer tileRendererLayer = createTileRendererLayer(tileCache, mapDataStore,
                getModel().mapViewPosition, null);
        layers.add(tileRendererLayer);
        BoundingBox boundingBox = mapDataStore.boundingBox();

        final Model model = getModel();
        if (model.mapViewPosition.getZoomLevel() == 0
                || !boundingBox.contains(model.mapViewPosition.getCenter())) {
            byte zoomLevel = LatLongUtils.zoomForBounds(model.mapViewDimension.getDimension(),
                    boundingBox, model.displayModel.getTileSize());
            model.mapViewPosition
                    .setMapPosition(new MapPosition(boundingBox.getCenterPoint(), zoomLevel));
            zoomControls.setZoomLevel(zoomLevel);
        }

    }

    @Override
    public void drawGraph(Graph graph, GraphPalette palette) {
        throw new RuntimeException("Not implemented, use drawGraph(File).");
    }

    @Override
    public void drawGraph(Graph graph) {
        throw new RuntimeException("Not implemented, use drawGraph(File).");
    }

    @Override
    public PathOverlay drawPath(Path path, Color color, boolean markers) {
        PolylineAutoScaling line = new PolylineAutoScaling(1, color);
        ArrayList<Point> points = new ArrayList<>(path.getArcs().size() * 4);
        for (Arc arc: path.getArcs()) {
            points.addAll(arc.getPoints());
        }
        line.addAll(points);
        PathOverlay overlay = null;
        if (markers) {
            MarkerAutoScaling origin = createMarker(path.getOrigin().getPoint(), color, color,
                    AlphaMode.TRANSPARENT),
                    destination = createMarker(path.getDestination().getPoint(), color, color,
                            AlphaMode.TRANSPARENT);
            overlay = new MapViewPathOverlay(line, origin, destination);
        }
        else {
            overlay = new MapViewPathOverlay(line);
        }
        return overlay;
    }

    @Override
    public PathOverlay drawPath(Path path, Color color) {
        return drawPath(path, color, true);
    }

    @Override
    public PathOverlay drawPath(Path path) {
        return drawPath(path, DEFAULT_PATH_COLOR, true);
    }

    @Override
    public PathOverlay drawPath(Path path, boolean markers) {
        return drawPath(path, DEFAULT_PATH_COLOR, markers);
    }

}
