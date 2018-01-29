package org.insa.graph ;

/**
 * Class containing information for road that may be shared
 * by multiple arcs.
 * 
 */
public class RoadInformation {
	
	/**
	 * Road type.
	 */
	public enum RoadType {
		MOTORWAY,
		TRUNK,
		PRIMARY,
		SECONDARY,
		MOTORWAY_LINK,
		TRUNK_LINK,
		PRIMARY_LINK,
		SECONDARY_LINK,
		TERTIARY,
		RESIDENTIAL,
		UNCLASSIFIED,
		ROAD,
		LIVING_STREET,
		SERVICE,
		ROUNDABOUT,
		COASTLINE
	}

	// Type of the road (see above).
	private RoadType type;

	// One way road?
	private boolean oneway;

	// Max speed in kilometers per hour.
	private int maxSpeed;

	// Name of the road.
	private String name;

	public RoadInformation(RoadType roadType, boolean isOneWay, int maxSpeed, String name) {
		this.type = roadType;
		this.oneway = isOneWay;
		this.maxSpeed = maxSpeed;
		this.name = name;
	}
	
	/**
	 * @return Type of the road.
	 */
	public RoadType getType() { return type; }
	
	/**
	 * @return true if this is a one-way road.
	 */
	public boolean isOneWay() { return oneway; }
	
	/**
	 * @return Maximum speed for this road (in km/h).
	 */
	public int getMaximumSpeed() { return maxSpeed; }
	
	/**
	 * @return Name of the road.
	 */
	public String getName() { return name; }

	@Override
	public String toString() {
		String typeAsString = "road";
		if (getType() == RoadType.COASTLINE) {
			typeAsString = "coast";
		}
		if (getType() == RoadType.MOTORWAY) {
			typeAsString = "highway";
		}
		return typeAsString + " : " + getName() 
				+ " " + (isOneWay() ? " (oneway) " : "") 
				+ maxSpeed + " km/h (max.)";
	}

}
