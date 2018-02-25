package org.insa.utility;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BinaryHeapTest {

    private int[] data1 = IntStream.range(0, 20).toArray();
    private int[] data2 = { 8, 1, 6, 3, 4, 5, 9 };

    private BinaryHeap<Integer> heap1, heap2;

    @BeforeEach
    void init() {
        // Create the range heap
        this.heap1 = new BinaryHeap<Integer>();
        this.heap2 = new BinaryHeap<Integer>();

        for (int v: data1)
            this.heap1.insert(v);
        for (int v: data2)
            this.heap2.insert(v);
    }

    @Test
    void testInsert() {
        BinaryHeap<Integer> heap = new BinaryHeap<Integer>();
        int size = 0;
        for (int x: data1) {
            heap.insert(x);
            size += 1;
            assertEquals(heap.size(), size);
        }
        assertEquals(data1.length, heap.size());

        heap = new BinaryHeap<>();
        size = 0;
        for (int x: data2) {
            heap.insert(x);
            size += 1;
            assertEquals(heap.size(), size);
        }
        assertEquals(data2.length, heap.size());
    }

    @Test
    void testDeleteMin() {
        // range 1 (sorted)
        int[] range1 = data1;
        int size = range1.length;
        assertEquals(heap1.size(), size);
        for (int x: range1) {
            assertEquals(heap1.deleteMin().intValue(), x);
            size -= 1;
            assertEquals(heap1.size(), size);
        }
        assertEquals(heap1.size(), 0);
        assertTrue(heap1.isEmpty());

        // range 2 (was not sorted)
        int[] range2 = Arrays.copyOf(data2, data2.length);
        Arrays.sort(range2);
        size = range2.length;
        assertEquals(heap2.size(), size);
        for (int x: range2) {
            assertEquals(heap2.deleteMin().intValue(), x);
            size -= 1;
            assertEquals(heap2.size(), size);
        }
        assertEquals(heap2.size(), 0);
        assertTrue(heap2.isEmpty());
    }

}
