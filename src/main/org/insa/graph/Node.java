package org.insa.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Class representing a Node in a {@link Graph}.
 * 
 * This class holds information regarding nodes in the graph together with the
 * successors associated to the nodes.
 * 
 * Nodes are comparable based on their ID.
 *
 */
public final class Node implements Comparable<Node>, Iterable<Arc> {

    /**
     * Link the two given nodes with one or two arcs (depending on roadInformation),
     * with the given attributes.
     * 
     * If `roadInformation.isOneWay()` is true, only a forward arc is created
     * (origin to destination) and added to origin. Otherwise, a corresponding
     * backward arc is created and add to destination.
     * 
     * @param origin Origin of the arc.
     * @param destination Destination of the arc.
     * @param length Length of the arc.
     * @param roadInformation Information corresponding to the arc.
     * @param points Points for the arc.
     * 
     * @return The newly created forward arc (origin to destination).
     */
    public static Arc linkNodes(Node origin, Node destination, float length,
            RoadInformation roadInformation, ArrayList<Point> points) {
        Arc arc = null;
        if (roadInformation.isOneWay()) {
            arc = new ArcForward(origin, destination, length, roadInformation, points);
            origin.addSuccessor(arc);
        }
        else {
            Arc d2o;
            if (origin.getId() < destination.getId()) {
                arc = new ArcForward(origin, destination, length, roadInformation, points);
                d2o = new ArcBackward(arc);
            }
            else {
                Collections.reverse(points);
                d2o = new ArcForward(destination, origin, length, roadInformation, points);
                arc = new ArcBackward(d2o);
            }
            origin.addSuccessor(arc);
            destination.addSuccessor(d2o);
        }
        return arc;
    }

    // ID of the node.
    private final int id;

    // Point of this graph.
    private final Point point;

    // Successors.
    private final ArrayList<Arc> successors;

    /**
     * Create a new Node with the given ID corresponding to the given Point with an
     * empty list of successors.
     * 
     * @param id ID of the node.
     * @param point Position of the node.
     */
    public Node(int id, Point point) {
        this.id = id;
        this.point = point;
        this.successors = new ArrayList<Arc>();
    }

    /**
     * Add a successor to this node.
     * 
     * @param arc Arc to the successor.
     */
    protected void addSuccessor(Arc arc) {
        successors.add(arc);
    }

    /**
     * @return ID of this node.
     */
    public int getId() {
        return id;
    }

    /**
     * @return Number of successors of this node.
     */
    public int getNumberOfSuccessors() {
        return this.successors.size();
    }

    /**
     * @return true if this node has at least one successor.
     */
    public boolean hasSuccessors() {
        return !this.successors.isEmpty();
    }

    @Override
    public Iterator<Arc> iterator() {
        return Collections.unmodifiableList(this.successors).iterator();
    }

    /**
     * @return Location of this node.
     */
    public Point getPoint() {
        return point;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof Node) {
            return getId() == ((Node) other).getId();
        }
        return false;
    }

    /**
     * Compare the ID of this node with the ID of the given node.
     * 
     * @param other Node to compare this node with.
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Node other) {
        return Integer.compare(getId(), other.getId());
    }

}
