package org.insa.drawing.graph;

import java.awt.Color;

import org.insa.graph.RoadInformation.RoadType;

public class BlackAndWhiteGraphPalette extends BasicGraphPalette {
	
	// Road colors (index
	private final static Color[] ROAD_COLOR_FROM_WIDTH = {
		null, new Color(140, 140, 140), new Color(80, 80, 80), new Color(40, 40, 40), new Color(30, 30, 30)
	};

	@Override
	public Color getDefaultPointColor() {
		return Color.BLACK;
	}

	@Override
	public Color getColorForType(RoadType type) {
		int width = getWidthForType(type);
		return ROAD_COLOR_FROM_WIDTH[width];
	}

}
