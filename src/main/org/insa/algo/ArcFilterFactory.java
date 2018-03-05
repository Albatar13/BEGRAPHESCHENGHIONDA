package org.insa.algo;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.insa.algo.AbstractInputData.ArcFilter;
import org.insa.graph.AccessRestrictions.AccessMode;
import org.insa.graph.AccessRestrictions.AccessRestriction;
import org.insa.graph.Arc;

public class ArcFilterFactory {

    /**
     * @return List of all arc filters in this factory.
     */
    public static List<ArcFilter> getAllFilters() {
        List<ArcFilter> filters = new ArrayList<>();

        // Common filters:

        // 1. No filter (all arcs allowed):
        filters.add(new ArcFilter() {
            @Override
            public boolean isAllowed(Arc arc) {
                return true;
            }

            @Override
            public String toString() {
                return "All roads are allowed.";
            }
        });

        // 2. Only road allowed for cars:
        filters.add(new ArcFilter() {
            @Override
            public boolean isAllowed(Arc arc) {
                return arc.getRoadInformation().getAccessRestrictions()
                        .isAllowedForAny(AccessMode.MOTORCAR, EnumSet.complementOf(EnumSet
                                .of(AccessRestriction.FORBIDDEN, AccessRestriction.PRIVATE)));
            }

            @Override
            public String toString() {
                return "Only roads open for cars.";
            }
        });

        // 3. Add your own filters here (do not forget to implement toString() to get an
        // understandable output!):

        return filters;
    }

}
