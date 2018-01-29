package org.insa.graph.io;

import java.io.IOException;

public class BadMagicNumberException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2176603967548838864L;
	
	// Actual and expected magic numbers.
	private int actualNumber, expectedNumber;
	
	/**
	 * 
	 * @param actualVersion
	 * @param expectedVersion
	 */
	public BadMagicNumberException(int actualNumber, int expectedNumber) {
		super();
		this.actualNumber = actualNumber;
		this.expectedNumber = expectedNumber;
	}
	
	/**
	 * 
	 */
	public int getActualMagicNumber() { return actualNumber; }
	
	/**
	 * 
	 */
	public int getExpectedMagicNumber() { return expectedNumber; }

}
