package org.insa.graphics;

import java.awt.Color;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import org.insa.graph.Node;
import org.insa.graph.Point;
import org.insa.graphics.drawing.DrawingClickListener;

public class MultiPointsClickListener implements DrawingClickListener, RunningAction {

    protected interface CallableWithNodes {

        /**
         * Function called when the given number of nodes is reached.
         * 
         * @param nodes
         */
        void call(ArrayList<Node> nodes);

    };

    // Enable/Disable.
    private boolean enabled = false;

    // List of points.
    private ArrayList<Node> points = new ArrayList<Node>();

    // Starting time
    private Instant startTime;

    // Number of points to find before running.
    private int nTargetPoints = 0;

    // Callable to call when points are reached.
    CallableWithNodes callable = null;

    // Graph
    private final MainWindow mainWindow;

    public MultiPointsClickListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
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
     * @param nTargetPoints Number of point to found before calling the callable.
     */
    public void enable(int nTargetPoints, CallableWithNodes callable) {
        this.enabled = true;
        this.nTargetPoints = nTargetPoints;
        this.points.clear();
        this.callable = callable;
        this.startTime = Instant.now();
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
        Node node = mainWindow.graph.findClosestNode(lonlat);
        mainWindow.drawing.drawMarker(node.getPoint(), Color.BLUE);
        points.add(node);
        if (points.size() == nTargetPoints) {
            callable.call(points);
            this.disable();
        }
    }

    @Override
    public boolean isRunning() {
        return isEnabled();
    }

    @Override
    public void interrupt() {
        disable();
    }

    @Override
    public Instant getStartingTime() {
        return startTime;
    }

    @Override
    public Duration getDuration() {
        return Duration.between(getStartingTime(), Instant.now());
    }

    @Override
    public String getInformation() {
        return getClass().getName();
    }

}
