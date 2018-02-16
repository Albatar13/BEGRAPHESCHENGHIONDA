package org.insa.algo;

public abstract class AbstractObserver {
	
	// Specify if the observer is graphic or not.
	private final boolean isgraphic;
	
	protected AbstractObserver(boolean isGraphic) {
		this.isgraphic = isGraphic;
	}
	
	/**
	 * @return true if this observer is graphic (use drawing to display
	 * information).
	 */
	public boolean isGraphic() {
		return isgraphic;
	}
	
}