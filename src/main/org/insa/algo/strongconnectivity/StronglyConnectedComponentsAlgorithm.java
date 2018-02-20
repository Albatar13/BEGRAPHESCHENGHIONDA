package org.insa.algo.strongconnectivity ;

import org.insa.algo.AbstractAlgorithm;

public abstract class StronglyConnectedComponentsAlgorithm extends AbstractAlgorithm<StronglyConnectedComponentObserver> {

	/**
	 * 
	 * @param instance
	 * @param logOutput
	 */
	public StronglyConnectedComponentsAlgorithm(StronglyConnectedComponentsData instance) {
		super(instance);
	}
	
	@Override
	public StronglyConnectedComponentsSolution run() {
		return (StronglyConnectedComponentsSolution)super.run();
	}
	
	@Override
	public StronglyConnectedComponentsData getInstance() {
		return (StronglyConnectedComponentsData)super.getInstance();
	}

}
