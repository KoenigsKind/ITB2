package itb2.image.doubleprecision;

import java.awt.Dimension;

import itb2.image.HsvImage;

/**
 * Represents an image with hue, saturation and value channel
 *  
 * @author Micha Strauch
 */
class HsvDoubleImage extends AbstractDoubleImage implements HsvImage {
	private static final long serialVersionUID = 6987212242453114952L;
	
	/** Max values for hue, saturation and value */
	private double[] maxValues = {360, 100, 100};

	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	HsvDoubleImage(int width, int height) {
		super(width, height, 3);
	}
	
	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	HsvDoubleImage(Dimension size) {
		super(size, 3);
	}
	
	@Override
	public double maxHue() {
		return maxValues[HUE];
	}
	
	@Override
	public double maxSaturation() {
		return maxValues[SATURATION];
	}
	
	@Override
	public double maxValue() {
		return maxValues[VALUE];
	}
	
	@Override
	public void setMaxValue(double hue, double saturation, double value) {
		if(hue < 0 || saturation < 0 || value < 0)
			throw new IllegalArgumentException(String.format("Values must be positive; given was: (%.0f, %.0f, %.0f)",
					hue, saturation, value));
		
		maxValues[HUE] = hue;
		maxValues[SATURATION] = saturation;
		maxValues[VALUE] = value;
	}
	
	@Override
	protected double[] getRGB(int column, int row) {
		double h = data[HUE][column][row];
		double s = data[SATURATION][column][row];
		double v = data[VALUE][column][row];
		
		return hsv2rgb(h, s, v);
	}
	
	/**
	 * Converts HSV values to RGB values
	 * 
	 * @param h Hue
	 * @param s Saturation
	 * @param v Value
	 * @return Array with RGB-values
	 */
	private double[] hsv2rgb(double h, double s, double v) {
		// normalized HSV-values:
		h *= 360 / maxHue();
		h %= 360;
		if(h < 0)
			h += 360;
		s /= maxSaturation();
		v /= maxValue();
		
		double c = v * s; // chroma
		double x = c * (1 - Math.abs(((h/60)%2) - 1));
		double m = v - c;
		
		double[] rgb;
		
		if(h < 60)
			rgb = new double[] {c, x, 0};
		else if(h < 120)
			rgb = new double[] {x, c, 0};
		else if(h < 180)
			rgb = new double[] {0, c, x};
		else if(h < 240)
			rgb = new double[] {0, x, c};
		else if(h < 300)
			rgb = new double[] {x, 0, c};
		else
			rgb = new double[] {c, 0, x};
		
		for(int i = 0; i < 3; i++)
			rgb[i] = 255 * (rgb[i] + m);
		
		return rgb;
	}

}
