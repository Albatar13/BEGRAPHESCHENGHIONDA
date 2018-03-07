package org.insa.algo;

import java.time.Duration;

/**
 * Base class for solution classes returned by the algorithm. This class
 * contains the basic information that any solution should have: status of the
 * solution (unknown, infeasible, etc.), solving time and the original input
 * data.
 */
public abstract class AbstractSolution {

    /**
     * Possible status for a solution.
     *
     */
    public enum Status {
        UNKNOWN, INFEASIBLE, FEASIBLE, OPTIMAL,
    };

    // Status of the solution.
    private final Status status;

    // Solving time for the solution.
    private Duration solvingTime;

    // Original input of the solution.
    private final AbstractInputData data;

    /**
     * Create a new abstract solution with unknown status.
     * 
     * @param data
     */
    protected AbstractSolution(AbstractInputData data) {
        this.data = data;
        this.solvingTime = Duration.ZERO;
        this.status = Status.UNKNOWN;
    }

    protected AbstractSolution(AbstractInputData data, Status status) {
        this.data = data;
        this.status = status;
    }

    /**
     * @return Original input for this solution.
     */
    public AbstractInputData getInputData() {
        return data;
    }

    /**
     * @return Status of this solution.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return Solving time of this solution.
     */
    public Duration getSolvingTime() {
        return solvingTime;
    }

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
