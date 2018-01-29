package org.insa.base ;

import java.io.* ;

/* Ne lisez pas cette classe. Lancez javadoc et lisez la doc generee plutot. */

/**
 *  La classe Readarg facilite la lecture de donnees depuis le clavier ou depuis la ligne de commande.
 *
 */
public class Readarg {

    private final String[] args ;
    private int next ;

    // Le Java est le langage prefere des Shadoks.
    private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public Readarg(String[] argz) {
	this.args = argz ;
	this.next = 0 ;
    }

    /** 
     * Obtient une chaine, ou bien depuis la ligne de commande, ou depuis l'entree standard.
     * @param msg  Message affiche avant de demander la chaine
     */
    public String lireString (String msg) {
	
	String resultat = "" ;

	System.out.print(msg) ;
	
	if (this.next >= this.args.length) {
	    try {
		resultat = br.readLine () ;
	    } catch (Exception e) {
		System.err.println ("Erreur de lecture de l'entree standard.") ;
		System.exit(1) ;
	    }
	}
	else {
	    resultat = this.args[this.next] ;
	    this.next++ ;
	    System.out.println (resultat) ;
	}

	return resultat ;
    }


    /** 
     * Obtient un entier, ou bien depuis la ligne de commande, ou depuis l'entree standard.
     * @param msg  Message affiche avant de demander l'entier
     */
    public int lireInt (String msg) {
	String lu = lireString (msg) ;
	int result = 0 ;
	try {
	    result = Integer.parseInt(lu) ;
	}
	catch (Exception e) {
	    System.err.println ("Un entier est attendu mais je lis " + lu) ;
	    System.exit(1) ;
	}
	return result ;
    }

    /** 
     * Obtient un float, ou bien depuis la ligne de commande, ou depuis l'entree standard.
     * @param msg  Message affiche avant de demander le float.
     */
    public float lireFloat (String msg) {
	String lu = lireString (msg) ;
	float result = 0 ;
	try {
	    result = Float.parseFloat(lu) ;
	}
	catch (Exception e) {
	    System.err.println ("Un reel est attendu mais je lis " + lu) ;
	    System.exit(1) ;
	}
	
	return result ;
    }
}
