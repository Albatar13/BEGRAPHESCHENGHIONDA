package org.insa.algo.strongconnectivity;

import org.insa.algo.AbstractAlgorithm;

public abstract class StronglyConnectedComponentsAlgorithm
        extends AbstractAlgorithm<StronglyConnectedComponentObserver> {

    /**
     * @param data
     */
    public StronglyConnectedComponentsAlgorithm(StronglyConnectedComponentsData data) {
        super(data);
    }

    @Override
    public StronglyConnectedComponentsSolution run() {
        return (StronglyConnectedComponentsSolution) super.run();
    }

    @Override
    public StronglyConnectedComponentsData getInputData() {
        return (StronglyConnectedComponentsData) super.getInputData();
    }

}
