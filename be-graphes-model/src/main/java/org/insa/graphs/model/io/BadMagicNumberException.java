package org.insa.graphs.model.io;

/**
 * Exception thrown when there is a mismatch between expected and actual magic
 * number.
 *
 */
public class BadMagicNumberException extends BadFormatException {

    /**
     * 
     */
    private static final long serialVersionUID = -2176603967548838864L;

    // Actual and expected magic numbers.
    private int actualNumber, expectedNumber;

    /**
     * Create a new BadMagicNumberException with the given expected and actual magic
     * number.
     * 
     * @param actualNumber Actual magic number (read from a file).
     * @param expectedNumber Expected magic number.
     */
    public BadMagicNumberException(int actualNumber, int expectedNumber) {
        super(String.format("Magic number mismatch, expected %#X, got %#X.", expectedNumber,
                actualNumber));
        this.actualNumber = actualNumber;
        this.expectedNumber = expectedNumber;
    }

    /**
     * @return The actual magic number.
     */
    public int getActualMagicNumber() {
        return actualNumber;
    }

    /**
     * @return The expected magic number.
     */
    public int getExpectedMagicNumber() {
        return expectedNumber;
    }

}
