package org.insa.drawing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.*;

import javax.swing.JPanel;

import org.insa.graph.Point;

/**
 *   Cette implementation de la classe Dessin produit vraiment un affichage
 *   (au contraire de la classe DessinInvisible).
 */

public class Drawing extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 96779785877771827L;
	
	private final Graphics2D gr;

	private double long1, long2, lat1, lat2;
	
	// Width and height of the image
	private final int width, height;
	
	private Image image;
	private ZoomAndPanListener zoomAndPanListener;
	
	public boolean autoRepaint = true;

	/**
	 *  Cree et affiche une nouvelle fenetre de dessin.
	 */
	public Drawing() {
		
		this.zoomAndPanListener = new ZoomAndPanListener(this, ZoomAndPanListener.DEFAULT_MIN_ZOOM_LEVEL, 20, 1.2);
		this.addMouseListener(zoomAndPanListener);
		this.addMouseMotionListener(zoomAndPanListener);
		this.addMouseWheelListener(zoomAndPanListener);
		
		this.width = 2000;
		this.height = 1600;
		
		BufferedImage img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_3BYTE_BGR);
		
		this.image = img;
		this.gr = img.createGraphics();
		
		this.zoomAndPanListener.setCoordTransform(this.gr.getTransform());

		this.long1 = -180;
		this.long2 = 180;
		this.lat1  = -90;
		this.lat2  = 90;

		this.clear();
		this.repaint();

	}

	@Override
	public void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setTransform(zoomAndPanListener.getCoordTransform());
		g.drawImage(image, 0, 0, this);
	}
	
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
		
		this.long1 = long1;
		this.long2 = long2;
		this.lat1= lat1;
		this.lat2 = lat2;
				
		double scale = 1 / Math.max(this.width / (double)this.getWidth(),  this.height / (double)this.getHeight());
		
		this.zoomAndPanListener.getCoordTransform().setToIdentity();
		this.zoomAndPanListener.getCoordTransform().translate((this.getWidth() - this.width * scale) / 2, 
				(this.getHeight() - this.height * scale) / 2);
		this.zoomAndPanListener.getCoordTransform().scale(scale, scale);
		this.zoomAndPanListener.setZoomLevel(0);
		this.repaint();
		
	}

	private int projx(double lon) {
		return (int)(width * (lon - this.long1) / (this.long2 - this.long1)) ;
	}

	private int projy(double lat) {
		return (int)(height * (1 - (lat - this.lat1) / (this.lat2 - this.lat1))) ;
	}
	
	/**
	 * Return the longitude and latitude corresponding to the given
	 * position of the MouseEvent.
	 * 
	 * @param event
	 * 
	 * @return
	 */
	public Point getLongitudeLatitude(MouseEvent event) throws NoninvertibleTransformException {
		// Get the point using the inverse transform of the Zoom/Pan object, this gives us
		// a point within the drawing box (between [0, 0] and [width, height]).
		Point2D ptDst = this.zoomAndPanListener.getCoordTransform().inverseTransform(event.getPoint(), null);
		
		// Inverse the "projection" on x/y to get longitude and latitude.
		double lon = ptDst.getX();
		double lat = ptDst.getY();
		lon = (lon / this.width) * (this.long2 - this.long1) + this.long1;
		lat = (1 - lat / this.height) * (this.lat2 - this.lat1) + this.lat1;
		
		// Return a new point.
		return new Point(lon, lat);			
	}

	public void drawLine(Point from, Point to) {
		int x1 = this.projx(from.getLongitude()) ;
		int x2 = this.projx(to.getLongitude()) ;
		int y1 = this.projy(from.getLatitude()) ;
		int y2 = this.projy(to.getLatitude()) ;

		gr.drawLine(x1, y1, x2, y2) ;
		this.doAutoPaint();
	}

	public void drawPoint(Point point, int width) {
		int x = this.projx(point.getLongitude()) - width / 2;
		int y = this.projy(point.getLatitude()) - width / 2;
		gr.fillOval(x, y, width, width);
		this.doAutoPaint();
	}

	public void putText(Point point, String txt) {
		int x = this.projx(point.getLongitude());
		int y = this.projy(point.getLatitude());
		gr.drawString(txt, x, y);
		this.doAutoPaint();	
	}

}
