package org.insa.algo.weakconnectivity;

import java.awt.Color;
import java.util.ArrayList;

import org.insa.drawing.Drawing;
import org.insa.drawing.graph.GraphDrawing;
import org.insa.graph.Node;

public class WeaklyConnectedComponentGraphicObserver extends WeaklyConnectedComponentObserver {
	
	private static final Color[] COLORS = {
		Color.BLUE, Color.ORANGE, Color.GREEN, Color.YELLOW, Color.RED
	};

	// Drawing + Graph drawing
	private Drawing drawing;
	private GraphDrawing gdrawing;
	
	// Current index color
	private int cindex = 0;
	
	public WeaklyConnectedComponentGraphicObserver(Drawing drawing) {
		super(true);
		this.drawing = drawing;
		this.gdrawing = new GraphDrawing(drawing);
		this.drawing.setAutoRepaint(true);
	}

	@Override
	public void notifyStartComponent(Node curNode) {
		this.drawing.setColor(COLORS[cindex]);
		cindex = (cindex + 1) % COLORS.length;
	}

	@Override
	public void notifyNewNodeInComponent(Node node) {
		this.gdrawing.drawPoint(node.getPoint(), 5);
		this.drawing.repaint();
	}

	@Override
	public void notifyEndComponent(ArrayList<Node> nodes) {
	}

}
