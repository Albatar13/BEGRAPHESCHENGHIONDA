package org.insa.graph;

import java.util.ArrayList;

public class Arc {
	
	// Destination node.
	private Node dest;

	// Length of the road (in meters).
	private int length;
	
	// Road information.
	RoadInformation info;
	
	// Segments.
	ArrayList<Point> points;
	
	/**
	 * @param dest
	 * @param length
	 * @param roadInformation
	 * @param points
	 */
	public Arc(Node dest, int length, RoadInformation roadInformation) {
		this.dest = dest;
		this.length = length;
		this.info = roadInformation;
		this.points = new ArrayList<Point>();
	}

	/**
	 * @param dest
	 * @param length
	 * @param roadInformation
	 * @param points
	 */
	public Arc(Node dest, int length, RoadInformation roadInformation, ArrayList<Point> points) {
		this.dest = dest;
		this.length = length;
		this.info = roadInformation;
		this.points = points;
	}
	
	/**
	 * @return Destination node of this arc.
	 */
	public Node getDest() {
		return dest;
	}

	/**
	 * @return Length of this arc, in meters.
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * @return Minimum time required to travel this arc, in seconds.
	 */
	public float getMinimumTravelTime() {
		return getLength() * 3600f / (info.getMaximumSpeed() * 1000f);
	}

	/**
	 * @return Road information for this arc.
	 */
	public RoadInformation getInfo() {
		return info;
	}

	/**
	 * @return Points representing segments of this arc. This function may return an empty
	 * ArrayList if the segments are stored in the reversed arc (for two-ways road).
	 */
	public ArrayList<Point> getPoints() {
		return points;
	}

}
