package org.insa.algo.utils;

public class ElementNotFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Element not found
    private final Object element;

    /**
     * @param element Element that was not found.
     */
    public ElementNotFoundException(Object element) {
        this.element = element;
    }

    /**
     * @return The element that was not found.
     */
    public Object getElement() {
        return this.element;
    }

    @Override
    public String toString() {
        return "element not found: " + element;
    }

}
