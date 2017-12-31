package itb2.image;

import java.awt.Graphics;

/**
 * Image that can be drawn to, using a Graphics Object.
 * 
 * @author Micha Strauch
 */
public interface DrawableImage extends Image {
	
	/**
	 * Returns the {@link Graphics} object for drawing.
	 * 
	 * @return Graphics object
	 */
	public Graphics getGraphics();
	
}
