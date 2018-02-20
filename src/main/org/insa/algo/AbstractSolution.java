package org.insa.algo;

import java.time.Duration;

public abstract class AbstractSolution {

	/**
	 * Possible status for a solution.
	 *
	 */
	public enum Status {
		UNKNOWN,
		INFEASIBLE,
		FEASIBLE,
		OPTIMAL,
	};
	
	// Status of the solution.
	Status status;
	
	// Solving time for the solution
	Duration solvingTime;
	
	// Original instance of the solution
	AbstractInstance instance;

	/**
	 * Create a new abstract solution with unknown status.
	 * 
	 * @param instance
	 */
	protected AbstractSolution(AbstractInstance instance) {
		this.instance = instance;
		this.solvingTime = Duration.ZERO;
		this.status = Status.UNKNOWN;
	}
	
	protected AbstractSolution(AbstractInstance instance, Status status) {
		this.instance = instance;
		this.status = status;
	}
	
	/**
	 * @return Original instance for this solution.
	 */
	public AbstractInstance getInstance() { return instance; }
	
	/**
	 * @return Status of this solution.
	 */
	public Status getStatus() { return status; }
	
	/**
	 * @return Solving time of this solution.
	 */
	public Duration getSolvingTime() { return solvingTime; }
	
	/**
	 * Set the solving time of this solution.
	 * 
	 * @param solvingTime Solving time for the solution.
	 */
	protected void setSolvingTime(Duration solvingTime) {
		this.solvingTime = solvingTime;
	}
	
	/**
	 * @return true if the solution is feasible or optimal.
	 */
	public boolean isFeasible() {
		return status == Status.FEASIBLE || status == Status.OPTIMAL;
	}
	
}
