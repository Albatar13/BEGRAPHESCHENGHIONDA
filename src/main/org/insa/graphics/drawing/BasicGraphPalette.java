package org.insa.graphics.drawing;

import java.awt.Color;

import org.insa.graph.Arc;
import org.insa.graph.RoadInformation.RoadType;

public class BasicGraphPalette implements GraphPalette {

    // Color types for arc.
    private static final Color MOTORWAY_COLOR = Color.RED;
    private static final Color BIG_ROAD_COLOR = new Color(255, 105, 0);
    private static final Color SMALL_ROAD_COLOR = new Color(255, 200, 0);
    private static final Color COASTLINE_COLOR = Color.BLUE;

    @Override
    public Color getColorForArc(Arc arc) {
        RoadType type = arc.getRoadInformation().getType();
        switch (type) {
        case MOTORWAY:
            return MOTORWAY_COLOR;
        case TRUNK:
        case PRIMARY:
        case SECONDARY:
        case MOTORWAY_LINK:
        case TRUNK_LINK:
        case PRIMARY_LINK:
            return BIG_ROAD_COLOR;
        case SECONDARY_LINK:
        case TERTIARY:
        case RESIDENTIAL:
        case UNCLASSIFIED:
        case LIVING_STREET:
        case SERVICE:
        case ROUNDABOUT:
        case PEDESTRIAN:
        case CYCLEWAY:
        case TRACK:
            return SMALL_ROAD_COLOR;
        case COASTLINE:
            return COASTLINE_COLOR;
        }

        return Color.BLACK;
    }

    @Override
    public int getWidthForArc(Arc arc) {
        RoadType type = arc.getRoadInformation().getType();
        int width = 1;
        switch (type) {
        case MOTORWAY:
            width = 2;
            break;
        case TRUNK:
        case PRIMARY:
        case SECONDARY:
        case MOTORWAY_LINK:
        case TRUNK_LINK:
        case PRIMARY_LINK:
            width = 1;
            break;
        case SECONDARY_LINK:
        case TERTIARY:
        case RESIDENTIAL:
        case UNCLASSIFIED:
        case LIVING_STREET:
        case SERVICE:
        case ROUNDABOUT:
        case PEDESTRIAN:
        case CYCLEWAY:
        case TRACK:
            width = 1;
            break;
        case COASTLINE:
            width = 4;
            break;
        }
        return width;
    }

}
