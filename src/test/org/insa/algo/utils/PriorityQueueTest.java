package org.insa.algo.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public abstract class PriorityQueueTest {

    /**
     * Needs to be implemented by child class to actually provide priority queue
     * implementation.
     * 
     * @return A new instance of a PriorityQueue implementation.
     */
    public abstract PriorityQueue<MutableInteger> createQueue();

    /**
     * Needs to be implemented by child class to actually provide priority queue
     * implementation.
     * 
     * @param queue Queue to copy.
     * 
     * @return Copy of the given queue.
     */
    public abstract PriorityQueue<MutableInteger> createQueue(PriorityQueue<MutableInteger> queue);

    protected static class MutableInteger implements Comparable<MutableInteger> {

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

    protected static class TestParameters<E extends Comparable<E>> {

        // Data to insert
        public final E[] data;
        public final int[] deleteOrder;

        public TestParameters(E[] data, int[] deleteOrder) {
            this.data = data;
            this.deleteOrder = deleteOrder;
        }

    };

    /**
     * Set of parameters.
     * 
     */
    @Parameters
    public static Collection<Object> data() {
        Collection<Object> objects = new ArrayList<>();
        objects.add(new TestParameters<>(new MutableInteger[0], new int[0]));
        objects.add(new TestParameters<>(
                IntStream.range(0, 50).mapToObj(MutableInteger::new).toArray(MutableInteger[]::new),
                IntStream.range(0, 50).toArray()));
        objects.add(new TestParameters<>(
                IntStream.range(0, 20).mapToObj(MutableInteger::new).toArray(MutableInteger[]::new),
                new int[] { 12, 17, 18, 19, 4, 5, 3, 2, 0, 9, 10, 16, 8, 14, 13, 15, 7, 6, 1,
                        11 }));
        objects.add(
                new TestParameters<>(
                        Arrays.stream(new int[] { 8, 1, 6, 3, 4, 5, 9 })
                                .mapToObj(MutableInteger::new).toArray(MutableInteger[]::new),
                        new int[] { 6, 5, 0, 1, 4, 2, 3 }));
        return objects;
    }

    @Parameter
    public TestParameters<MutableInteger> parameters;

    // Actual queue.
    private PriorityQueue<MutableInteger> queue;

    @Before
    public void init() {
        // Create the range queue
        this.queue = createQueue();

        for (MutableInteger v: parameters.data) {
            this.queue.insert(v);
        }
    }

    @Test
    public void testIsEmpty() {
        assertEquals(parameters.data.length == 0, this.queue.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(parameters.data.length, this.queue.size());
    }

    @Test
    public void testInsert() {
        PriorityQueue<MutableInteger> queue = createQueue();
        int size = 0;
        for (MutableInteger x: parameters.data) {
            queue.insert(x);
            assertEquals(++size, queue.size());
        }
        assertEquals(parameters.data.length, queue.size());
        MutableInteger[] range = Arrays.copyOf(parameters.data, parameters.data.length);
        Arrays.sort(range);

        for (MutableInteger mi: range) {
            assertEquals(mi.get(), queue.deleteMin().value);
            assertEquals(--size, queue.size());
        }
    }

    @Test(expected = EmptyPriorityQueueException.class)
    public void testEmptyFindMin() {
        Assume.assumeTrue(queue.isEmpty());
        queue.findMin();
    }

    @Test
    public void testFindMin() {
        Assume.assumeFalse(queue.isEmpty());
        assertEquals(Collections.min(Arrays.asList(parameters.data)).get(), queue.findMin().get());
    }

    @Test(expected = EmptyPriorityQueueException.class)
    public void testEmptyDeleteMin() {
        Assume.assumeTrue(queue.isEmpty());
        queue.deleteMin();
    }

    @Test
    public void testDeleteMin() {
        int size = parameters.data.length;
        assertEquals(queue.size(), size);
        MutableInteger[] range = Arrays.copyOf(parameters.data, parameters.data.length);
        Arrays.sort(range);
        for (MutableInteger x: range) {
            assertEquals(x, queue.deleteMin());
            size -= 1;
            assertEquals(size, queue.size());
        }
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
    }

    @Test(expected = ElementNotFoundException.class)
    public void testRemoveEmpty() {
        Assume.assumeTrue(queue.isEmpty());
        queue.remove(new MutableInteger(0));
    }

    @Test(expected = ElementNotFoundException.class)
    public void testRemoveNotFound() {
        Assume.assumeFalse(queue.isEmpty());
        List<MutableInteger> data = Arrays.asList(parameters.data);
        queue.remove(new MutableInteger(Collections.min(data).get() - 1));
        queue.remove(new MutableInteger(Collections.max(data).get() + 1));
    }

    @Test(expected = ElementNotFoundException.class)
    public void testDeleteThenRemove() {
        Assume.assumeFalse(queue.isEmpty());
        MutableInteger min = queue.deleteMin();
        queue.remove(min);
    }

    @Test(expected = ElementNotFoundException.class)
    public void testRemoveTwice() {
        Assume.assumeFalse(queue.isEmpty());
        queue.remove(parameters.data[4 % parameters.data.length]);
        queue.remove(parameters.data[4 % parameters.data.length]);
    }

    @Test
    public void testRemove() {
        int size1 = queue.size();
        for (int i = 0; i < parameters.deleteOrder.length; ++i) {
            // Remove from structure
            queue.remove(parameters.data[parameters.deleteOrder[i]]);

            // Copy the remaining elements
            PriorityQueue<MutableInteger> copyTree = createQueue(queue);

            // Retrieve all remaining elements in both structures
            ArrayList<MutableInteger> remains_in = new ArrayList<>(),
                    remains_cp = new ArrayList<>();
            for (int j = i + 1; j < parameters.deleteOrder.length; ++j) {
                remains_in.add(parameters.data[parameters.deleteOrder[j]]);
                remains_cp.add(copyTree.deleteMin());
            }
            Collections.sort(remains_in);

            // Check that the copy is now empty, and that both list contains all
            // elements.
            assertTrue(copyTree.isEmpty());
            assertEquals(remains_in, remains_cp);

            // Check that the size of the original tree is correct.
            assertEquals(--size1, queue.size());
        }
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemoveThenAdd() {
        Assume.assumeFalse(queue.isEmpty());
        int min = Collections.min(Arrays.asList(parameters.data)).get();
        for (MutableInteger mi: parameters.data) {
            queue.remove(mi);
            assertEquals(parameters.data.length - 1, queue.size());
            mi.set(--min);
            queue.insert(mi);
            assertEquals(parameters.data.length, queue.size());
            assertEquals(min, queue.findMin().get());
        }
    }

}
