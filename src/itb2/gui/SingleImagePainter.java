package itb2.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import itb2.image.Image;

/**
 * Panel to display a single image.
 * Use the mouse wheel to zoom in/out.
 *
 * @author Micha Strauch
 */
class SingleImagePainter extends JPanel {
	private static final long serialVersionUID = 642660245893976850L;
	
	/** Preferred size of this JPanel */
	private final Dimension dimension;
	
	/** Image to display */
	private Image image;
	
	/** Current zoom */
	private double zoom;
	
	/** Constructor */
	SingleImagePainter() {
		dimension = new Dimension();
		zoom = 1;
		setBackground(GuiConstants.WORKBENCH_BACKGROUND);
		
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				Point preZoom = getMouseOnPainter();
				
				double factor = Math.pow(0.9, e.getWheelRotation());
				zoom *= factor;
				updateSize();
				
				Point postZoom = getMouseOnPainter();
				
				double dx = (preZoom.x - postZoom.x) * zoom;
				double dy = (preZoom.y - postZoom.y) * zoom;
				
				Rectangle rect = getVisibleRect();
				rect.translate((int)dx, (int)dy);
				scrollRectToVisible(rect);
			}
		});
	}
	
	/** Returns the mouse position relative to this image painter. */
	Point getMouseOnPainter() {
		Point p = getMousePosition();
		p.x /= zoom;
		p.y /= zoom;
		
		return p;
	}
	
	/** Returns the mouse position relative to the image. */
	Point getMouseOnImage() {
		Point p = getMouseOnPainter();
		
		Point origin = getImageOrigin();
		p.translate(-origin.x, -origin.y);
		
		return p;
	}
	
	/** Sets the current zoom. */
	void setZoom(double zoom) {
		this.zoom = zoom;
		updateSize();
	}
	
	/** Returns the current zoom. */
	double getZoom() {
		return zoom;
	}
	
	/** Sets the image to display. */
	void setImage(Image image) {
		this.image = image;
		updateSize();
	}
	
	/** Returns the displayed image. */
	Image getImage() {
		return image;
	}
	
	/** Updates the size of this panel and repaints the image. */
	void updateSize() {
		dimension.height = (int)(image == null ? 0 : image.getHeight() * zoom);
		dimension.width = (int)(image == null ? 0 : image.getWidth() * zoom);
		
		setPreferredSize(dimension);
		revalidate();
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(image == null)
			return;
		
		Graphics2D g2d = g instanceof Graphics2D ? (Graphics2D) g : null;
		
		Point origin = getImageOrigin();
		
		if(g2d != null)
			g2d.scale(zoom, zoom);
		
		g.drawImage(image.asBufferedImage(), origin.x, origin.y, null);
		
		if(g2d != null)
			g2d.scale(1/zoom, 1/zoom);
	}
	
	/** Returns the location of the image's top left corner. */
	private Point getImageOrigin() {
		int paneWidth = getWidth();
		int paneHeight = getHeight();
		
		double imgWidth = image.getWidth() * zoom;
		double imgHeight = image.getHeight() * zoom;
		
		double x = imgWidth < paneWidth ? (paneWidth - imgWidth) / 2 : 0;
		double y = imgHeight < paneHeight ? (paneHeight - imgHeight) / 2 : 0;
		
		x /= zoom;
		y /= zoom;
		
		return new Point((int)x, (int)y);
	}
}
