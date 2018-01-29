package org.insa.graph.io;

import java.io.IOException;

public class BadVersionException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7776317018302386042L;
	
	// Actual and expected version..
	private int actualVersion, expectedVersion;
	
	/**
	 * 
	 * @param actualVersion
	 * @param expectedVersion
	 */
	public BadVersionException(int actualVersion, int expectedVersion) {
		super();
		this.actualVersion = actualVersion;
		this.expectedVersion = expectedVersion;
	}
	
	/**
	 * 
	 */
	public int getActualVersion() { return actualVersion; }
	
	/**
	 * 
	 */
	public int getExpectedVersion() { return expectedVersion; }
}
