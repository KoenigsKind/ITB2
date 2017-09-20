package itb2.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;

import itb2.image.Image;

/**
 * Simple window to display an image.
 * 
 * @author Micha Strauch
 */
public class ImageFrame extends JFrame {
	private static final long serialVersionUID = 2330218900409843365L;
	
	/** Max size the content pane will have when creating the window */ 
	private static final int DEFAULT_WIDTH = 600, DEFAULT_HEIGHT = 450;
	
	/** Prefix title */
	private static final String TITLE = "Image Viewer";
	
	/** Image painter, displaying the image */
	private final SingleImagePainter imagePainter;
	
	/**
	 * Creating the image window in the middle of the given component and displaying the given image.
	 * You must call setVisible(true) after creating the ImageFrame!
	 * 
	 * @param comp  Component this ImageFrame belongs to
	 * @param image
	 */
	public ImageFrame(Component comp, Image image) {
		// Display image
		imagePainter = new SingleImagePainter();
		imagePainter.setImage(image);
		
		DraggableScrollPane scrollPane = new DraggableScrollPane(imagePainter);
		add(scrollPane);
		
		// Set size
		int width = DEFAULT_WIDTH < image.getWidth() ? DEFAULT_WIDTH : image.getWidth();
		int height = DEFAULT_HEIGHT < image.getHeight() ? DEFAULT_HEIGHT : image.getHeight();
		scrollPane.setPreferredSize(new Dimension(width + 10, height + 10));
		
		// Set title
		String title = TITLE;
		if(image.getName() != null) {
			if(image.getName() instanceof File)
				title += " - " + ((File)image.getName()).getName();
			else
				title += " - " + image.getName();
		}
		setTitle(title);
		
		// Closure
		pack();
		setLocationRelativeTo(comp);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	/** Zoom the image to fit the screen */
	public void fitToScreen() {
		//TODO Add fit to screen
		throw new UnsupportedOperationException("Not yet implemented!");
	}
	
	/** Reset zoom to original size */
	public void resetZoom() {
		setZoom(1);
	}
	
	/** Set zoom to given value (1 = original size) */
	public void setZoom(double zoom) {
		imagePainter.setZoom(zoom);
	}
	
}
