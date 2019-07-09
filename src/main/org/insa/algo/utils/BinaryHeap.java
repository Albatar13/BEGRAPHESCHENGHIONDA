package org.insa.algo.utils;

import java.util.ArrayList;



/**
 * Implements a binary heap containing elements of type E.
 *
 * Note that all comparisons are based on the compareTo method,
 * hence E must implement Comparable
 * 
 * @author Mark Allen Weiss
 * @author DLB
 */
public class BinaryHeap<E extends Comparable<E>> implements PriorityQueue<E> {

    // Number of elements in heap.
    private int currentSize;

    // The heap array.
    private final ArrayList<E> array;

    /**
     * Construct a new empty binary heap.
     */
    public BinaryHeap() {
        this.currentSize = 0;
        this.array = new ArrayList<E>();
    }

    /**
     * Construct a copy of the given heap.
     * 
     * @param heap Binary heap to copy.
     */
    public BinaryHeap(BinaryHeap<E> heap) {
        this.currentSize = heap.currentSize;
        this.array = new ArrayList<E>(heap.array);
    }

    /**
     * Set an element at the given index.
     * 
     * @param index Index at which the element should be set.
     * @param value Element to set.
     */
    private void arraySet(int index, E value) {
        if (index == this.array.size()) {
            this.array.add(value);
        }
        else {
            this.array.set(index, value);
        }
        
    }

    /**
     * @return Index of the parent of the given index.
     */
    private int index_parent(int index) {
        return (index - 1) / 2;
    }

    /**
     * @return Index of the left child of the given index.
     */
    private int index_left(int index) {
        return index * 2 + 1;
    }

    /**
     * Internal method to percolate up in the heap.
     * 
     * @param index Index at which the percolate begins.
     */
    private void percolateUp(int index) {
        E x = this.array.get(index);

        for (; index > 0
                && x.compareTo(this.array.get(index_parent(index))) < 0; index = index_parent(
                        index)) {
            E moving_val = this.array.get(index_parent(index));
            this.arraySet(index, moving_val);
        }

        this.arraySet(index, x);
    }

