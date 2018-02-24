package org.insa.graphics;

import java.awt.Color;
import java.util.ArrayList;

import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Point;
import org.insa.graphics.MainWindow.CallableWithNodes;
import org.insa.graphics.drawing.Drawing;
import org.insa.graphics.drawing.DrawingClickListener;

public class MultiPointsClickListener implements DrawingClickListener {

    // Enable/Disable.
    private boolean enabled = false;

    // List of points.
    private ArrayList<Node> points = new ArrayList<Node>();

    // Number of points to find before running.
    private int nTargetPoints = 0;

    // Callable to call when points are reached.
    CallableWithNodes callable = null;

    // Graph
    private final Graph graph;

    // Drawing
    private final Drawing drawing;

    public MultiPointsClickListener(Graph graph, Drawing drawing) {
        this.graph = graph;
        this.drawing = drawing;
    }

    /**
     * @return true if this listener is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enable this listener.
     * 
     * @param nTargetPoints
     *            Number of point to found before calling the callable.
     */
    public void enable(int nTargetPoints, CallableWithNodes callable) {
        this.enabled = true;
        this.nTargetPoints = nTargetPoints;
        this.points.clear();
        this.callable = callable;
    }

    /**
     * Disable this listener.
     */
    public void disable() {
        this.enabled = false;
    }

    @Override
    public void mouseClicked(Point lonlat) {
        if (!isEnabled()) {
            return;
        }
        Node node = graph.findClosestNode(lonlat);
        drawing.drawMarker(node.getPoint(), Color.BLUE);
        points.add(node);
        if (points.size() == nTargetPoints) {
            callable.call(points);
            this.disable();
        }
    }

}
