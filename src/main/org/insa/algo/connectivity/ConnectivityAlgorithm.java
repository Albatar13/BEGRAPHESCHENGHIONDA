package org.insa.algo.connectivity ;

import java.io.* ;

import org.insa.algo.AbstractAlgorithm;
import org.insa.algo.AbstractSolution;

public class ConnectivityAlgorithm extends AbstractAlgorithm {

	/**
	 * 
	 * @param instance
	 * @param logOutput
	 */
	public ConnectivityAlgorithm(ConnectivityInstance instance, PrintStream logOutput) {
		super(instance, logOutput);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AbstractSolution doRun() {
		ConnectivityInstance instance = (ConnectivityInstance)getInstance();
		ConnectivitySolution solution = null;
		// TODO: 
		return solution;
	}

}
