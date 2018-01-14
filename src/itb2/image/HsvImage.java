package itb2.image;

/**
 * Image with three channels for hue, saturation and value.
 *
 * @author Micha Strauch
 */
public interface HsvImage extends Image {
	
	/** IDs for each channel */
	public static int HUE = 0, SATURATION = 1, VALUE = 2;
	
	/** Maximum value for hue */
	public double maxHue();
	
	/** Maximum value for saturation */
	public double maxSaturation();
	
	/** Maximum value for value */
	public double maxValue();
	
	/** Sets the maximum value for hue, saturation and value */
	public void setMaxValue(double hue, double saturation, double value);
	
}
