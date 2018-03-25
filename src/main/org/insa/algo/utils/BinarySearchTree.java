package org.insa.algo.utils;

import java.util.SortedSet;
import java.util.TreeSet;

public class BinarySearchTree<E extends Comparable<E>> implements PriorityQueue<E> {

    // Underlying implementation
    private final SortedSet<E> sortedSet;

    /**
     * Create a new empty binary search tree.
     */
    public BinarySearchTree() {
        this.sortedSet = new TreeSet<>();
    }

    /**
     * Create a copy of the given binary search tree.
     * 
     * @param bst Binary search tree to copy.
     */
    public BinarySearchTree(BinarySearchTree<E> bst) {
        this.sortedSet = new TreeSet<>(bst.sortedSet);
    }

    @Override
    public boolean isEmpty() {
        return sortedSet.isEmpty();
    }

    @Override
    public int size() {
        return sortedSet.size();
    }

    @Override
    public void insert(E x) {
        sortedSet.add(x);
    }

    @Override
    public void remove(E x) throws ElementNotFoundException {
        if (!sortedSet.remove(x)) {
            throw new ElementNotFoundException(x);
        }
    }

    @Override
    public E findMin() throws EmptyPriorityQueueException {
        if (isEmpty()) {
            throw new EmptyPriorityQueueException();
        }
        return sortedSet.first();
    }

    @Override
    public E deleteMin() throws EmptyPriorityQueueException {
        E min = findMin();
        remove(min);
        return min;
    }

}
