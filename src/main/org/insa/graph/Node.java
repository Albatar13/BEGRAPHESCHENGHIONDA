package org.insa.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a Node in a {@link Graph}.
 * 
 * This class holds information regarding nodes in the graph together with the
 * successors associated to the nodes.
 * 
 * Nodes are comparable based on their ID.
 *
 */
public class Node implements Comparable<Node> {

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
        ArcForward arc = new ArcForward(origin, destination, length, roadInformation, points);
        origin.addSuccessor(arc);
        if (!roadInformation.isOneWay()) {
            destination.addSuccessor(new ArcBackward(arc));
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
     * @return Immutable view of the list of successors of this node.
     */
    public List<Arc> getSuccessors() {
        return Collections.unmodifiableList(successors);
    }

    /**
     * @return Location of this node.
     */
    public Point getPoint() {
        return point;
    }

    @Override
    public boolean equals(Object other) {
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
