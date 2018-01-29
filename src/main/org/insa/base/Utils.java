package org.insa.base ;

import java.io.* ;

import org.insa.drawing.Drawing;

/**
 *   Fonctions accessoires dont vous n'avez pas a vous servir directement.
 */
public class Utils {


	// Calibrer la sortie graphique en fonction de la carte
	// Vous pouvez modifier les coordonnees pour ameliorer le rendu.
	public static void calibrer(String nomCarte, Drawing dessin) {

		if (nomCarte.startsWith("insa")) {
			// L'INSA
			dessin.setBB (1.462, 1.473, 43.567, 43.5744) ;
		}
		else if (nomCarte.startsWith("paris")) {
			// Ile de la Cité, Paris
			dessin.setBB (2.329, 2.372, 48.839, 48.867) ;
		}
		else if (nomCarte.startsWith("mayot")) {
			// Mayotte
			dessin.setBB (44.5, 45.5, -13.25, -12.25) ;
		}
		else if (nomCarte.startsWith("reuni")) {
			// La Réunion
			dessin.setBB (55.0, 56.0, -21.5, -20.5) ;
		}
		else if (nomCarte.startsWith("midip")) {
			dessin.setBB (-0.6, 3.8, 42.2, 45.3) ;
		}
		else if (nomCarte.startsWith("franc")) {
			dessin.setBB (-5.2, 10.0, 41.0, 51.5) ;
		}
		else if (nomCarte.startsWith("pfranc")) {
			dessin.setBB (-5.2, 10.0, 41.0, 51.5) ;
		}
		else if (nomCarte.startsWith("morbihan")) {
			dessin.setBB (-3.53, -2.452, 47.27, 47.665) ;
		}
		else if (nomCarte.startsWith("newzealand")) {
			dessin.setBB (153.415, 179.912, -47.931, -33.980) ;
		}
		else if (nomCarte.startsWith("fract") || nomCarte.startsWith("carr")) {
			dessin.setBB (-0.05, 1.05, -0.05, 1.05) ;
		}
		else {
			dessin.setBB (-20.0, 50.0, 20.0, 70.0) ;
		}
	}


}
