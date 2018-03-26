package org.insa.algo.utils;

public class BinarySearchTreeTest extends PriorityQueueTest {

    @Override
    public PriorityQueue<MutableInteger> createQueue() {
        return new BinarySearchTree<>();
    }

    @Override
    public PriorityQueue<MutableInteger> createQueue(PriorityQueue<MutableInteger> queue) {
        return new BinarySearchTree<>((BinarySearchTree<MutableInteger>) queue);
    }

}
