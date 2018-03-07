package org.insa.algo;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

/**
 * Base class for algorithm classes.
 *
 * @param <Observer> Observer type for the algorithm.
 */
public abstract class AbstractAlgorithm<Observer> {

    // Input data for the algorithm
    protected final AbstractInputData data;

    // List of observers for the algorithm
    protected final ArrayList<Observer> observers;

    /**
     * Create a new algorithm with an empty list of observers.
     * 
     * @param data Input data for the algorithm.
     */
    protected AbstractAlgorithm(AbstractInputData data) {
        this.data = data;
        this.observers = new ArrayList<Observer>();
    }

    /**
     * Create a new algorithm with the given list of observers.
     * 
     * @param data Input data for the algorithm.
     * @param observers Initial list of observers for the algorithm.
     */
    protected AbstractAlgorithm(AbstractInputData data, ArrayList<Observer> observers) {
        this.data = data;
        this.observers = observers;
    }

    /**
     * Add an observer to this algorithm.
     * 
     * @param observer Observer to add to this algorithm.
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
     * @return Input for this algorithm.
     */
    public AbstractInputData getInputData() {
        return data;
    }

    /**
     * Run the algorithm and return the solution.
     * 
     * This methods internally time the call to doRun() and update the result of the
     * call with the computed solving time.
     * 
     * @return The solution found by the algorithm (may not be a feasible solution).
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
     * @return The solution found, must not be null (use an infeasible or unknown
     * status if necessary).
     */
    protected abstract AbstractSolution doRun();

}
