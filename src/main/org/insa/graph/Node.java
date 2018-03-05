package org.insa.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node implements Comparable<Node> {

    // ID of the node.
    private final int id;

    // Point of this graph.
    private final Point point;

    // Successors.
    private final ArrayList<Arc> successors;

    /**
     * Create a new Node corresponding to the given Point with an empty list of
     * successors.
     * 
     * @param point
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
     * @return List of successors of this node.
     */
    public List<Arc> getSuccessors() {
        return Collections.unmodifiableList(successors);
    }

    /**
     * @return Point of this node.
     */
    public Point getPoint() {
        return point;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Node) {
            return getId() == ((Node) other).getId();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Node other) {
        return Integer.compare(getId(), other.getId());
    }

}
