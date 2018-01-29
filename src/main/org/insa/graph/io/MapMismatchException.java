package org.insa.graph.io;

public class MapMismatchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3076730078387819138L;
	// Actual and expected magic numbers.
	private int actualMapId, expectedMapId;
	
	/**
	 * 
	 * @param actualVersion
	 * @param expectedVersion
	 */
	public MapMismatchException(int actualMapId, int expectedMapId) {
		super();
		this.actualMapId = actualMapId;
		this.expectedMapId = expectedMapId;
	}

	/**
	 * @return
	 */
	public int getActualMapId() {
		return actualMapId;
	}

	/**
	 * @return
	 */
	public int getExpectedMapId() {
		return expectedMapId;
	}
}
