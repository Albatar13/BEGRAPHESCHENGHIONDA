package org.insa.graphics;

import org.insa.graph.Graph;

public interface GraphChangeListener {

    /**
     * Event fire when a new graph has been loaded.
     * 
     * @param graph The new graph.
     */
    public void newGraphLoaded(Graph graph);

}
