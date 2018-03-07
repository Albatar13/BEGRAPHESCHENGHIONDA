package org.insa.graph;

import java.util.EnumMap;
import java.util.EnumSet;

/**
 * Class containing access restrictions for roads/arcs.
 * 
 * This class maps transport modes to their restriction and provide interface
 * based on EnumSet to query restrictions.
 * 
 * To each transport is associated at most one restriction per road (no
 * restriction corresponds to {@link AccessRestriction#UNKNOWN} but a road can
 * have different restrictions for different modes.
 *
 */
public class AccessRestrictions {

    /**
     * List of managed transport modes.
     *
     */
    public enum AccessMode {

        // Specific modes
        FOOT,
        BICYCLE,
        SMALL_MOTORCYCLE,
        AGRICULTURAL,
        MOTORCYCLE,
        MOTORCAR,
        HEAVY_GOODS,
        PUBLIC_TRANSPORT;

        /**
         * EnumSet containing all the possible transport modes.
         */
        public static final EnumSet<AccessMode> ALL = EnumSet.allOf(AccessMode.class);

        /**
         * EnumSet containing all the vehicle transport modes.
         */
        public static final EnumSet<AccessMode> VEHICLE = EnumSet.range(AccessMode.BICYCLE,
                AccessMode.PUBLIC_TRANSPORT);

        /**
         * EnumSet containing all the motorized vehicle transport modes.
         */
        public static final EnumSet<AccessMode> MOTOR_VEHICLE = EnumSet
                .range(AccessMode.SMALL_MOTORCYCLE, AccessMode.PUBLIC_TRANSPORT);
    }

    /**
     * Possible restrictions for the roads/arcs.
     *
     */
    public enum AccessRestriction {
        ALLOWED, FORBIDDEN, PRIVATE, DESTINATION, DELIVERY, CUSTOMERS, FORESTRY, UNKNOWN;

        // Not private or forbidden
        public static final EnumSet<AccessRestriction> ALLOWED_FOR_SOMETHING = EnumSet.of(
                AccessRestriction.ALLOWED, AccessRestriction.DESTINATION,
                AccessRestriction.DESTINATION, AccessRestriction.DELIVERY,
                AccessRestriction.CUSTOMERS, AccessRestriction.FORESTRY);

    }

    // Map mode -> restriction
    private final EnumMap<AccessMode, AccessRestriction> restrictions;

    /**
     * Create new AccessRestrictions instances with unknown restrictions.
     */
    public AccessRestrictions() {
        this.restrictions = new EnumMap<>(AccessMode.class);
        for (AccessMode mode: AccessMode.values()) {
            this.restrictions.put(mode, AccessRestriction.UNKNOWN);
        }
    }

    /**
     * Create a new AccessRestrictions instances with the given restrictions.
     * 
     * @param restrictions Map of restrictions for this instance of
     * AccessRestrictions.
     */
    public AccessRestrictions(EnumMap<AccessMode, AccessRestriction> restrictions) {
        this.restrictions = restrictions;
    }

    /**
     * Retrieve the restriction corresponding to the given mode.
     * 
     * @param mode Mode for which the restriction should be retrieved.
     * 
     * @return Restriction for the given mode.
     */
    public AccessRestriction getRestrictionFor(AccessMode mode) {
        return restrictions.getOrDefault(mode, AccessRestriction.UNKNOWN);
    }

    /**
     * Check if the restriction associated with the given mode is one of the given
     * restrictions.
     * 
     * @param mode Mode for which to check the restrictions.
     * @param restrictions List of queried restrictions for the mode.
     * 
     * @return true if the restriction of the given mode is one of the given
     * restrictions.
     */
    public boolean isAllowedForAny(AccessMode mode, EnumSet<AccessRestriction> restrictions) {
        return restrictions.contains(getRestrictionFor(mode));
    }

    /**
     * Check if the restriction for the given mode corresponds to the given
     * restriction.
     * 
     * @param mode Mode for which the restriction should be checked.
     * @param restriction Restriction to check against.
     * 
     * @return true if the restriction of the given mode corresponds to the given
     * restriction.
     */
    public boolean isAllowedFor(AccessMode mode, AccessRestriction restriction) {
        return getRestrictionFor(mode).equals(restriction);
    }

    /**
     * Check if the restriction associated to each given mode is one of the
     * restrictions. The restriction may not be the same for all modes.
     * 
     * @param modes Modes for which restrictions should be checked.
     * @param restrictions Set of wanted restrictions for the modes.
     * 
     * @return true if all the given modes are allowed for any of the given
     * restrictions.
     */
    public boolean areAllAllowedForAny(EnumSet<AccessMode> modes,
            EnumSet<AccessRestriction> restrictions) {
        boolean allowed = true;
        for (AccessMode mode: modes) {
            allowed = allowed && isAllowedForAny(mode, restrictions);
        }
        return allowed;
    }
}
