package itb2.image;

public interface HsvImage extends Image {
	
	/** IDs for each channel */
	public static int HUE = 0, SATURATION = 1, VALUE = 2;
	
	/** Maximum value for hue */
	public double maxHue(); //360
	
	/** Maximum value for saturation */
	public double maxSaturation(); //100
	
	/** Maximum value for value */
	public double maxValue(); //255
	
	/** Sets the maximum value for hue, saturation and value */
	public void setMaxValue(double hue, double saturation, double value);
	
}
