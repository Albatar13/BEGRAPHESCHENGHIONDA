package org.insa.algo.utils;

/**
 * Interface representing a basic priority queue.
 * 
 * Implementation should enforce the required complexity of each method.
 * 
 */
public interface PriorityQueue<E extends Comparable<E>> {

    /**
     * Check if the priority queue is empty.
     * 
     * <p>
     * <b>Complexity:</b> <i>O(1)</i>
     * </p>
     * 
     * @return true if the queue is empty, false otherwise.
     */
    public boolean isEmpty();

    /**
     * Get the number of elements in this queue.
     * 
     * <p>
     * <b>Complexity:</b> <i>O(1)</i>
     * </p>
     * 
     * @return Current size (number of elements) of this queue.
     */
    public int size();

    /**
     * Insert the given element into the queue.
     * 
     * <p>
     * <b>Complexity:</b> <i>O(log n)</i>
     * </p>
     * 
     * @param x Item to insert.
     */
    public void insert(E x);

    /**
     * Remove the given element from the priority queue.
     * 
     * <p>
     * <b>Complexity:</b> <i>O(log n)</i>
     * </p>
     * 
     * @param x Item to remove.
     */
    public void remove(E x) throws ElementNotFoundException;

    /**
     * Retrieve (but not remove) the smallest item in the queue.
     * 
     * <p>
     * <b>Complexity:</b> <i>O(1)</i>
     * </p>
     * 
     * @return The smallest item in the queue.
     * 
     * @throws EmptyPriorityQueueException if this queue is empty.
     */
    public E findMin() throws EmptyPriorityQueueException;

    /**
     * Remove and return the smallest item from the priority queue.
     * 
     * <p>
     * <b>Complexity:</b> <i>O(log n)</i>
     * </p>
     * 
     * @return The smallest item in the queue.
     * 
     * @throws EmptyPriorityQueueException if this queue is empty.
     */
    public E deleteMin() throws EmptyPriorityQueueException;

}
