package org.insa.graph.io;

import org.insa.graph.Arc;
import org.insa.graph.Node;
import org.insa.graph.RoadInformation;

/**
 * Base interface that should be implemented by classes that want to observe the
 * reading of a graph by a {@link GraphReader}.
 *
 */
public interface GraphReaderObserver {

    /**
     * Notify observer about information on the graph, this method is always the
     * first called
     * 
     * @param mapId ID of the graph.
     */
    public void notifyStartReading(String mapId);

    /**
     * Notify that the graph has been fully read.
     */
    public void notifyEndReading();

    /**
     * Notify that the reader is starting to read node.
     * 
     * @param nNodes Number of nodes to read.
     */
    public void notifyStartReadingNodes(int nNodes);

    /**
     * Notify that a new nodes has been read.
     * 
     * @param node read.
     */
    public void notifyNewNodeRead(Node node);

    /**
     * Notify that the reader is starting to read descriptor/road informations.
     * 
     * @param nDesc Number of descriptors to read.
     */
    public void notifyStartReadingDescriptors(int nDesc);

    /**
     * Notify that a new descriptor has been read.
     * 
     * @param desc Descriptor read.
     */
    public void notifyNewDescriptorRead(RoadInformation desc);

    /**
     * Notify that the reader is starting to read arcs.
     * 
     * @param nArcs Number of arcs to read (!= number of arcs in the graph).
     */
    public void notifyStartReadingArcs(int nArcs);

    /**
     * Notify that a new arc has been read.
     * 
     * @param arc Arc read.
     */
    public void notifyNewArcRead(Arc arc);

}
