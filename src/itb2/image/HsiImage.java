package itb2.image;

/**
 * Image with three channels, for hue, saturation and intensity.
 * The intensity channel is equal to a grayscale image.
 *
 * @author Micha Strauch
 */
public interface HsiImage extends Image {
	
	/** IDs for each channel */
	public static int HUE = 0, SATURATION = 1, INTENSITY = 2;
	
	/** Maximum value for hue */
	public double maxHue();
	
	/** Maximum value for saturation */
	public double maxSaturation();
	
	/** Maximum value for intensity */
	public double maxIntensity();
	
	/** Sets the maximum value for hue, saturation and intensity */
	public void setMaxValue(double hue, double saturation, double intensity);
	
}
