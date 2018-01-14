package itb2.image;

/**
 * Image with three channels for red, green and blue value.
 * All values should be between 0 and 255.
 *
 * @author Micha Strauch
 */
public interface RgbImage extends Image {
	
	/** IDs for each channel */
	public static int RED = 0, GREEN = 1, BLUE = 2;
	
}
