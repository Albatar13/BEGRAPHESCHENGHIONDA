package org.insa.drawing ;

/**
 *   Classe abstraite pour dessiner a l'ecran.
 *   Deux implementations : une sous-classe DessinVisible qui dessine vraiment a l'ecran
 *   et une sous-classe DessinInvisible qui ne dessine rien (pour ne pas ralentir les tests avec l'affichage).
 */

import java.awt.* ;

public interface Drawing {
	
	/**
	 * Enable auto-repaint mode - When this mode is enable, call to 
	 * drawing function will automatically repaint the drawing, which
	 * may be very slow in some case.
	 * 
	 * @param autoRepaint Use true to enable auto-repaint, false to disable.
	 * 
	 */
	public void setAutoRepaint(boolean autoRepaint);
	
	/**
	 * Repaint the drawing.
	 * 
	 */
	public void repaint();
	
    /**
     * Set the pencil width.
     * 
     * @param width Width for the pencil.
     * 
     */
    public void setWidth(int width);
    
    /**
     * Set the pencil color.
     * 
     * param color Color for the pencil.
     * 
     */
    public void setColor(Color col);

    /**
     *  Indique les bornes de la fenetre graphique.
     *  Le calcul des coordonnees en pixel se fera automatiquement
     *  a l'appel des methodes drawLine et autres.
     *
     *  @param long1 longitude du bord gauche
     *  @param long2 longitude du bord droit
     *  @param lat1 latitude du bord bas
     *  @param lat2 latitude du bord haut
     *  
     */
    public void setBB(double long1, double long2, double lat1, double lat2);

    /**
     *  Trace un segment.
     *  @param long1 longitude du premier point
     *  @param lat1 latitude du premier point
     *  @param long2 longitude du second point
     *  @param lat2 latitude du second point
     */
    public void drawLine(float long1, float lat1, float long2, float lat2);

    /**
     *  Trace un point.
     *  @param lon longitude du point
     *  @param lat latitude du point
     *  @param width grosseur du point
     */
    public void drawPoint(float lon, float lat, int width);

    /**
     *  Ecrit du texte a la position indiquee.
     *  @param lon longitude du point ou positionner le texte.
     *  @param lat latitude du point ou positionner le texte.
     *  @param txt le texte a ecrire.
     */
    public void putText(float lon, float lat, String txt);

}
