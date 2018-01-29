package org.insa.algo ;

import java.io.* ;

public abstract class AbstractAlgorithm {

    protected PrintStream output;
    protected AbstractInstance instance;
    protected AbstractSolution solution;
    
    /**
     * 
     * @param instance
     * @param logOutput
     */
    protected AbstractAlgorithm(AbstractInstance instance, PrintStream logOutput) {
    		this.instance = instance;
    		this.output = logOutput;	
    		this.solution = null;
    }
    
    /**
     * Update the current solution.
     * 
     * @param solution New solution, or null to unset the current solution.
     * 
     */
    protected void updateLastSolution(AbstractSolution solution) {
    		this.solution = solution;
    }
    
    /**
     * @return Instance corresponding to this algorithm.
     */
    public AbstractInstance getInstance() { return instance; }
    
    /**
     * @return Last solution, or null if no solution was stored.
     */
    public AbstractSolution getLastSolution() { return solution; }
        
    /**
     * Run the algorithm and update the current solution.
     * 
     * @return true if a feasible solution was found (even non-optimal).
     */
    public boolean run() {
    		this.solution = this.doRun();
    		return this.solution != null && this.solution.isFeasible();
    }
    
    /**
     * Abstract method that should be implemented by child class.
     * 
     * @return A solution, if one was found, or null.
     */
    protected abstract AbstractSolution doRun();

}
