package org.insa.drawing;

import java.awt.Color;

/**
 *   Cette implementation de la classe Dessin ne produit pas d'affichage,
 *   ce qui accelere l'execution (utile pour ne pas ralentir les tests).
 */

public class DrawingInvisible implements Drawing {
    
    public DrawingInvisible () { }

	@Override
	public void setWidth(int width) { }

	@Override
	public void setColor(Color col) { }

	@Override
	public void setBB(double long1, double long2, double lat1, double lat2) { }

	@Override
	public void drawLine(float long1, float lat1, float long2, float lat2) { }

	@Override
	public void drawPoint(float lon, float lat, int width) { }

	@Override
	public void putText(float lon, float lat, String txt) { }

	@Override
	public void setAutoRepaint(boolean autoRepaint) {	 }

	@Override
	public void repaint() { }
    
}
