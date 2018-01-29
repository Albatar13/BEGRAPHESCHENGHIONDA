package org.insa.base ;

import java.io.* ;
import java.util.zip.* ;

/* Ne lisez pas cette classe. Lancez javadoc et lisez la doc generee plutot. */

/**
 *  La classe Openfile permet de lire les fichiers contenant les cartes :
 *   <ul>
 *    <li> en trouvant le bon dossier parmi les dossiers pre-configures </li>
 *    <li> en dezippant automatiquement si besoin </li>
 *   </ul>
 *
 */
public class Openfile {

    // Le programme examine chaque dossier dans l'ordre jusqu'a trouver celui qui contient la carte voulue
    private static final String[] datadirs = 
    {  	// NE MODIFIEZ PAS CELUI-CI
	// car il permet de tester en etant a l'INSA.
	"/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps",

	// Celui-ci pour les chemins
	"/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/",

        // On cherche aussi dans le sous-repertoire local "Maps" (s'il existe)
	"Maps", 

	// et dans le repertoire courant (Unix uniquement)
	".",

	// Si vous utilisez votre propre dossier pour les donnees, mettez-le ici.
	"/home/votrepropredossier/a/vous",
    } ;

    // Extension testees. Garder l'extension vide dans la liste.
    private static final String[] extensions = { ".map", ".gz", ".map.gz", ".path", ".path.gz", "" } ;

    /** 
     * Ouvre le fichier indiqué et renvoie un DataInputStream sur ce fichier.
     * Le fichier ne sera pas ferme avant la fin de l'application.
     * @param filename  Nom du fichier a ouvrir (sans chemin)
     */
    public static DataInputStream open (String filename) {

	if (!filename.equals (new File(filename).getName())) {
	    System.out.println("Le nom du fichier ne doit pas contenir un chemin (ni absolu, ni relatif).") ;
	    System.out.println("Il doit juste contenir le nom du fichier contenant la carte.") ;
	    System.out.println("Si vous voulez utiliser un dossier specifique, configurez base/Openfile.java") ;
	    System.exit(1) ;
	}

	boolean trouve = false ;
	InputStream fileinput = null ;
	String fname = null ;
	String fullpath = null ;

	for (int extn = 0 ; !trouve && extn < extensions.length ; extn++) {
	    fname = filename + extensions[extn] ;
	    for (int index = 0 ; !trouve && index < datadirs.length ; index++) {
		fullpath = datadirs[index] + File.separator + fname ;
		File file = new File(fullpath) ;
		if (file.canRead()) {
		    trouve = true ;
		    try {
			fileinput = new FileInputStream(file) ;
		    } catch (IOException e) {
			e.printStackTrace() ;
			System.exit(1) ;
		    }
		}
	    }
	}

	if (!trouve) {
	    // Pas trouve
	    System.out.println("Impossible de trouver le fichier " + filename) ;
	    System.out.println("  pourtant j'ai cherche dans les dossiers : ") ;
	    int existepas = 0 ;
	    for (int i = 0 ; i < datadirs.length ; i++) {
		System.out.println("     - " + datadirs[i]) ;
		if (!new File(datadirs[i]).isDirectory()) {
		    switch (existepas) {
		    case 0:  System.out.println("       (Ce dossier n'existe pas d'ailleurs)") ; break;
		    case 1:  System.out.println("       (Ce dossier n'existe pas non plus)") ; break;
		    default: System.out.println("       (Celui-la non plus)") ; break;
		    }
		    existepas++ ;
		}
		System.out.println() ;
	    }
	    System.exit(1) ;
	}

	System.out.println("Fichier utilisee : " + fullpath) ;
	System.out.println() ;

	if (fname.endsWith(".gz")) {
	    // The file is gzipped.
	    try {
		fileinput = new GZIPInputStream(fileinput) ;
	    } catch (IOException e) {
		e.printStackTrace() ;
		System.exit(1) ;		
	    }
	}
	else {
	    fileinput = new BufferedInputStream(fileinput) ;
	}

	return new DataInputStream(fileinput) ;
    }

}
