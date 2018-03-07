package org.insa.algo.carpooling;

import org.insa.algo.AbstractAlgorithm;

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
