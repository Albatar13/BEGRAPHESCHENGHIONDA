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
        ALLOWED, FORBIDDEN, PRIVATE, DESTINATION, DELIVERY, CUSTOMERS, FORESTRY, UNKNOWN;

        // Not private or forbidden
        public static final EnumSet<AccessRestriction> ALLOWED_FOR_SOMETHING = EnumSet.of(AccessRestriction.ALLOWED,
                AccessRestriction.DESTINATION, AccessRestriction.DESTINATION, AccessRestriction.DELIVERY,
                AccessRestriction.CUSTOMERS, AccessRestriction.FORESTRY);

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

    public long value = 0;

    /**
     * Create a new instance of access restrictions with the given restrictions.
     * 
     * @param restrictions
     */
    public AccessRestrictions(EnumMap<AccessMode, AccessRestriction> restrictions, long value) {
        this.restrictions = restrictions;
        this.value = value;
    }

    /**
     * @param mode
     * 
     * @return Restriction for the given mode.
     */
    public AccessRestriction getRestrictionFor(AccessMode mode) {
        return restrictions.getOrDefault(mode, AccessRestriction.UNKNOWN);
    }

    /**
     * @param mode
     * @param restrictions
     * 
     * @return true if the given mode is allowed for any of the given restrictions.
     */
    public boolean isAllowedForAny(AccessMode mode, EnumSet<AccessRestriction> restrictions) {
        return restrictions.contains(getRestrictionFor(mode));
    }

    /**
     * @param mode
     * @param restriction
     * 
     * @return true if the given mode is allowed for the given restriction.
     */
    public boolean isAllowedFor(AccessMode mode, AccessRestriction restriction) {
        return getRestrictionFor(mode).equals(restriction);
    }

    /**
     * @param modes
     * @param restrictions
     * 
     * @return true if all the given modes are allowed for any of the given
     *         restrictions.
     */
    public boolean areAllAllowedForAny(EnumSet<AccessMode> modes, EnumSet<AccessRestriction> restrictions) {
        boolean allowed = true;
        for (AccessMode mode: modes) {
            allowed = allowed && isAllowedForAny(mode, restrictions);
        }
        return allowed;
    }
}
