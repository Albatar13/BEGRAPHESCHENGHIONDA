package org.insa.graphs.algorithm.utils;

import java.util.ArrayList;

public class BinaryHeapFormatter {

    /**
     * This class is used by {@link #toStringTree}, and simply contains three string
     * accumulating. This is an immutable class.
     *
     */
    private static class Context {

        // Output text:
        public final String acu;

        // Margin to get back exactly under the current position:
        public final String margin;

        // Last margin used for the last child of a node. The markers are different:
        public final String lastmargin;

        /**
         * Creaet a new {@code Context}.
         * 
         * @param acu        The accumulated string.
         * @param margin     The current margin.
         * @param lastMargin The last margin used.
         */
        public Context(String acu, String margin, String lastMargin) {
            this.acu = acu;
            this.margin = margin;
            this.lastmargin = lastMargin;
        }

        /**
         * Creates a new context by appending newlines to this context.
         * 
         * @param n Number of newlines to append.
         * 
         * @return a new context with {@code n} newlines appended.
         */
        public Context appendNewlines(int n) {
            if (n <= 0) {
                return this;
            }
            else {
                return (new Context(this.acu + "\n" + this.margin, this.margin, this.lastmargin)
                        .appendNewlines(n - 1));
            }
        }

        /**
         * Creates a new context by appending the given string to this context.
         * 
         * @param count Number of spaces to add to the margin, or {@code null} to use
         *              the length of the string.
         * @param text  String to append.
         * 
         * @return a new context with {@code text} appended.
         */
        public Context appendText(Integer count, String text) {
            int cnt = (count == null) ? text.length() : count;
            final String spaces = new String(new char[cnt]).replace('\0', ' ');
            return new Context(this.acu + text, this.margin + spaces, this.lastmargin + spaces);
        }

        /**
         * Creates a new context by appending a branch to this context.
         * 
         * @param n     Number of spaces to add to the margin, or {@code null} to use
         *              the length of the string.
         * @param label Name of the branch.
         * 
         * @return a new context with the branch appended.
         */
        public Context appendBranch(Integer count, String label) {
            final Context ctxt = this.appendText(count, label);

            if (count == null) {
                return new Context(ctxt.acu + "_", ctxt.margin + "|", ctxt.margin + " ");
            }
            else {
                return new Context(ctxt.acu, ctxt.margin + "|", ctxt.margin + " ")
                        .appendNewlines(1);

            }
        }
    }

    /*
     * Input : ready to write the current node at the current context position.
     * Output : the last character of acu is the last character of the current node.
     */
    protected static <E extends Comparable<E>> Context toStringLoop(BinaryHeap<E> heap,
            Context ctxt, int node, int max_depth) {

        if (max_depth < 0) {
            return ctxt.appendText(null, "...");
        }
        else {
            E nodeval = heap.array.get(node);
            String nodevals = nodeval.toString();

            ArrayList<Integer> childs = new ArrayList<Integer>();
            // Add childs
            int index_left = heap.indexLeft(node);
            int index_right = index_left + 1;

            if (index_left < heap.size()) {
                childs.add(index_left);
            }
            if (index_right < heap.size()) {
                childs.add(index_right);
            }

            Context ctxt2 = childs.isEmpty() ? ctxt.appendText(null, nodevals)
                    : ctxt.appendBranch(1, nodevals);

            for (int ch = 0; ch < childs.size(); ch++) {
                boolean is_last = (ch == childs.size() - 1);
                int child = childs.get(ch);

                if (is_last) {
                    Context ctxt3 = new Context(ctxt2.acu, ctxt2.lastmargin, ctxt2.lastmargin);
                    ctxt2 = new Context(toStringLoop(heap, ctxt3.appendText(null, "___"), child,
                            max_depth - 1).acu, ctxt2.margin, ctxt2.lastmargin);
                }
                else {
                    ctxt2 = new Context(toStringLoop(heap, ctxt2.appendText(null, "___"), child,
                            max_depth - 1).acu, ctxt2.margin, ctxt2.lastmargin).appendNewlines(2);
                }
            }

            return ctxt2;
        }
    }

    /**
     * Creates a multi-lines string representing a tree view of the given binary
     * heap.
     * 
     * @param heap     The binary heap to display.
     * @param maxDepth Maximum depth of the tree to display.
     * 
     * @return a string containing a tree view of the given binary heap.
     */
    public static <E extends Comparable<E>> String toStringTree(BinaryHeap<E> heap, int maxDepth) {
        final Context init_context = new Context("   ", "   ", "   ");
        final Context result = toStringLoop(heap, init_context, 0, maxDepth);
        return result.acu;
    }

    /**
     * Creates a multi-lines string representing a sorted view of the given binary
     * heap.
     * 
     * @param heap         The binary heap to display.
     * @param max_elements Maximum number of elements to display. or {@code -1} to
     *                     display all the elements.
     * 
     * @return a string containing a sorted view the given binary heap.
     */
    public static <E extends Comparable<E>> String toStringSorted(BinaryHeap<E> heap,
            int max_elements) {
        String result = "";
        final BinaryHeap<E> copy = new BinaryHeap<E>(heap);

        final String truncate;
        if (max_elements < 0 || max_elements >= heap.size()) {
            truncate = "";
        }
        else {
            truncate = ", only " + max_elements + " elements are shown";
        }

        result += "========  Sorted HEAP  (size = " + heap.size() + truncate + ")  ========\n\n";

        while (!copy.isEmpty() && max_elements-- != 0) {
            result += copy.deleteMin() + "\n";
        }

        result += "\n--------  End of heap  --------";

        return result;
    }

    public static void main(String[] args) {
        final BinaryHeap<Integer> heap = new BinaryHeap<Integer>();

        for (int i = 0; i < 12; i++) {
            heap.insert(i);
        }

        System.out.println(heap.toStringSorted(-1));
        System.out.println(heap.toStringTree(6));
    }
}
