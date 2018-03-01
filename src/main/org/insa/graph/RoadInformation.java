package org.insa.graph;

import java.util.Map;

/**
 * Class containing information for road that may be shared by multiple arcs.
 * 
 */
public class RoadInformation {

    /**
     * Access mode.
     */
    public enum AccessMode {
        FOOT, BICYCLE, SMALL_MOTORCYCLE, MOTORCYCLE, MOTORCAR, BUS
    }

    /**
     * Class containing access restriction information.
     *
     */
    public static class AccessRestriction {

        // Private road
        private boolean is_private = false;

        // Public transport restricted.
        private boolean is_publicTransport = false;

        // Map Enum -> Allowed.
        private Map<AccessMode, Boolean> allowedModes;

        /**
         * Construct a new AccessInformation with unknown information: Not private, not
         * public transport and all modes are allowed.
         */
        public AccessRestriction() {

        }

        /**
         * @param isPrivate
         * @param isPublicTransport
         * @param allowedModes
         */
        public AccessRestriction(boolean isPrivate, boolean isPublicTransport,
                Map<AccessMode, Boolean> allowedModes) {
            this.is_private = isPrivate;
            this.is_publicTransport = isPublicTransport;
            this.allowedModes = allowedModes;
        }

        /**
         * @return true if this is a private road.
         */
        public boolean isPrivate() {
            return is_private;
        }

        /**
         * @return true if this road is reserved for public transport.
         */
        public boolean isPublicTransport() {
            return is_publicTransport;
        }

        /**
         * @param mode
         * 
         * @return true if this road is allowed for the specified mode.
         */
        public boolean isAllowedFor(AccessMode mode) {
            if (this.allowedModes == null) {
                return true;
            }
            return this.allowedModes.getOrDefault(mode, false);
        }

    }

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
    private final RoadType type;

    // Access information
    private final AccessRestriction access;

    // One way road?
    private final boolean oneway;

    // Max speed in kilometers per hour.
    private final int maxSpeed;

    // Name of the road.
    private final String name;

    public RoadInformation(RoadType roadType, AccessRestriction access, boolean isOneWay,
            int maxSpeed, String name) {
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
    public AccessRestriction getAccessRestrictions() {
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
