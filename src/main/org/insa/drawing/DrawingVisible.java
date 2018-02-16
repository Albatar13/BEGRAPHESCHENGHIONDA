package org.insa.drawing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.*;

import javax.swing.JPanel;

/**
 *   Cette implementation de la classe Dessin produit vraiment un affichage
 *   (au contraire de la classe DessinInvisible).
 */

public class DrawingVisible extends JPanel implements Drawing {

	/**
	 * 
	 */
	private static final long serialVersionUID = 96779785877771827L;
	
	private final Graphics2D gr;

	private float long1;
	private float long2;
	private float lat1;
	private float lat2;
	
	// Width and height of the image
	private final int width, height;

	private boolean bb_is_set ;
	
	private Image image;
	private ZoomAndPanListener zoomAndPanListener;
	
	public boolean autoRepaint = true;

	/**
	 *  Cree et affiche une nouvelle fenetre de dessin.
	 */
	public DrawingVisible() {
		super();
		
		this.zoomAndPanListener = new ZoomAndPanListener(this, ZoomAndPanListener.DEFAULT_MIN_ZOOM_LEVEL, 20, 1.2);
		this.addMouseListener(zoomAndPanListener);
		this.addMouseMotionListener(zoomAndPanListener);
		this.addMouseWheelListener(zoomAndPanListener);
		
		this.width = 2000;
		this.height = 1600;
		
		BufferedImage img = new BufferedImage (this.width, this.height, BufferedImage.TYPE_3BYTE_BGR);
		
		this.image = img;
		this.gr = img.createGraphics();
		
		this.zoomAndPanListener.setCoordTransform(this.gr.getTransform());
		
		this.bb_is_set = false;


		this.long1 = 0.0f;
		this.long2 = this.width;
		this.lat1  = 0.0f;
		this.lat2  = this.height;

		this.clear();
		this.repaint();

	}

	@Override
	public void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		g.setTransform(zoomAndPanListener.getCoordTransform());
		g.drawImage(image, 0, 0, this);
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
	
	public void setWidth(int width) {
		this.gr.setStroke(new BasicStroke(width));
	}

	public void setColor(Color col) {
		this.gr.setColor(col);
	}

	public void clear() {
		this.gr.setColor(Color.WHITE);
		this.gr.fillRect(0, 0, this.width, this.height);
	}

	public void setBB(double long1, double long2, double lat1, double lat2) {	

		if (long1 > long2 || lat1 > lat2) {
			throw new Error("DessinVisible.setBB : mauvaises coordonnees.");
		}
		
		this.long1 = (float)long1;
		this.long2 = (float)long2;
		this.lat1= (float)lat1;
		this.lat2 = (float)lat2;
		
		this.bb_is_set = true;
		
		double scale = 1 / Math.max(this.width / (double)this.getWidth(),  this.height / (double)this.getHeight());
		
		this.zoomAndPanListener.getCoordTransform().setToIdentity();
		this.zoomAndPanListener.getCoordTransform().translate((this.getWidth() - this.width * scale) / 2, 
				(this.getHeight() - this.height * scale) / 2);
		this.zoomAndPanListener.getCoordTransform().scale(scale, scale);
		this.zoomAndPanListener.setZoomLevel(0);
		this.repaint();
		
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

	public void drawLine(float long1, float lat1, float long2, float lat2) {
		this.checkBB() ;
		int x1 = this.projx(long1) ;
		int x2 = this.projx(long2) ;
		int y1 = this.projy(lat1) ;
		int y2 = this.projy(lat2) ;

		gr.drawLine(x1, y1, x2, y2) ;
		this.doAutoPaint();
	}

	public void drawPoint(float lon, float lat, int width) {
		this.checkBB() ;
		int x = this.projx(lon) - width / 2 ;
		int y = this.projy(lat) - width / 2 ;
		gr.fillOval (x, y, width, width) ;
		this.doAutoPaint();
	}

	public void putText(float lon, float lat, String txt) {
		this.checkBB() ;
		int x = this.projx(lon) ;
		int y = this.projy(lat) ;
		gr.drawString (txt, x, y) ;
		this.doAutoPaint();	
	}

}
