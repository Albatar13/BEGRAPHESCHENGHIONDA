package org.insa.algo.utils;

/**
 * Interface representing a basic priority queue.
 * 
 * @see https://en.wikipedia.org/wiki/Priority_queue
 */
public interface PriorityQueue<E extends Comparable<E>> {

    /**
     * Check if the priority queue is empty.
     * 
     * @return true if the queue is empty, false otherwise.
     */
    public boolean isEmpty();

    /**
     * @return Current size (number of elements) of this queue.
     */
    public int size();

    /**
     * Insert the given element into the queue.
     * 
     * @param x Item to insert.
     */
    public void insert(E x);

    /**
     * Remove the given element from the priority queue.
     * 
     * @param x Item to remove.
     */
    public void remove(E x) throws ElementNotFoundException;

    /**
     * Retrieve (but not remove) the smallest item in the queue.
     * 
     * @return The smallest item in the queue.
     * 
     * @throws EmptyPriorityQueueException if this queue is empty.
     */
    public E findMin() throws EmptyPriorityQueueException;

    /**
     * Remove and return the smallest item from the priority queue.
     * 
     * @return The smallest item in the queue.
     * 
     * @throws EmptyPriorityQueueException if this queue is empty.
     */
    public E deleteMin() throws EmptyPriorityQueueException;

}
