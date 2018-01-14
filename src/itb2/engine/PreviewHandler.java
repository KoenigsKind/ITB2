package itb2.engine;

import itb2.image.Image;

/**
 * Handler for changing the image preview.<p>
 * Used at {@link CommunicationManager#preview(String, Image)}
 *
 * @author Micha Strauch
 */
public interface PreviewHandler {
	
	/**
	 * Change preview to display given image with given message.
	 * 
	 * @param message Message to display
	 * @param image   New image to display
	 */
	public void preview(String message, Image image);
	
	/** Closes this preview. */
	public void close();

}
