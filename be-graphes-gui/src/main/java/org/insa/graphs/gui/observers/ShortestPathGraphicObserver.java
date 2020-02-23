package org.insa.graphs.gui.observers;

import java.awt.Color;

import org.insa.graphs.algorithm.shortestpath.ShortestPathObserver;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.overlays.PointSetOverlay;
import org.insa.graphs.model.Node;

public class ShortestPathGraphicObserver implements ShortestPathObserver {

    // Drawing and Graph drawing
    protected Drawing drawing;
    protected PointSetOverlay psOverlay1, psOverlay2;

    public ShortestPathGraphicObserver(Drawing drawing) {
        this.drawing = drawing;
        psOverlay1 = drawing.createPointSetOverlay(1, Color.CYAN);
        psOverlay2 = drawing.createPointSetOverlay(1, Color.BLUE);
    }

    @Override
    public void notifyOriginProcessed(Node node) {
        // drawing.drawMarker(node.getPoint(), Color.RED);
    }

    @Override
    public void notifyNodeReached(Node node) {
        psOverlay1.addPoint(node.getPoint());
    }

    @Override
    public void notifyNodeMarked(Node node) {
        psOverlay2.addPoint(node.getPoint());
    }

    @Override
    public void notifyDestinationReached(Node node) {
        // drawing.drawMarker(node.getPoint(), Color.RED);
    }

}
