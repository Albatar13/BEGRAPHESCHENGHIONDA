package org.insa.algo.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

public class BinarySearchTreeTest {

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

    // Actual searchTree.
    private BinarySearchTree<MutableInteger> searchTree1, searchTree2;

    @Before
    public void init() {
        // Create the range searchTree
        this.searchTree1 = new BinarySearchTree<>();
        this.searchTree2 = new BinarySearchTree<>();

        for (MutableInteger v: data1) {
            this.searchTree1.insert(v);
        }

        for (MutableInteger v: data2) {
            this.searchTree2.insert(v);
        }
    }

    @Test
    public void testIsEmpty() {
        BinarySearchTree<MutableInteger> tree = new BinarySearchTree<>();
        assertTrue(tree.isEmpty());
        assertFalse(this.searchTree1.isEmpty());
        assertFalse(this.searchTree2.isEmpty());
    }

    @Test
    public void testSize() {
        BinarySearchTree<MutableInteger> tree = new BinarySearchTree<>();
        assertEquals(0, tree.size());
        assertEquals(20, this.searchTree1.size());
        assertEquals(7, this.searchTree2.size());
    }

    @Test
    public void testInsert() {
        BinarySearchTree<MutableInteger> searchTree = new BinarySearchTree<>();
        int size = 0;
        for (MutableInteger x: data1) {
            searchTree.insert(x);
            assertEquals(++size, searchTree.size());
        }
        assertEquals(data1.length, searchTree.size());

        searchTree = new BinarySearchTree<>();
        size = 0;
        for (MutableInteger x: data2) {
            searchTree.insert(x);
            assertEquals(++size, searchTree.size());
        }
        assertEquals(data2.length, searchTree.size());
    }

    @Test(expected = EmptyPriorityQueueException.class)
    public void testEmptyFindMin() {
        BinarySearchTree<MutableInteger> searchTree = new BinarySearchTree<>();
        searchTree.findMin();
    }

    @Test
    public void testFindMin() {
        assertEquals(0, searchTree1.findMin().get());
        assertEquals(1, searchTree2.findMin().get());
    }

    @Test(expected = EmptyPriorityQueueException.class)
    public void testEmptyDeleteMin() {
        BinarySearchTree<MutableInteger> searchTree = new BinarySearchTree<>();
        searchTree.deleteMin();
    }

    @Test
    public void testDeleteMin() {
        // range 1 (sorted)
        int size = data1.length;
        assertEquals(searchTree1.size(), size);
        for (MutableInteger x: data1) {
            assertEquals(x, searchTree1.deleteMin());
            size -= 1;
            assertEquals(size, searchTree1.size());
        }
        assertEquals(0, searchTree1.size());
        assertTrue(searchTree1.isEmpty());

        // range 2 (was not sorted)
        MutableInteger[] range2 = Arrays.copyOf(data2, data2.length);
        Arrays.sort(range2);
        size = range2.length;
        assertEquals(searchTree2.size(), size);
        for (MutableInteger x: range2) {
            assertEquals(x.get(), searchTree2.deleteMin().get());
            size -= 1;
            assertEquals(size, searchTree2.size());
        }
        assertEquals(0, searchTree2.size());
        assertTrue(searchTree2.isEmpty());
    }

    @Test(expected = ElementNotFoundException.class)
    public void testRemoveEmpty() {
        BinarySearchTree<MutableInteger> searchTree = new BinarySearchTree<>();
        searchTree.remove(new MutableInteger(0));
    }

    @Test(expected = ElementNotFoundException.class)
    public void testRemoveNotFound() {
        searchTree1.remove(new MutableInteger(20));
    }

    @Test
    public void testRemove() {
        // searchTree 1
        int size1 = searchTree1.size();
        int[] deleteOrder1 = new int[] { 12, 17, 18, 19, 4, 5, 3, 2, 0, 9, 10, 16, 8, 14, 13, 15, 7,
                6, 1, 11 };
        for (int x: deleteOrder1) {
            searchTree1.remove(this.data1[x]);
            assertEquals(--size1, searchTree1.size());
        }
        assertTrue(searchTree1.isEmpty());

        // searchTree 2
        int size2 = searchTree2.size();
        int[] deleteOrder2 = new int[] { 6, 5, 0, 1, 4, 2, 3 };
        for (int x: deleteOrder2) {
            searchTree2.remove(this.data2[x]);
            assertEquals(--size2, searchTree2.size());
        }
        assertTrue(searchTree2.isEmpty());
    }

    @Test
    public void testRemoveThenAdd() {
        MutableInteger mi5 = this.data1[6];
        searchTree1.remove(mi5);
        assertEquals(19, searchTree1.size());
        mi5.set(-20);
        searchTree1.insert(mi5);
        assertEquals(20, searchTree1.size());
        assertEquals(-20, searchTree1.findMin().get());
    }

}
