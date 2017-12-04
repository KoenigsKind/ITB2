package itb2.image;

public interface HsiImage extends Image {
	/** Max value for each channel */
	public static final int MAX_HUE = 360, MAX_SATURATION = 100, MAX_INTENSITY = 255;
	//TODO MAX_HUE zu groß für AbstractByteImage
	
	/** IDs for each channel */
	public static int HUE = 0, SATURATION = 1, INTENSITY = 2;
}
