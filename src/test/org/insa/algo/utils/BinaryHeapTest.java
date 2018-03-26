package org.insa.algo.utils;

public class BinaryHeapTest extends PriorityQueueTest {

    @Override
    public PriorityQueue<MutableInteger> createQueue() {
        return new BinaryHeap<>();
    }

    @Override
    public PriorityQueue<MutableInteger> createQueue(PriorityQueue<MutableInteger> queue) {
        return new BinaryHeap<>((BinaryHeap<MutableInteger>) queue);
    }

}
