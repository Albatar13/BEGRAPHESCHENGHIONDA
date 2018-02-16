package org.insa.graph.io ;

import java.io.* ;
import java.util.zip.* ;

/**
 *  Class that can be used to open (compressed) files from a specified
 *  set of folders or for a full path.
 *  
 */
public class Openfile {

	/**
	 * These folders will be looked up for the files.
	 * 
	 */
	private static final String[] datadirs = {
			
		// INSA folder containing maps.
		"/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps",

		// INSA folder containing paths.
		"/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/",

		// Maps sub-folder.
		"Maps", 
		
		// Current folder.
		"."
	};

	/**
	 * Available extensions.
	 * 
	 */
	private static final String[] extensions = { ".map", ".gz", ".map.gz", ".path", ".path.gz", "" };

	/** 
	 * Open the given file and return a corresponding DataInputStream.
	 * 
	 * @param filename Name of the file to open (without extension) or full path to the given file.
	 * @throws IOException 
	 */
	public static DataInputStream open(String filename) throws IOException {

		File file = null;
		String fullpath = null;
		
		// If the filename containing only a name (not a path):
		if (filename.equals (new File(filename).getName())) {

	
			for (String ext: extensions) {
				String fname = filename + ext;
				for (int index = 0; file == null && index < datadirs.length; ++index) {
					fullpath = datadirs[index] + File.separator + fname;
					file = new File(fullpath);
					if (!file.exists()) {
						file = null;
					}
				}
			}
			
		}
		else {
			fullpath = filename;
			file = new File(filename);
		}
			
		InputStream fileInput = new FileInputStream(new File(fullpath));

		// If the file is compressed.
		if (fullpath.endsWith(".gz")) {
			fileInput = new GZIPInputStream(fileInput) ;
		}
		else {
			fileInput = new BufferedInputStream(fileInput) ;
		}

		return new DataInputStream(fileInput) ;
	}

}
