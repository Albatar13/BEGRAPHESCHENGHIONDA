package org.insa.algo.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

public class BinaryHeapTest {

    class MutableInteger implements Comparable<MutableInteger> {

        // Actual value
        private int value;

        public MutableInteger(int value) {
            this.value = value;
        }

        /**
         * @return The integer value stored inside this MutableInteger.
         */
        public int get() {
            return this.value;
        }

        /**
         * Update the integer value stored inside this MutableInteger.
         * 
         * @param value New value to set.
         */
        public void set(int value) {
            this.value = value;
        }

        @Override
        public int compareTo(MutableInteger other) {
            return Integer.compare(this.value, other.value);
        }

    };

    // Raw data arrays.
    private MutableInteger[] data1 = IntStream.range(0, 20).mapToObj(MutableInteger::new)
            .toArray(MutableInteger[]::new);
    private MutableInteger[] data2 = Arrays.stream(new int[] { 8, 1, 6, 3, 4, 5, 9 })
            .mapToObj(MutableInteger::new).toArray(MutableInteger[]::new);

    // Actual heap.
    private BinaryHeap<MutableInteger> heap1, heap2;

    @Before
    public void init() {
        // Create the range heap
        this.heap1 = new BinaryHeap<>();
        this.heap2 = new BinaryHeap<>();

        for (MutableInteger v: data1) {
            this.heap1.add(v);
        }

        for (MutableInteger v: data2) {
            this.heap2.add(v);
        }
    }

    @Test
    public void testInsert() {
        BinaryHeap<MutableInteger> heap = new BinaryHeap<>();
        int size = 0;
        for (MutableInteger x: data1) {
            heap.add(x);
            size += 1;
            assertEquals(heap.size(), size);
        }
        assertEquals(data1.length, heap.size());

        heap = new BinaryHeap<>();
        size = 0;
        for (MutableInteger x: data2) {
            heap.add(x);
            size += 1;
            assertEquals(heap.size(), size);
        }
        assertEquals(data2.length, heap.size());
    }

    @Test
    public void testDeleteMin() {
        // range 1 (sorted)
        int size = data1.length;
        assertEquals(heap1.size(), size);
        for (MutableInteger x: data1) {
            assertEquals(heap1.deleteMin(), x);
            size -= 1;
            assertEquals(heap1.size(), size);
        }
        assertEquals(heap1.size(), 0);
        assertTrue(heap1.isEmpty());

        // range 2 (was not sorted)
        MutableInteger[] range2 = Arrays.copyOf(data2, data2.length);
        Arrays.sort(range2);
        size = range2.length;
        assertEquals(heap2.size(), size);
        for (MutableInteger x: range2) {
            assertEquals(heap2.deleteMin().get(), x.get());
            size -= 1;
            assertEquals(heap2.size(), size);
        }
        assertEquals(heap2.size(), 0);
        assertTrue(heap2.isEmpty());
    }

    @Test
    public void testUpdate() {
        MutableInteger newMin = data2[data2.length - 1];
        newMin.set(0);
        heap2.update(newMin);
        assertEquals(heap2.findMin(), newMin);
    }

}
