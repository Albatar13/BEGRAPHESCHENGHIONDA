package org.insa.algo.weakconnectivity;

import java.awt.Color;
import java.util.ArrayList;

import org.insa.drawing.Drawing;
import org.insa.graph.Node;

public class WeaklyConnectedComponentGraphicObserver implements WeaklyConnectedComponentObserver {

    private static final Color[] COLORS = { Color.BLUE, Color.ORANGE, Color.GREEN, Color.YELLOW, Color.RED };

    // Drawing + Graph drawing
    private Drawing drawing;

    // Current index color
    private int cindex = -1;

    public WeaklyConnectedComponentGraphicObserver(Drawing drawing) {
        this.drawing = drawing;
    }

    @Override
    public void notifyStartComponent(Node curNode) {
        cindex = (cindex + 1) % COLORS.length;
    }

    @Override
    public void notifyNewNodeInComponent(Node node) {
        this.drawing.drawPoint(node.getPoint(), 1, COLORS[cindex]);
    }

    @Override
    public void notifyEndComponent(ArrayList<Node> nodes) {
    }

}
