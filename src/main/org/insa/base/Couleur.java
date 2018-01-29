package org.insa.base ;

/**
 *   Choix des couleurs pour l'affichage.
 */

import java.awt.* ;

import org.insa.drawing.Drawing;

public class Couleur {

    static final Color autoroute = Color.red ;
    static final Color bigroute = new Color(255, 105, 0) ;
    static final Color tiroute = new Color(255, 234, 0) ;
    static final Color cote = Color.blue ;

    public static void set(Drawing d, char type) {

	// Voir le fichier Descripteur.java pour le type des routes.
	switch (type) {
	case 'a':
	    d.setWidth(2) ;
	    d.setColor(Color.red) ;
	    break ;

	case 'b':
	case 'c':
	case 'd':
	case 'e':
	case 'f':
	case 'g':
	    d.setWidth(1) ;
	    d.setColor(bigroute) ;
	    break ;
	case 'h':
	case 'i':
	case 'j':
	case 'k':
	case 'l':
	case 'm':
	case 'n':
	case 'o':
	    d.setWidth(1) ;
	    d.setColor(tiroute) ;
	    break ;
	    
	case 'z':
	    d.setWidth(4) ;
	    d.setColor(cote) ;
	    break ;
	    
	default:
	    d.setWidth(1) ;
	    d.setColor(Color.black) ;
	}
    }
}
