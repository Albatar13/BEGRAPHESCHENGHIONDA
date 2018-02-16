package org.insa.algo ;

import java.util.ArrayList;

public abstract class AbstractAlgorithm implements Runnable {

    protected AbstractInstance instance;
    protected AbstractSolution solution;

    protected ArrayList<AbstractObserver> observers;
    
    protected AbstractAlgorithm(AbstractInstance instance) {
		this.instance = instance;
		this.observers = new ArrayList<AbstractObserver>();	
		this.solution = null;
    }

    protected AbstractAlgorithm(AbstractInstance instance, ArrayList<AbstractObserver> observers) {
    		this.instance = instance;
    		this.observers = observers;;	
    		this.solution = null;
    }
    
    /**
     * Add an observer to this algorithm.
     * 
     * @param observer
     */
    public void addObserver(AbstractObserver observer) {
    		observers.add(observer);
    }
    
    /**
     * @return The list of observers for this algorithm.
     */
    public ArrayList<AbstractObserver> getObservers() {
    		return observers;
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
    public void run() {
    		this.solution = this.doRun();
    }
    
    /**
     * Abstract method that should be implemented by child class.
     * 
     * @return A solution, if one was found, or null.
     */
    protected abstract AbstractSolution doRun();

}