    /**
     * Internal method to percolate down in the heap.
     * 
     * @param index Index at which the percolate begins.
     */
    private void percolateDown(int index) {
        int ileft = index_left(index);
        int iright = ileft + 1;

        if (ileft < this.currentSize) {
            E current = this.array.get(index);
            E left = this.array.get(ileft);
            boolean hasRight = iright < this.currentSize;
            E right = (hasRight) ? this.array.get(iright) : null;

            if (!hasRight || left.compareTo(right) < 0) {
                // Left is smaller
                if (left.compareTo(current) < 0) {
                    this.arraySet(index, left);
                    this.arraySet(ileft, current);
                    this.percolateDown(ileft);
                }
            }
            else {
                // Right is smaller
                if (right.compareTo(current) < 0) {
                    this.arraySet(index, right);
                    this.arraySet(iright, current);
                    this.percolateDown(iright);
                }
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return this.currentSize == 0;
    }

    @Override
    public int size() {
        return this.currentSize;
    }

    @Override
    public void insert(E x) {
        int index = this.currentSize++;
        this.arraySet(index, x);
        this.percolateUp(index);
    }

    @Override
    public void remove(E x) throws ElementNotFoundException {
        // TODO:
    }

    @Override
    public E findMin() throws EmptyPriorityQueueException {
        if (isEmpty())
            throw new EmptyPriorityQueueException();
        return this.array.get(0);
    }

    @Override
    public E deleteMin() throws EmptyPriorityQueueException {
        E minItem = findMin();
        E lastItem = this.array.get(--this.currentSize);
        this.arraySet(0, lastItem);
        this.percolateDown(0);
        return minItem;
    }

    /* This class is used by toString_tree.
     * It is just a triple of strings. Could it be made simpler in Java ?
     *
     * Printing context, functional style.
     */
    private class Context {
        /* Output text */
        public final String acu ; 

        /* margin: the margin to get back exactly under the current position on the next line. */
        public final String margin ;
        
        /* lastmargin: margin used for the last child of a node. The markers are different. */
        public final String lastmargin ;

        public Context(String a, String b, String c) {
            this.acu = a ;
            this.margin = b ;
            this.lastmargin = c ;
        }

        /* Appends newlines. */
        public Context nl(int n) {
            if (n <= 0) { return this ; }
            else {
                String acu2 = this.acu + "\n" + this.margin ;
                return (new Context(acu2, this.margin, this.lastmargin).nl(n-1)) ;
            }
        }

        /* Adds some text */
        public Context add(Integer count, String s) {
            int cnt = (count==null) ? s.length() : count ;
            String spaces = new String(new char[cnt]).replace('\0', ' ');

            return new Context(this.acu + s, this.margin + spaces, this.lastmargin + spaces) ;
        }

        /* Adds a branch */
        public Context br(Integer count, String label) {
            Context ctxt = this.add(count, label) ;

            if (count == null) {
                return new Context(ctxt.acu + "_",
                                   ctxt.margin + "|",
                                   ctxt.margin + " ") ;
            }
            else {
                return new Context(ctxt.acu,
                                   ctxt.margin + "|",
                                   ctxt.margin + " ").nl(1) ;
            
            }
        }
    }

    /* Input : ready to write the current node at the current context position.
     * Output : the last character of acu is the last character of the current node. */
    public Context toString_loop(Context ctxt, int node, int max_depth) {

        if (max_depth < 0) { return ctxt.add(null,"...") ; }
        else {            
            E nodeval = this.array.get(node) ;
            String nodevals = nodeval.toString() ;
        
            ArrayList<Integer> childs = new ArrayList<Integer>() ;
            // Add childs
            int index_left = this.index_left(node) ;
            int index_right = index_left + 1 ;

            if (index_left < this.currentSize) { childs.add(index_left) ; }
            if (index_right < this.currentSize) { childs.add(index_right) ; }
        
            Context ctxt2 = childs.isEmpty() ? ctxt.add(null,nodevals) : ctxt.br(1, nodevals) ;

            for (int ch = 0 ; ch < childs.size() ; ch++) {
                boolean is_last = (ch == childs.size() - 1) ;
                int child = childs.get(ch) ;

                if (is_last) {
                    Context ctxt3 = new Context( ctxt2.acu, ctxt2.lastmargin, ctxt2.lastmargin) ;
                    ctxt2 = new Context( this.toString_loop(ctxt3.add(null, "___"),child,max_depth-1).acu,
                                         ctxt2.margin,
                                         ctxt2.lastmargin) ;
                }
                else {
                    ctxt2 = new Context(this.toString_loop(ctxt2.add(null,"___"),child,max_depth-1).acu,
                                        ctxt2.margin,
                                        ctxt2.lastmargin).nl(2) ;
                }
            }
        
            return ctxt2 ;
        }
    }
            
        
    // Textual representation of the tree.
    public String toString_tree(int max_depth) {
        Context init_context = new Context("   ", "   ", "   ") ;        
        Context result = this.toString_loop(init_context, 0, max_depth) ;
        return result.acu ;
    }
    
    // Prints the elements, sorted.
    // max_elements: maximal number of elements printed. -1 for infinity.
    public String toString_sorted(int max_elements) {
        String result = "\n" ;
        BinaryHeap<E> copy = new BinaryHeap<E>(this);

        int remaining = max_elements ;

        String truncate = "" ;
        if (max_elements < 0 || max_elements >= this.currentSize) {
            truncate = "" ;
        } else {
            truncate = ", only " + max_elements + " elements are shown";
        }
        
        result += "========  Sorted HEAP  (size = " + this.currentSize + truncate + ")  ========\n\n" ;

        while (!copy.isEmpty() && remaining != 0) {
            result += copy.deleteMin() + "\n" ;
            remaining-- ;
        }

        result += "\n--------  End of heap  --------\n\n" ;

        return result ;        
    }

    public String toString() {
        return this.toString_tree(8) ;
    }

    /* 
    public static void main(String[] args) {
        BinaryHeap<Integer> heap = new BinaryHeap<Integer>() ;

        for (int i = 0 ; i < 50 ; i++) {
            heap.insert(i) ;
        }

        System.out.println(heap.toString_tree(4)) ;        
    }
    */
    
}


