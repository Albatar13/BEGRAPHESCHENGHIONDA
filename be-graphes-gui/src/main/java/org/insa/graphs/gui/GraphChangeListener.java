package org.insa.graphs.gui;

import org.insa.graphs.model.Graph;

public interface GraphChangeListener {

    /**
     * Event fire when a new graph has been loaded.
     * 
     * @param graph The new graph.
     */
    public void newGraphLoaded(Graph graph);

}
