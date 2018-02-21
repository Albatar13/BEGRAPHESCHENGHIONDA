package org.insa.drawing;

import java.awt.Color;

import org.insa.graph.RoadInformation.RoadType;

public class BasicGraphPalette implements GraphPalette {
	
	// Color types for arc.
    static final Color motorway = Color.RED;
    static final Color bigroad = new Color(255, 105, 0);
    static final Color smallroad = new Color(255, 234, 0);
    static final Color coastline = Color.BLUE;
    
    // Default point width
    static final int DEFAULT_POINT_WIDTH = 1;
    
    /**
     * 
     */
    public BasicGraphPalette() { }
    
	@Override
	public int getDefaultPointWidth() {
		return 2;
	}

	@Override
	public Color getDefaultPointColor() {
		return Color.GREEN;
	}

	@Override
	public Color getColorForType(RoadType type) {
		Color color = Color.BLACK;
		switch (type) {
			case MOTORWAY: 
				color = motorway;
				break;
			case TRUNK:
			case PRIMARY:
			case SECONDARY:
			case MOTORWAY_LINK:
			case TRUNK_LINK:
			case PRIMARY_LINK:
				color = bigroad;
				break;
			case SECONDARY_LINK:
			case TERTIARY:
			case RESIDENTIAL:
			case UNCLASSIFIED:
			case ROAD:
			case LIVING_STREET:
			case SERVICE:
			case ROUNDABOUT:
				color = smallroad;
				break;
			case COASTLINE:
				color = coastline;
				break;
		}
		return color;
	}

	@Override
	public int getWidthForType(RoadType type) {
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
			case ROAD:
			case LIVING_STREET:
			case SERVICE:
			case ROUNDABOUT:
				width = 1;
				break;
			case COASTLINE:
				width = 4;
				break;
		}
		return width;
	}
	
}
