package org.insa.drawing;

import java.awt.*;
import java.awt.image.*;

/**
 *   Cette implementation de la classe Dessin produit vraiment un affichage
 *   (au contraire de la classe DessinInvisible).
 */

public class DrawingVisible extends Canvas implements Drawing {

	/**
	 * 
	 */
	private static final long serialVersionUID = 96779785877771827L;

	private final Graphics2D gr;

	private float long1;
	private float long2;
	private float lat1;
	private float lat2;
	private final float width;
	private final float height;

	private boolean bb_is_set ;
	
	private Image image;
	private ZoomAndPanListener zoomAndPanListener;
	
	public boolean autoRepaint = true;

	/**
	 *  Cree et affiche une nouvelle fenetre de dessin.
	 */
	public DrawingVisible (int largeur, int hauteur) {
		super();
		
		this.zoomAndPanListener = new ZoomAndPanListener(this, 0, ZoomAndPanListener.DEFAULT_MAX_ZOOM_LEVEL, 1.2);
		this.addMouseListener(zoomAndPanListener);
		this.addMouseMotionListener(zoomAndPanListener);
		this.addMouseWheelListener(zoomAndPanListener);
		
		BufferedImage img = new BufferedImage (largeur, hauteur, BufferedImage.TYPE_3BYTE_BGR);
		
		this.image = img;
		this.gr = img.createGraphics();
		
		this.zoomAndPanListener.setCoordTransform(this.gr.getTransform());
		
		this.bb_is_set = false;

		this.width = largeur;
		this.height = hauteur;

		this.long1 = (float)0.0;
		this.long2 = (float)largeur;
		this.lat1  = (float)0.0;
		this.lat2  = (float)hauteur;

		this.setColor(Color.white);
		gr.fillRect(0,0, largeur, hauteur);
		this.repaint();

	}

	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		g.setTransform(zoomAndPanListener.getCoordTransform());
		g.drawImage(image, 0, 0, this);
	}
	

	@Override
	public Dimension getPreferredSize() {
		Dimension size = new Dimension(0, 0);

		if (image != null) {
			int w = image.getWidth(null);
			int h = image.getHeight(null);
			size = new Dimension(w > 0 ? w : 0, h > 0 ? h : 0);
		}
		return size;
	}
	
	@Override
	public void setAutoRepaint(boolean autoRepaint) {
		this.autoRepaint = autoRepaint;
	}
	
	protected void doAutoPaint() {
		if (autoRepaint) {
			this.repaint();
		}
	}
	
	public void setWidth (int width) {
		this.gr.setStroke(new BasicStroke(width));
	}

	public void setColor (Color col) {
		this.gr.setColor (col);
	}

	public void setBB (double long1, double long2, double lat1, double lat2) {	

		if (long1 > long2 || lat1 > lat2) {
			throw new Error("DessinVisible.setBB : mauvaises coordonnees.");
		}

		/* Adapte la BB en fonction de la taille du dessin, pour préserver le ratio largeur/hauteur */
		double deltalong = long2 - long1 ;
		double deltalat = lat2 - lat1 ;
		double ratiobb = deltalong / deltalat ;
		double ratiogr = width / height ;

		/* On ne peut qu'agrandir la BB, pour ne rien perdre. 
		 * Si le ratiobb est trop petit, il faut agrandir deltalong 
		 * s'il est trop grand, il faut agrandir deltalat. */
		if (ratiobb < ratiogr) {
			/* De combien faut-il agrandir ? */
			double delta = (ratiogr - ratiobb) * deltalat ;

			this.long1 = (float)(long1 - 0.5*delta) ;
			this.long2 = (float)(long2 + 0.5*delta) ;
			this.lat1 = (float)lat1 ;
			this.lat2 = (float)lat2 ;
		}
		else {
			double delta = (deltalong / ratiogr) - deltalat ;

			this.long1 = (float)long1 ;
			this.long2 = (float)long2 ;
			this.lat1 = (float)(lat1 - 0.5*delta);
			this.lat2 = (float)(lat2 + 0.5*delta);
		}

		this.bb_is_set = true ;
	}

	private int projx(float lon) {
		return (int)(width * (lon - this.long1) / (this.long2 - this.long1)) ;
	}

	private int projy(float lat) {
		return (int)(height * (1 - (lat - this.lat1) / (this.lat2 - this.lat1))) ;
	}

	private void checkBB() {
		if (!this.bb_is_set) {
			throw new Error("Classe DessinVisible : vous devez invoquer la methode setBB avant de dessiner.") ;
		}
	}

	public void drawLine (float long1, float lat1, float long2, float lat2) {
		this.checkBB() ;
		int x1 = this.projx(long1) ;
		int x2 = this.projx(long2) ;
		int y1 = this.projy(lat1) ;
		int y2 = this.projy(lat2) ;

		gr.drawLine(x1, y1, x2, y2) ;
		this.doAutoPaint();
	}

	public void drawPoint (float lon, float lat, int width) {
		this.checkBB() ;
		int x = this.projx(lon) - width / 2 ;
		int y = this.projy(lat) - width / 2 ;
		gr.fillOval (x, y, width, width) ;
		this.doAutoPaint();
	}

	public void putText (float lon, float lat, String txt) {
		this.checkBB() ;
		int x = this.projx(lon) ;
		int y = this.projy(lat) ;
		gr.drawString (txt, x, y) ;
		this.doAutoPaint();	
	}

}
