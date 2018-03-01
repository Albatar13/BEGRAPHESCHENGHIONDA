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
        RESIDENTIAL,
        UNCLASSIFIED,
        ROAD,
        LIVING_STREET,
        SERVICE,
        ROUNDABOUT,
        COASTLINE
    }

    /**
     * Access mode.
     */
    public enum AccessMode {
        FOOT, BICYCLE, SMALL_MOTORCYCLE, MOTORCYCLE, MOTORCAR, BUS
    }

    // Some masks...
    @SuppressWarnings("unused")
    private static final int MASK_UNKNOWN = 0x01;
    private static final int MASK_PRIVATE = 0x02;
    @SuppressWarnings("unused")
    private static final int MASK_AGRICULTURAL = 0x04;
    @SuppressWarnings("unused")
    private static final int MASK_SERVICE = 0x08;
    private static final int MASK_PUBLIC_TRANSPORT = 0x10;

    private static final int MASK_FOOT = 0x01 << 8;
    private static final int MASK_BICYCLE = 0x02 << 8;
    private static final int MASK_MOTORCYCLE = 0x0C << 8;
    private static final int MASK_SMALL_MOTORCYCLE = 0x08 << 8;
    private static final int MASK_MOTORCAR = 0x10 << 8;
    private static final int MASK_BUS = 0x20 << 8;

    // Type of the road (see above).
    private final RoadType type;

    // Access information
    private final int access;

    // One way road?
    private final boolean oneway;

    // Max speed in kilometers per hour.
    private final int maxSpeed;

    // Name of the road.
    private final String name;

    public RoadInformation(RoadType roadType, int access, boolean isOneWay, int maxSpeed,
            String name) {
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
    public boolean isPrivate() {
        return (this.access & MASK_PRIVATE) != 0;
    }

    /**
     * @return true if this road is reserved for public transport.
     */
    public boolean isPublicTransport() {
        return (this.access & MASK_PUBLIC_TRANSPORT) != 0;
    }

    /**
     * @param mode
     * 
     * @return true if this road is allowed for the specified mode.
     */
    public boolean isAllowedFor(AccessMode mode) {
        if ((this.access & MASK_UNKNOWN) != 0) {
            return true;
        }
        int maskedAccess = 0;
        switch (mode) {
        case FOOT:
            maskedAccess = access & MASK_FOOT;
            break;
        case BICYCLE:
            maskedAccess = access & MASK_BICYCLE;
            break;
        case SMALL_MOTORCYCLE:
            maskedAccess = access & MASK_SMALL_MOTORCYCLE;
            break;
        case MOTORCYCLE:
            maskedAccess = access & MASK_MOTORCYCLE;
            break;
        case MOTORCAR:
            maskedAccess = access & MASK_MOTORCAR;
            break;
        case BUS:
            maskedAccess = access & MASK_BUS;
            break;
        }
        return maskedAccess != 0;
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
        return typeAsString + " : " + getName() + " " + (isOneWay() ? " (oneway) " : "") + maxSpeed
                + " km/h (max.)";
    }

}
