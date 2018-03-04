package org.insa.graph;

/**
 * Class containing information for road that may be shared by multiple arcs.
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
        TRACK,
        RESIDENTIAL,
        UNCLASSIFIED,
        LIVING_STREET,
        SERVICE,
        ROUNDABOUT,
        PEDESTRIAN,
        BICYCLE,
        COASTLINE
    }

    // Type of the road (see above).
    private final RoadType type;

    // Access information
    private final AccessRestrictions access;

    // One way road?
    private final boolean oneway;

    // Max speed in kilometers per hour.
    private final int maxSpeed;

    // Name of the road.
    private final String name;

    public RoadInformation(RoadType roadType, AccessRestrictions access, boolean isOneWay, int maxSpeed, String name) {
        this.type = roadType;
        this.access = access;
        this.oneway = isOneWay;
        this.maxSpeed = maxSpeed;
        this.name = name;
    }

    // Access information

    /**
     * @return true if this is a private road.
     */
    public AccessRestrictions getAccessRestrictions() {
        return this.access;
    }

    /**
     * @return Type of the road.
     */
    public RoadType getType() {
        return type;
    }

    /**
     * @return true if this is a one-way road.
     */
    public boolean isOneWay() {
        return oneway;
    }

    /**
     * @return Maximum speed for this road (in kmph).
     */
    public int getMaximumSpeed() {
        return maxSpeed;
    }

    /**
     * @return Name of the road.
     */
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String typeAsString = "road";
        if (getType() == RoadType.COASTLINE) {
            typeAsString = "coast";
        }
        if (getType() == RoadType.MOTORWAY) {
            typeAsString = "highway";
        }
        return typeAsString + " : " + getName() + " " + (isOneWay() ? " (oneway) " : "") + maxSpeed + " km/h (max.)";
    }

}
