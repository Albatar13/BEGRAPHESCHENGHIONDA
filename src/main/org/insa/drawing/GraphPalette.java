package org.insa.drawing;

import java.awt.Color;

import org.insa.graph.RoadInformation.RoadType;

public interface GraphPalette {
	
	/**
	 * @return The default point width for this palette.
	 */
	public int getDefaultPointWidth();
	
	/**
	 * @return The default point color for this palette.
	 */
	public Color getDefaultPointColor();
	
	/**
	 * @param type Type of the road.
	 * 
	 * @return Color associated to the given type of road.
	 */
	public Color getColorForType(RoadType type);

	/**
	 * @param type Type of the road.
	 * 
	 * @return Width associated to the given type of road.
	 */
	public int getWidthForType(RoadType type);

}
