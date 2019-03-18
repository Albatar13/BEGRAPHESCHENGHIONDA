package org.insa.graph;

import java.util.EnumMap;
import java.util.EnumSet;

/**
 * <p>
 * Class containing access restrictions for roads/arcs.
 * </p>
 * 
 * <p>
 * This class maps transport modes to their restriction and provide interface
 * based on EnumSet to query restrictions.
 * </p>
 * 
 * <p>
 * To each transport is associated at most one restriction per road (no
 * restriction corresponds to {@link AccessRestriction#UNKNOWN} but a road can
 * have different restrictions for different modes.
 * </p>
 *
 */
public class AccessRestrictions {

    /**
     * Enumeration representing the available transport modes.
     *
     * @see <a href=
     *      "https://wiki.openstreetmap.org/wiki/Key:access#Transport_mode_restrictions">OpenStreetMap
     *      reference for access modes.</a>
     */
    public enum AccessMode {

        /**
         * Access mode corresponding to pedestrians.
         */
        FOOT,

        /**
         * Access mode corresponding to bicycles (non-motorized).
         */
        BICYCLE,

        /**
         * Access mode corresponding to small motorcycles (limited speed).
         */
        SMALL_MOTORCYCLE,

        /**
         * Access mode corresponding to agricultural vehicles.
         */
        AGRICULTURAL,

        /**
         * Access mode corresponding to motorcycles.
         */
        MOTORCYCLE,

        /**
         * Access mode corresponding to motorcars.
         */
        MOTORCAR,

        /**
         * Access mode corresponding to heavy transportation vehicles.
         */
        HEAVY_GOODS,

        /**
         * Access mode corresponding to public transport vehicles.
         */
        PUBLIC_TRANSPORT;

        /**
         * {@code EnumSet} containing all possible transport modes.
         * 
         * 
         */
        public static final EnumSet<AccessMode> ALL = EnumSet.allOf(AccessMode.class);

        /**
         * {@code EnumSet} containing all vehicle transport modes.
         * 
         */
        public static final EnumSet<AccessMode> VEHICLE = EnumSet.range(AccessMode.BICYCLE,
                AccessMode.PUBLIC_TRANSPORT);

        /**
         * {@code EnumSet} containing all motorized vehicle transport modes.
         * 
         */
        public static final EnumSet<AccessMode> MOTOR_VEHICLE = EnumSet
                .range(AccessMode.SMALL_MOTORCYCLE, AccessMode.PUBLIC_TRANSPORT);
    }

    /**
     * Possible restrictions for the roads/arcs.
     *
     * @see <a href=
     *      "https://wiki.openstreetmap.org/wiki/Key:access#Transport_mode_restrictions">OpenStreetMap
     *      reference for access restrictions.</a>
     */
    public enum AccessRestriction {

        /**
         * 
         */
        ALLOWED,

        /**
         * 
         */
        FORBIDDEN,

        /**
         * 
         */
        PRIVATE,

        /**
         * 
         */
        DESTINATION,

        /**
         * 
         */
        DELIVERY,

        /**
         * 
         */
        CUSTOMERS,

        /**
         * 
         */
        FORESTRY,

        /**
         * 
         */
        UNKNOWN;

        /**
         * {@code EnumSet} corresponding to restrictions that are not totally private.
         * 
         */
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
     *        AccessRestrictions.
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
     * @return {@code true} if the restriction of the given mode is one of the given
     *         restrictions.
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
     * @return {@code true} if the restriction of the given mode corresponds to the
     *         given restriction.
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
     * @return {@code true} if all the given modes are allowed for any of the given
     *         restrictions.
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
