package org.insa.graphs.gui;

import java.time.Duration;
import java.time.Instant;

public interface RunningAction {

    /**
     * @return true if this action is running.
     */
    public boolean isRunning();

    /**
     * Interrupt this action.
     */
    public void interrupt();

    /**
     * @return Starting time of this action.
     */
    public Instant getStartingTime();

    /**
     * @return Current duration of this action.
     */
    public Duration getDuration();

    /**
     * @return Information for this action.
     */
    public String getInformation();

}
