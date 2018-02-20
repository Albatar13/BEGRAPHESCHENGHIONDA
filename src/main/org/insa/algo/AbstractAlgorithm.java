package org.insa.algo ;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public abstract class AbstractAlgorithm<Observer> {

    protected AbstractData instance;
    protected ArrayList<Observer> observers;
    
    protected AbstractAlgorithm(AbstractData instance) {
		this.instance = instance;
		this.observers = new ArrayList<Observer>();	
    }

    protected AbstractAlgorithm(AbstractData instance, ArrayList<Observer> observers) {
    		this.instance = instance;
    		this.observers = observers;;	
    }
    
    /**
     * Add an observer to this algorithm.
     * 
     * @param observer
     */
    public void addObserver(Observer observer) {
    		observers.add(observer);
    }
    
    /**
     * @return The list of observers for this algorithm.
     */
    public ArrayList<Observer> getObservers() {
    		return observers;
    }
    
    /**
     * @return Instance corresponding to this algorithm.
     */
    public AbstractData getInstance() { return instance; }
        
    /**
     * Run the algorithm and update the current solution.
     * 
     * @return true if a feasible solution was found (even non-optimal).
     */
    public AbstractSolution run() {
    		Instant start = Instant.now();
    		AbstractSolution solution = this.doRun();
    		solution.setSolvingTime(Duration.between(start, Instant.now()));
    		return solution;
    }
    
    /**
     * Abstract method that should be implemented by child class.
     * 
     * @return A solution, if one was found, or null.
     */
    protected abstract AbstractSolution doRun();

}
