package org.insa.graphs.algorithm.carpooling;

import org.insa.graphs.algorithm.AbstractAlgorithm;

public abstract class CarPoolingAlgorithm extends AbstractAlgorithm<CarPoolingObserver> {

    protected CarPoolingAlgorithm(CarPoolingData data) {
        super(data);
    }

    @Override
    public CarPoolingSolution run() {
        return (CarPoolingSolution) super.run();
    }

    @Override
    protected abstract CarPoolingSolution doRun();

    @Override
    public CarPoolingData getInputData() {
        return (CarPoolingData) super.getInputData();
    }

}
