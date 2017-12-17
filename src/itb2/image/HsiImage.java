package itb2.image;

public interface HsiImage extends Image {
	
	/** IDs for each channel */
	public static int HUE = 0, SATURATION = 1, INTENSITY = 2;
	
	/** Maximum value for hue */
	public int maxHue(); //360
	
	/** Maximum value for saturation */
	public int maxSaturation(); //100
	
	/** Maximum value for intensity */
	public int maxIntensity(); //255
	
	/** Sets the maximum value for hue, saturation and intensity */
	public void setMaxValue(int hue, int saturation, int intensity);
	
}
