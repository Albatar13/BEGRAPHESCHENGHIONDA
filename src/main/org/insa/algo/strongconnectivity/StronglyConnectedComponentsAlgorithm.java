package org.insa.algo.strongconnectivity ;

import org.insa.algo.AbstractAlgorithm;

public abstract class StronglyConnectedComponentsAlgorithm extends AbstractAlgorithm<StronglyConnectedComponentObserver> {

	/**
	 * 
	 * @param instance
	 * @param logOutput
	 */
	public StronglyConnectedComponentsAlgorithm(StronglyConnectedComponentsInstance instance) {
		super(instance);
	}
	
	@Override
	public StronglyConnectedComponentsSolution run() {
		return (StronglyConnectedComponentsSolution)super.run();
	}
	
	@Override
	public StronglyConnectedComponentsInstance getInstance() {
		return (StronglyConnectedComponentsInstance)super.getInstance();
	}

}
