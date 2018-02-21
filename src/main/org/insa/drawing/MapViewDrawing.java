package org.insa.drawing;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.prefs.Preferences;

import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Path;
import org.insa.graph.Point;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;
import org.mapsforge.map.awt.util.AwtUtil;
import org.mapsforge.map.awt.util.JavaPreferences;
import org.mapsforge.map.awt.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.model.common.Observer;
import org.mapsforge.map.model.common.PreferencesFacade;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import com.google.common.util.concurrent.SettableFuture;

public class MapViewDrawing extends MapView implements Drawing {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8606967833704938092L;
	
	// Default path color.
	public static final Color DEFAULT_PATH_COLOR = new Color(66, 134, 244);
	
	// Graphic factory.
    private static final GraphicFactory GRAPHIC_FACTORY = AwtGraphicFactory.INSTANCE;
    
    // Default tile size.
    private static final int DEFAULT_TILE_SIZE = 512;
	
	// Tile size.
	int tileSize;

	public MapViewDrawing() {
		setBackground(Color.WHITE);
		getMapScaleBar().setVisible(true);
		this.tileSize = DEFAULT_TILE_SIZE;
		DisplayModel model = getModel().displayModel;
        model.setFixedTileSize(tileSize);
        model.setBackgroundColor(convertColor(Color.WHITE));
	}
	
	protected int convertColor(Color color) {
		return GRAPHIC_FACTORY.createColor(color.getAlpha(), color.getRed(), 
				color.getGreen(), color.getBlue());
	}
	
	private Paint createPaintStroke(int width, Color color) {
		Paint paintStroke = AwtGraphicFactory.INSTANCE.createPaint();
		paintStroke.setStyle(Style.STROKE);
		if (width != 0) {
			paintStroke.setStrokeWidth(width);
		}
		if (color != null) {
			paintStroke.setColor(convertColor(color));
		}
		return paintStroke;
	}
	
	/**
	 * 
	 * @param color
	 * @return
	 */
	private static File getMapsforgeFileFromGraph(Graph graph) {
		// TODO: Find a way to change this...
		Map<Integer, String> idToNames = new HashMap<Integer, String>();
		idToNames.put(0x100, "insa");
		idToNames.put(0x101, "insa");
		idToNames.put(0x110, "paris");
		idToNames.put(0x200, "mayotte");
		idToNames.put(0x250, "newzealand");
		idToNames.put(0x300, "reunion");
		idToNames.put(0x400, "midip");
		idToNames.put(0x410, "morbihan");
		
		File file = null;
		if (idToNames.containsKey(graph.getMapId())) {
			file = new File("Maps/" + idToNames.get(graph.getMapId()) + ".mapfg");
		}
		return file;
	}
	
	protected LatLong convertPoint(Point point) {
		return new LatLong(point.getLatitude(), point.getLongitude());
	}
	
    private static TileRendererLayer createTileRendererLayer(TileCache tileCache, MapDataStore mapDataStore, MapViewPosition mapViewPosition, HillsRenderConfig hillsRenderConfig) {
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore, mapViewPosition, false, true, false, GRAPHIC_FACTORY, hillsRenderConfig) {
            @Override
            public boolean onTap(LatLong tapLatLong, org.mapsforge.core.model.Point layerXY, org.mapsforge.core.model.Point tapXY) {
                System.out.println("Tap on: " + tapLatLong);
                return true;
            }
        };
        XmlRenderTheme renderTheme = InternalRenderTheme.DEFAULT;
        tileRendererLayer.setXmlRenderTheme(renderTheme);
        return tileRendererLayer;
    }
		
	@Override
	public void clear() {
		getLayerManager().getLayers().clear();
		repaint();
	}

	@Override
	public void drawLine(Point from, Point to) {
		drawLine(from, to, 0, null);
	}

	@Override
	public void drawLine(Point from, Point to, int width) {
		drawLine(from, to, width, null);
	}

	@Override
	public void drawLine(Point from, Point to, int width, Color color) {
		Paint paintStroke = createPaintStroke(width, color);
        Polyline line = new Polyline(paintStroke, AwtGraphicFactory.INSTANCE);
        line.getLatLongs().add(convertPoint(from));
        line.getLatLongs().add(convertPoint(to));
        getLayerManager().getLayers().add(line);	
       }
	
	@Override
	public void drawMarker(Point point) {
		drawMarker(point, null);
	}

	@Override
	public void drawMarker(Point point, Color color) {
		Marker marker = new Marker(convertPoint(point), GRAPHIC_FACTORY.createBitmap(10, 20), 1, 2);
		getLayerManager().getLayers().add(marker);
	}
	
	@Override
	public void drawPoint(Point point, int width, Color color) {
		// TODO: Maybe do something?
	}

	@Override
	public void drawGraph(Graph graph, GraphPalette palette) {
		
		File graphFile = getMapsforgeFileFromGraph(graph);

        // Tile cache
        TileCache tileCache = AwtUtil.createTileCache(
        		tileSize, getModel().frameBufferModel.getOverdrawFactor(),
            1024, new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString()));
        
        // Layers
        Layers layers = getLayerManager().getLayers();

        MapDataStore mapDataStore = new MapFile(graphFile);
        TileRendererLayer tileRendererLayer = createTileRendererLayer(tileCache, mapDataStore, 
        		getModel().mapViewPosition, null);
        layers.add(tileRendererLayer);
        BoundingBox boundingBox = mapDataStore.boundingBox();
		
        final PreferencesFacade preferencesFacade = new JavaPreferences(Preferences.userNodeForPackage(MapViewDrawing.class));
        final Model model = getModel();
        model.init(preferencesFacade);
        if (model.mapViewPosition.getZoomLevel() == 0 || !boundingBox.contains(model.mapViewPosition.getCenter())) {
            byte zoomLevel = LatLongUtils.zoomForBounds(model.mapViewDimension.getDimension(), boundingBox, model.displayModel.getTileSize());
            model.mapViewPosition.setMapPosition(new MapPosition(boundingBox.getCenterPoint(), zoomLevel));
        }
	}

	@Override
	public void drawGraph(Graph graph) {
		drawGraph(graph, null);
	}

	@Override
	public void drawPath(Path path, Color color, boolean markers) {
		Paint paintStroke = createPaintStroke(5, DEFAULT_PATH_COLOR);
		Polyline line = new Polyline(paintStroke, AwtGraphicFactory.INSTANCE);
        for (Arc arc: path.getArcs()) {
        		ArrayList<Point> points = arc.getPoints();
        		for (int i = 0; i < points.size(); ++i) {
        			line.getLatLongs().add(new LatLong(points.get(i).getLatitude(), points.get(i).getLongitude()));
        		}
        }
        getLayerManager().getLayers().add(line);
        if (markers) {
        		drawMarker(path.getOrigin().getPoint());
        		drawMarker(path.getDestination().getPoint());
        }
	}
	
	@Override
	public void drawPath(Path path, Color color) {
		drawPath(path, color, true);
	}

	@Override
	public void drawPath(Path path) {
		drawPath(path, DEFAULT_PATH_COLOR, true);
	}

	@Override
	public void drawPath(Path path, boolean markers) {
		drawPath(path, DEFAULT_PATH_COLOR, markers);
	}
	
}
