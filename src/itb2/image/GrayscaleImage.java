package itb2.image;

/**
 * Image containing only one channel for brightness.
 * All values should be between 0 and 255.
 *
 * @author Micha Strauch
 */
public interface GrayscaleImage extends Image {
	
	/** ID of the channel */
	public static int GRAYSCALE = 0;
	
}
