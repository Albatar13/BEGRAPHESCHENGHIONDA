package org.insa.drawing.graph;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import org.insa.drawing.Drawing;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Point;
import org.insa.graph.RoadInformation.RoadType;

public class GraphDrawing {

	// Drawing
	private Drawing drawing;

	// Palette
	private GraphPalette palette;
	
	public GraphDrawing(Drawing drawing) {
		this.drawing = drawing;
		this.palette = new BasicGraphPalette();
	}
	
	public GraphDrawing(Drawing drawing, GraphPalette palette) {
		this.drawing = drawing;
		this.palette = palette;
	}

	public void drawLine(Point p1, Point p2) {
		drawing.drawLine(p1.getLongitude(), p1.getLatitude(), 
				p2.getLongitude(), p2.getLatitude());
	}
	
	public void drawPoint(Point p) {
		drawPoint(p, palette.getDefaultPointWidth());
	}
	
	public void drawPoint(Point p, int width) {
		drawing.drawPoint(p.getLongitude(), p.getLatitude(), width);
	}
	
	public void drawPoint(Point p, int width, Color c) {
		drawing.setColor(c);
		drawing.drawPoint(p.getLongitude(), p.getLatitude(), width);
	}

	/**
	 * Draw the given arc with automatic color and width depending
	 * on the road type.
	 * 
	 * @param arc Arc to draw.
	 */
	public void drawArc(Arc arc) {
		drawArc(arc, true);
	}
	
	/**
	 * Draw the given arc.
	 * 
	 * @param arc Arc to draw.
	 * @param autoColorAndWidth Set to true to set color and width based
	 * on the road type of the arc.
	 */
	public void drawArc(Arc arc, boolean autoColorAndWidth) {
		ArrayList<Point> pts = arc.getPoints();
		if (!pts.isEmpty()) {
			if (autoColorAndWidth) {
				drawing.setColor(palette.getColorForType(arc.getInfo().getType()));
				drawing.setWidth(palette.getWidthForType(arc.getInfo().getType()));
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
		double minLon = Double.POSITIVE_INFINITY, minLat = Double.POSITIVE_INFINITY, 
				maxLon = Double.NEGATIVE_INFINITY, maxLat = Double.NEGATIVE_INFINITY;
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
		
		double deltaLon = 0.02 * (maxLon - minLon),
				deltaLat = 0.02 * (maxLat - minLat);

		drawing.setBB(minLon - deltaLon, maxLon + deltaLon, 
				minLat - deltaLat, maxLat + deltaLat);
	}

	
	/**
	 * Clear the drawing and draw the given graph on the drawing.
	 * 
	 * @param graph Graph to draw.
	 */
	public void drawGraph(Graph graph) {

		drawing.clear();
		
		initialize(graph);

		for (Node node: graph.getNodes()) {
			for (Arc arc: node.getSuccessors()) {
				drawArc(arc);
			}
		}
	}

}
