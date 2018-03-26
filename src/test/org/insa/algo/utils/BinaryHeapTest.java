package org.insa.algo.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
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
            this.heap1.insert(v);
        }

        for (MutableInteger v: data2) {
            this.heap2.insert(v);
        }
    }

    @Test
    public void testIsEmpty() {
        BinaryHeap<MutableInteger> tree = new BinaryHeap<>();
        assertTrue(tree.isEmpty());
        assertFalse(this.heap1.isEmpty());
        assertFalse(this.heap2.isEmpty());
    }

    @Test
    public void testSize() {
        BinaryHeap<MutableInteger> tree = new BinaryHeap<>();
        assertEquals(0, tree.size());
        assertEquals(20, this.heap1.size());
        assertEquals(7, this.heap2.size());
    }

    @Test
    public void testInsert() {
        BinaryHeap<MutableInteger> heap = new BinaryHeap<>();
        int size = 0;
        for (MutableInteger x: data1) {
            heap.insert(x);
            assertEquals(++size, heap.size());
        }
        assertEquals(data1.length, heap.size());

        heap = new BinaryHeap<>();
        size = 0;
        for (MutableInteger x: data2) {
            heap.insert(x);
            assertEquals(++size, heap.size());
        }
        assertEquals(data2.length, heap.size());
    }

    @Test(expected = EmptyPriorityQueueException.class)
    public void testEmptyFindMin() {
        BinaryHeap<MutableInteger> heap = new BinaryHeap<>();
        heap.findMin();
    }

    @Test
    public void testFindMin() {
        assertEquals(0, heap1.findMin().get());
        assertEquals(1, heap2.findMin().get());
    }

    @Test(expected = EmptyPriorityQueueException.class)
    public void testEmptyDeleteMin() {
        BinaryHeap<MutableInteger> heap = new BinaryHeap<>();
        heap.deleteMin();
    }

    @Test
    public void testDeleteMin() {
        // range 1 (sorted)
        int size = data1.length;
        assertEquals(heap1.size(), size);
        for (MutableInteger x: data1) {
            assertEquals(x, heap1.deleteMin());
            size -= 1;
            assertEquals(size, heap1.size());
        }
        assertEquals(0, heap1.size());
        assertTrue(heap1.isEmpty());

        // range 2 (was not sorted)
        MutableInteger[] range2 = Arrays.copyOf(data2, data2.length);
        Arrays.sort(range2);
        size = range2.length;
        assertEquals(heap2.size(), size);
        for (MutableInteger x: range2) {
            assertEquals(x.get(), heap2.deleteMin().get());
            size -= 1;
            assertEquals(size, heap2.size());
        }
        assertEquals(0, heap2.size());
        assertTrue(heap2.isEmpty());
    }

    @Test(expected = ElementNotFoundException.class)
    public void testRemoveEmpty() {
        BinaryHeap<MutableInteger> heap = new BinaryHeap<>();
        heap.remove(new MutableInteger(0));
    }

    @Test(expected = ElementNotFoundException.class)
    public void testRemoveNotFound() {
        heap1.remove(new MutableInteger(20));
    }

    @Test
    public void testRemove() {
        // heap 1
        int size1 = heap1.size();
        int[] deleteOrder1 = new int[] { 12, 17, 18, 19, 4, 5, 3, 2, 0, 9, 10, 16, 8, 14, 13, 15, 7,
                6, 1, 11 };
        for (int i = 0; i < deleteOrder1.length; ++i) {
            // Remove from structure
            heap1.remove(this.data1[deleteOrder1[i]]);

            // Copy the remaining elements
            BinaryHeap<MutableInteger> copyTree = new BinaryHeap<>(heap1);

            // Retrieve all remaining elements in both structures
            MutableInteger[] remains_in = new MutableInteger[deleteOrder1.length - i - 1],
                    remains_cp = new MutableInteger[deleteOrder1.length - i - 1];
            for (int j = 0; j < remains_cp.length; ++j) {
                remains_in[j] = this.data1[deleteOrder1[i + j + 1]];
                remains_cp[j] = copyTree.deleteMin();
            }

            // Check that the copy is now empty, and that both list contains all
            // elements.
            assertTrue(copyTree.isEmpty());
            assertEquals(new HashSet<>(Arrays.asList(remains_in)),
                    new HashSet<>(Arrays.asList(remains_cp)));

            // Check that the size of the original tree is correct.
            assertEquals(--size1, heap1.size());
        }
        assertTrue(heap1.isEmpty());

        // heap 2
        int size2 = heap2.size();
        int[] deleteOrder2 = new int[] { 6, 5, 0, 1, 4, 2, 3 };
        for (int i = 0; i < deleteOrder2.length; ++i) {
            // Remove from structure
            heap2.remove(this.data2[deleteOrder2[i]]);

            // Copy the remaining elements
            BinaryHeap<MutableInteger> copyTree = new BinaryHeap<>(heap2);

            // Retrieve all remaining elements in both structures
            MutableInteger[] remains_in = new MutableInteger[deleteOrder2.length - i - 1],
                    remains_cp = new MutableInteger[deleteOrder2.length - i - 1];
            for (int j = 0; j < remains_cp.length; ++j) {
                remains_in[j] = this.data2[deleteOrder2[i + j + 1]];
                remains_cp[j] = copyTree.deleteMin();
            }

            // Check that the copy is now empty, and that both list contains all
            // elements.
            assertTrue(copyTree.isEmpty());
            assertEquals(new HashSet<>(Arrays.asList(remains_in)),
                    new HashSet<>(Arrays.asList(remains_cp)));

            // Check that the size of the original tree is correct.
            assertEquals(--size2, heap2.size());
        }
        assertTrue(heap2.isEmpty());
    }

    @Test
    public void testRemoveThenAdd() {
        MutableInteger mi5 = this.data1[6];
        heap1.remove(mi5);
        assertEquals(19, heap1.size());
        mi5.set(-20);
        heap1.insert(mi5);
        assertEquals(20, heap1.size());
        assertEquals(-20, heap1.findMin().get());
    }

}
