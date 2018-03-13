package org.insa.algo.shortestpath;

import org.insa.algo.AbstractInputData;
import org.insa.algo.AbstractSolution;
import org.insa.graph.Path;

public class ShortestPathSolution extends AbstractSolution {

    // Optimal solution.
    private Path path;

    /**
     * {@inheritDoc}
     */
    public ShortestPathSolution(ShortestPathData data) {
        super(data);
    }

    /**
     * Create a new infeasible shortest-path solution for the given input and
     * status.
     * 
     * @param data Original input data for this solution.
     * @param status Status of the solution (UNKNOWN / INFEASIBLE).
     */
    public ShortestPathSolution(ShortestPathData data, Status status) {
        super(data, status);
    }

    /**
     * Create a new shortest-path solution.
     * 
     * @param data Original input data for this solution.
     * @param status Status of the solution (FEASIBLE / OPTIMAL).
     * @param path Path corresponding to the solution.
     */
    public ShortestPathSolution(ShortestPathData data, Status status, Path path) {
        super(data, status);
        this.path = path;
    }

    @Override
    public ShortestPathData getInputData() {
        return (ShortestPathData) super.getInputData();
    }

    /**
     * @return The path of this solution, if any.
     */
    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        String info = null;
        if (!isFeasible()) {
            info = String.format("No path found from node #%d to node #%d",
                    getInputData().getOrigin().getId(), getInputData().getDestination().getId());
        }
        else {
            info = String.format("Found a path from node #%d to node #%d",
                    getInputData().getOrigin().getId(), getInputData().getDestination().getId());
            if (getInputData().getMode() == AbstractInputData.Mode.LENGTH) {
                info = String.format("%s, %.4f kilometers", info, (getPath().getLength() / 1000.0));
            }
            else {
                info = String.format("%s, %.4f minutes", info,
                        (getPath().getMinimumTravelTime() / 60.0));
            }
        }
        info += " in " + getSolvingTime().getSeconds() + " seconds.";
        return info;
    }

}
