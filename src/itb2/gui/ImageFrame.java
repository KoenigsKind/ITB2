package itb2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFrame;

import itb2.image.DrawableImage;
import itb2.image.Image;
import itb2.image.ImageFactory;

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
	
	/** ScrollPane containing imagePainter */
	private final DraggableScrollPane scrollPane;
	
	/**
	 * Creating the image window in the middle of the given component and displaying the given image.
	 * Lets the user select pixels on the image and calls the given consumer, once the maximum number
	 * of pixels are called (if <code>maxSelections &gt; 0</code>) or the window is closed.
	 * 
	 * @param comp                Component this ImageFrame belongs to
	 * @param image               Image to display
	 * @param title               Title of the frame (Optional, set to null otherwise)
	 * @param maxSelections       Maximum number of selections (Optional, set to 0 otherwise)
	 * @param callAfterSelection  Consumer to be called after selection
	 */
	public ImageFrame(Component comp, Image image, String title, int maxSelections, Consumer<List<Point>> callAfterSelection) {
		this(comp, ImageFactory.bytePrecision().drawable(image.asBufferedImage()), title);
		
		Graphics graphics = ((DrawableImage)imagePainter.getImage()).getGraphics();
		
		List<Point> selections = new ArrayList<>();
		
		// Handle mouse click
		imagePainter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point p = imagePainter.getMouseOnImage();
				
				Image image = imagePainter.getImage();
				if(p.x < 0 || p.y < 0 || p.x >= image.getWidth() || p.y >= image.getHeight())
					return;
				
				selections.add(p);
				
				if(maxSelections > 0 && maxSelections == selections.size())
					dispose();
				else {
					// Draw circle at selection
					graphics.setColor(Color.BLACK);
					graphics.fillOval(p.x - 4, p.y - 4, 9, 9);
					graphics.setColor(Color.WHITE);
					graphics.fillOval(p.x - 3, p.y - 3, 7, 7);
					imagePainter.repaint();
				}
			}
		});
		
		// Call consumer when closing
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				callAfterSelection.accept(selections);
			};
		});
		setVisible(true);
	}
	
	/**
	 * Creating the image window in the middle of the given component and displaying the given image.
	 * You must call setVisible(true) after creating the ImageFrame!
	 * 
	 * @param comp  Component this ImageFrame belongs to
	 * @param image Image to display
	 */
	public ImageFrame(Component comp, Image image) {
		this(comp, image, null);
	}
	
	/**
	 * Creating the image window in the middle of the given component and displaying the given image.
	 * You must call setVisible(true) after creating the ImageFrame!
	 * 
	 * @param comp  Component this ImageFrame belongs to
	 * @param image Image to display
	 * @param title Title of the frame (Optional, set to null otherwise)
	 */
	public ImageFrame(Component comp, Image image, String title) {
		// Display image
		imagePainter = new SingleImagePainter();
		imagePainter.setImage(image);
		
		ImageToolBar toolBar = new ImageToolBar(this);
		scrollPane = new DraggableScrollPane(imagePainter);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(toolBar, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		// Set size
		int width = DEFAULT_WIDTH < image.getWidth() ? DEFAULT_WIDTH : image.getWidth();
		int height = DEFAULT_HEIGHT < image.getHeight() ? DEFAULT_HEIGHT : image.getHeight();
		scrollPane.setPreferredSize(new Dimension(width + 10, height + 10));
		
		// Set title
		setTitle(title);
		
		// Closure
		pack();
		setLocationRelativeTo(comp);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	/** Zoom the image to fit the screen */
	public void fitToScreen() {
		Image image = imagePainter.getImage();
		if(image != null) {
			int imgWidth = image.getWidth();
			int imgHeight = image.getHeight();
			
			double border = scrollPane.getVerticalScrollBar().getWidth();

			double zoomHor = (scrollPane.getWidth() - border) / imgWidth;
			double zoomVert = (scrollPane.getHeight() - border) / imgHeight;
			double zoom = zoomHor < zoomVert ? zoomHor : zoomVert;
			imagePainter.setZoom(zoom);
		} else
			imagePainter.setZoom(1);
	}
	
	/** Reset zoom to original size */
	public void resetZoom() {
		setZoom(1);
	}
	
	/** Set zoom to given value (1 = original size) */
	public void setZoom(double zoom) {
		imagePainter.setZoom(zoom);
	}
	
	@Override
	public void setTitle(String title) {
		if(title == null) {
			title = TITLE;
			Image image = imagePainter.getImage();
			if(image != null && image.getName() != null) {
				if(image.getName() instanceof File)
					title += " - " + ((File)image.getName()).getName();
				else
					title += " - " + image.getName();
			}
		}
		
		super.setTitle(title);
	}

	/** Sets the image to display */
	public void setImage(Image image) {
		imagePainter.setImage(image);
	}

}
