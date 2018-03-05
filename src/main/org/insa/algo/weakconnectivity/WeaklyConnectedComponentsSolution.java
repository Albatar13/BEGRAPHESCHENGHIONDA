package org.insa.algo.weakconnectivity;

import java.util.ArrayList;

import org.insa.algo.AbstractSolution;
import org.insa.graph.Node;

public class WeaklyConnectedComponentsSolution extends AbstractSolution {

    // Components
    private ArrayList<ArrayList<Node>> components;

    protected WeaklyConnectedComponentsSolution(WeaklyConnectedComponentsData data) {
        super(data);
    }

    protected WeaklyConnectedComponentsSolution(WeaklyConnectedComponentsData data, Status status,
            ArrayList<ArrayList<Node>> components) {
        super(data, status);
        this.components = components;
    }

    @Override
    public WeaklyConnectedComponentsData getInputData() {
        return (WeaklyConnectedComponentsData) super.getInputData();
    }

    /**
     * @return Components of the solution, if any.
     */
    public ArrayList<ArrayList<Node>> getComponents() {
        return components;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        int nIsolated = 0;
        int nGt10 = 0;
        for (ArrayList<Node> component: components) {
            if (component.size() == 1) {
                nIsolated += 1;
            }
            else if (component.size() > 10) {
                nGt10 += 1;
            }
        }
        return "Found " + components.size() + " components (" + nGt10 + " with more than 10 nodes, "
                + nIsolated + " isolated nodes) in " + getSolvingTime().getSeconds() + " seconds.";

    }

}
