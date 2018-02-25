package org.insa.graphics.drawing;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Path;
import org.insa.graph.Point;
import org.insa.graphics.drawing.overlays.MarkerOverlay;
import org.insa.graphics.drawing.overlays.MarkerUtils;
import org.insa.graphics.drawing.overlays.Overlay;
import org.insa.graphics.drawing.overlays.PathOverlay;
import org.insa.graphics.drawing.overlays.PolylineAutoScaling;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.awt.graphics.AwtBitmap;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;
import org.mapsforge.map.awt.util.AwtUtil;
import org.mapsforge.map.awt.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

public class MapViewDrawing extends MapView implements Drawing {

    /**
     * 
     */
    private static final long serialVersionUID = 8606967833704938092L;

    private class MapViewOverlay implements Overlay {

        // Marker associated.
        protected Layer[] layers;

        public MapViewOverlay(Layer[] layers) {
            this.layers = layers;
            for (Layer layer: this.layers) {
                MapViewDrawing.this.getLayerManager().getLayers().add(layer);
            }
        }

        @Override
        public void setVisible(boolean visible) {
            for (Layer layer: layers) {
                layer.setVisible(visible);
            }
        }

        @Override
        public boolean isVisible() {
            return this.layers[0].isVisible();
        }

        @Override
        public void delete() {
            Layers mlayers = MapViewDrawing.this.getLayerManager().getLayers();
            for (Layer layer: layers) {
                mlayers.remove(layer);
            }
        }

    };

    private class MapViewMarkerOverlay extends MapViewOverlay implements MarkerOverlay {

        public MapViewMarkerOverlay(Marker marker) {
            super(new Layer[] { marker });
        }

        @Override
        public Point getPoint() {
            Marker marker = (Marker) super.layers[0];
            return new Point(marker.getLatLong().getLongitude(), marker.getLatLong().getLatitude());
        }

        @Override
        public void moveTo(Point point) {
            Marker marker = (Marker) this.layers[0];
            this.delete();
            marker = new Marker(convertPoint(point), marker.getBitmap(), marker.getHorizontalOffset(),
                    marker.getVerticalOffset());
            this.layers[0] = marker;
            MapViewDrawing.this.getLayerManager().getLayers().add(marker);
        }

    };

    private class MapViewPathOverlay extends MapViewOverlay implements PathOverlay {

        public MapViewPathOverlay(PolylineAutoScaling path, Marker origin, Marker destination) {
            super(new Layer[] { path, origin, destination });
        }

        public MapViewPathOverlay(PolylineAutoScaling path) {
            super(new Layer[] { path });
        }

    }

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

    public MapViewDrawing() {
        getMapScaleBar().setVisible(true);
        DisplayModel model = getModel().displayModel;
        this.tileSize = DEFAULT_TILE_SIZE;
        model.setFixedTileSize(this.tileSize);
    }

    /**
     * 
     * @param color
     * @return
     */
    private File getMapsforgeFileFromGraph(Graph graph) {
        // TODO: Find a way to change this...
        Map<Integer, String> idToNames = new HashMap<Integer, String>();
        idToNames.put(0x100, "insa");
        idToNames.put(0x110, "paris");
        idToNames.put(0x200, "mayotte");
        idToNames.put(0x250, "newzealand");
        idToNames.put(0x300, "reunion");
        idToNames.put(0x400, "midip");
        idToNames.put(0x410, "morbihan");

        File file = null;
        if (idToNames.containsKey(graph.getMapId())) {
            file = new File("Maps/" + idToNames.get(graph.getMapId()) + ".mapfg");
            if (!file.exists()) {
                file = new File("Maps/new/" + idToNames.get(graph.getMapId()) + ".mapfg");
            }
        }

        if (file == null || !file.exists()) {
            JFileChooser fileChooser = new JFileChooser("Maps/");
            fileChooser.setFileFilter(new FileNameExtensionFilter("mapsforge files", "" + "mapfg"));
            if (fileChooser.showOpenDialog(this.getParent()) == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
            }
        }

        return file;
    }

    protected LatLong convertPoint(Point point) {
        return new LatLong(point.getLatitude(), point.getLongitude());
    }

    private TileRendererLayer createTileRendererLayer(TileCache tileCache, MapDataStore mapDataStore,
            MapViewPosition mapViewPosition, HillsRenderConfig hillsRenderConfig) {
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore, mapViewPosition, false,
                true, false, GRAPHIC_FACTORY, hillsRenderConfig) {
            @Override
            public boolean onTap(LatLong tapLatLong, org.mapsforge.core.model.Point layerXY,
                    org.mapsforge.core.model.Point tapXY) {
                System.out.println("Tap on: " + tapLatLong);
                Point pt = new Point(tapLatLong.getLongitude(), tapLatLong.getLatitude());
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

    @Override
    public void clear() {
        getLayerManager().getLayers().clear();
        repaint();
    }

    protected Marker createMarker(Point point, Color color) {
        Bitmap bitmap = new AwtBitmap(MarkerUtils.getMarkerForColor(color));
        return new Marker(convertPoint(point), bitmap, 0, -bitmap.getHeight() / 2);
    }

    @Override
    public MarkerOverlay drawMarker(Point point) {
        return drawMarker(point, Color.GREEN);
    }

    @Override
    public MarkerOverlay drawMarker(Point point, Color color) {
        return new MapViewMarkerOverlay(createMarker(point, color));
    }

    @Override
    public void drawPoint(Point point, int width, Color color) {
        // TODO:
    }

    @Override
    public void drawGraph(Graph graph, GraphPalette palette) {

        File graphFile = getMapsforgeFileFromGraph(graph);

        // Tile cache
        TileCache tileCache = AwtUtil.createTileCache(tileSize, getModel().frameBufferModel.getOverdrawFactor(), 1024,
                new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString()));

        // Layers
        Layers layers = getLayerManager().getLayers();

        MapDataStore mapDataStore = new MapFile(graphFile);
        TileRendererLayer tileRendererLayer = createTileRendererLayer(tileCache, mapDataStore,
                getModel().mapViewPosition, null);
        layers.add(tileRendererLayer);
        BoundingBox boundingBox = mapDataStore.boundingBox();

        final Model model = getModel();
        if (model.mapViewPosition.getZoomLevel() == 0 || !boundingBox.contains(model.mapViewPosition.getCenter())) {
            byte zoomLevel = LatLongUtils.zoomForBounds(model.mapViewDimension.getDimension(), boundingBox,
                    model.displayModel.getTileSize());
            model.mapViewPosition.setMapPosition(new MapPosition(boundingBox.getCenterPoint(), zoomLevel));
        }
    }

    @Override
    public void drawGraph(Graph graph) {
        drawGraph(graph, null);
    }

    @Override
    public PathOverlay drawPath(Path path, Color color, boolean markers) {
        PolylineAutoScaling line = new PolylineAutoScaling(1, DEFAULT_PATH_COLOR);
        for (Arc arc: path.getArcs()) {
            line.add(arc.getPoints());
        }
        PathOverlay overlay = null;
        if (markers) {
            Marker origin = createMarker(path.getOrigin().getPoint(), DEFAULT_PATH_COLOR),
                    destination = createMarker(path.getDestination().getPoint(), DEFAULT_PATH_COLOR);
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