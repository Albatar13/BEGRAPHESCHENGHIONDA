package org.insa.drawing.graph;

import java.awt.Color;

import org.insa.drawing.Drawing;
import org.insa.graph.Arc;
import org.insa.graph.Path;

public class PathDrawing {
	
	// Default color
	public static final Color DEFAULT_PATH_COLOR = new Color(255, 0, 255);
	
	// Drawing
	private Drawing drawing;
	private GraphDrawing graphDrawing;
	
	/**
	 * @param drawing
	 */
	public PathDrawing(Drawing drawing) {
		this.drawing = drawing;
		this.graphDrawing = new GraphDrawing(drawing);
	}
	
	/**
	 * Draw the given path with the given color.
	 * 
	 * @param path
	 * @param color
	 */
	public void drawPath(Path path, Color color) {
		this.graphDrawing.drawPoint(path.getFirstNode().getPoint(), 4, color);
		this.drawing.setColor(color);
		this.drawing.setWidth(2);
		for (Arc arc: path.getArcs()) {
			this.graphDrawing.drawArc(arc, false);
		}
		this.graphDrawing.drawPoint(path.getLastNode().getPoint(), 4, color);
	}
	
	/**
	 * Draw the given path with default color.
	 * 
	 * @param path
	 */
	public void drawPath(Path path) {
		drawPath(path, DEFAULT_PATH_COLOR);
		drawing.repaint();
	}

}
