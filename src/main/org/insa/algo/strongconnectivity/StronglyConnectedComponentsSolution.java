package org.insa.algo.strongconnectivity;

import java.util.ArrayList;

import org.insa.algo.AbstractSolution;
import org.insa.graph.Node;

public class StronglyConnectedComponentsSolution extends AbstractSolution {

    // Components
    private ArrayList<ArrayList<Node>> components;

    protected StronglyConnectedComponentsSolution(StronglyConnectedComponentsData data) {
        super(data);
    }

    protected StronglyConnectedComponentsSolution(StronglyConnectedComponentsData data,
            Status status, ArrayList<ArrayList<Node>> components) {
        super(data, status);
        this.components = components;
    }

    @Override
    public StronglyConnectedComponentsData getInputData() {
        return (StronglyConnectedComponentsData) super.getInputData();
    }

    /**
     * @return Components of the solution, if any.
     */
    public ArrayList<ArrayList<Node>> getComponents() {
        return components;
    }

}
