package org.insa.algo.packageswitch;

import org.insa.algo.AbstractAlgorithm;

public abstract class PackageSwitchAlgorithm extends AbstractAlgorithm<PackageSwitchObserver> {

    /**
     * Create a new PackageSwitchAlgorithm with the given data.
     * 
     * @param data
     */
    protected PackageSwitchAlgorithm(PackageSwitchData data) {
        super(data);
    }

    @Override
    public PackageSwitchSolution run() {
        return (PackageSwitchSolution) super.run();
    }

    @Override
    protected abstract PackageSwitchSolution doRun();

    @Override
    public PackageSwitchData getInputData() {
        return (PackageSwitchData) super.getInputData();
    }

}
