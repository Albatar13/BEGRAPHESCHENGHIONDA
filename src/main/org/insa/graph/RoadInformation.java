package org.insa.graph;

/**
 * <p>
 * Class containing information for road that may be shared by multiple arcs.
 * </p>
 * 
 * <p>
 * Sharing information between arcs reduces memory footprints of the program (a
 * long road is often split into multiple arcs at each intersection).
 * </p>
 * 
 */
public class RoadInformation {

    /**
     * Enumeration for road types.
     * 
     * @see <a href=
     *      "https://wiki.openstreetmap.org/wiki/Key:highway#Values">OpenStreetMap
     *      reference for road types.</a>
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
        CYCLEWAY,
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

    /**
     * Create a new RoadInformation instance containing the given parameters.
     * 
     * @param roadType Type of the road (see {@link RoadType}).
     * @param access Access restrictions for the road (see
     *        {@link AccessRestrictions}).
     * @param isOneWay true if this road is a one way road, false otherwise.
     * @param maxSpeed Maximum speed for the road (in kilometers-per-hour).
     * @param name Name of the road.
     */
    public RoadInformation(RoadType roadType, AccessRestrictions access, boolean isOneWay,
            int maxSpeed, String name) {
        this.type = roadType;
        this.access = access;
        this.oneway = isOneWay;
        this.maxSpeed = maxSpeed;
        this.name = name;
    }

    /**
     * @return Access restrictions for this road.
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
     * @return true if the road is a one-way road.
     */
    public boolean isOneWay() {
        return oneway;
    }

    /**
     * @return Maximum speed for this road (in kilometers-per-hour).
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

    @Override
    public String toString() {
        String typeAsString = "road";
        if (getType() == RoadType.COASTLINE) {
            typeAsString = "coast";
        }
        if (getType() == RoadType.MOTORWAY) {
            typeAsString = "highway";
        }
        return typeAsString + " : " + getName() + " " + (isOneWay() ? " (oneway) " : "") + maxSpeed
                + " km/h (max.)";
    }

}
