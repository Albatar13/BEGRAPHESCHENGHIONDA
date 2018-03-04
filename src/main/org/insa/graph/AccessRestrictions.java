package org.insa.graph;

import java.util.EnumMap;
import java.util.EnumSet;

public class AccessRestrictions {

    public enum AccessMode {

        // Specific modes
        FOOT, BICYCLE, SMALL_MOTORCYCLE, AGRICULTURAL, MOTORCYCLE, MOTORCAR, HEAVY_GOODS, PUBLIC_TRANSPORT;

        // All available modes
        public static final EnumSet<AccessMode> ALL = EnumSet.allOf(AccessMode.class);

        // Vehicle
        public static final EnumSet<AccessMode> VEHICLE = EnumSet.range(AccessMode.BICYCLE,
                AccessMode.PUBLIC_TRANSPORT);

        // Motor vehicle
        public static final EnumSet<AccessMode> MOTOR_VEHICLE = EnumSet.range(AccessMode.SMALL_MOTORCYCLE,
                AccessMode.PUBLIC_TRANSPORT);
    }

    public enum AccessRestriction {
        ALLOWED, FORBIDDEN, PRIVATE, DESTINATION, FORESTRY, UNKNOWN
    }

    // Map mode -> restriction
    private final EnumMap<AccessMode, AccessRestriction> restrictions;

    /**
     * Create new access restrictions with unknown restrictions.
     */
    public AccessRestrictions() {
        this.restrictions = new EnumMap<>(AccessMode.class);
        for (AccessMode mode: AccessMode.values()) {
            this.restrictions.put(mode, AccessRestriction.UNKNOWN);
        }
    }

    /**
     * Create a new instance of access restrictions with the given restrictions.
     * 
     * @param restrictions
     */
    public AccessRestrictions(EnumMap<AccessMode, AccessRestriction> restrictions) {
        this.restrictions = restrictions;
    }

}
