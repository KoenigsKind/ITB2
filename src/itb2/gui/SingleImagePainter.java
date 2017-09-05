package itb2.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import itb2.image.Image;

class SingleImagePainter extends JPanel {
	private static final long serialVersionUID = 642660245893976850L;
	private final Dimension dimension;
	private Image image;
	private double zoom;
	
	SingleImagePainter() {
		dimension = new Dimension();
		zoom = 1;
		setBackground(GuiConstants.WORKBENCH_BACKGROUND);
		
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				Point preZoom = getMouseOnImage();
				
				double factor = Math.pow(0.9, e.getWheelRotation());
				zoom *= factor;
				updateSize();
				
				Point postZoom = getMouseOnImage();
				
				double dx = (preZoom.x - postZoom.x) * zoom;
				double dy = (preZoom.y - postZoom.y) * zoom;
				
				Rectangle rect = getVisibleRect();
				rect.translate((int)dx, (int)dy);
				scrollRectToVisible(rect);
			}
		});
	}
	
	Point getMouseOnImage() {
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p, this);
		p.x /= zoom;
		p.y /= zoom;
		return p;
	}
	
	void setZoom(double zoom) {
		this.zoom = zoom;
		updateSize();
	}
	
	double getZoom() {
		return zoom;
	}
	
	void setImage(Image image) {
		this.image = image;
		updateSize();
	}
	
	Image getImage() {
		return image;
	}
	
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
		
		int paneWidth = getWidth();
		int paneHeight = getHeight();
		
		double imgWidth = image.getWidth() * zoom;
		double imgHeight = image.getHeight() * zoom;
		
		double x = imgWidth < paneWidth ? (paneWidth - imgWidth) / 2 : 0;
		double y = imgHeight < paneHeight ? (paneHeight - imgHeight) / 2 : 0;
		
		x /= zoom;
		y /= zoom;
		
		if(g2d != null)
			g2d.scale(zoom, zoom);
		
		g.drawImage(image.asBufferedImage(), (int)x, (int)y, null);
		
		if(g2d != null)
			g2d.scale(1/zoom, 1/zoom);
	}
}
