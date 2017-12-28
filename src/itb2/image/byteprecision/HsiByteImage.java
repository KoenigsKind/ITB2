package itb2.image.byteprecision;

import java.awt.Dimension;

import itb2.image.HsiImage;

/**
 * Represents an image with hue, saturation and intensity channel
 *  
 * @author Micha Strauch
 */
class HsiByteImage extends AbstractByteImage implements HsiImage {
	private static final long serialVersionUID = 503832263212040770L;
	
	/** Max values for hue, saturation and intensity */
	private int[] maxValues = {255, 255, 255};

	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	HsiByteImage(int width, int height) {
		super(width, height, 3);
	}
	
	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	HsiByteImage(Dimension size) {
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
	public double maxIntensity() {
		return maxValues[INTENSITY];
	}
	
	@Override
	public void setMaxValue(double hue, double saturation, double intensity) {
		if(hue != (int) hue || saturation != (int) saturation || intensity != (int) intensity)
			throw new IllegalArgumentException(String.format("Values must be integers; given was: (%.1f, %.1f, %.1f)",
					hue, saturation, intensity));
		if(hue < 0 || hue > 255 || saturation < 0 || saturation > 255 || intensity < 0 || intensity > 255)
			throw new IllegalArgumentException(String.format("Values must be between 0 and 255; given was: (%.0f, %.0f, %.0f)",
					hue, saturation, intensity));
		
		maxValues[HUE] = (int)hue;
		maxValues[SATURATION] = (int)saturation;
		maxValues[INTENSITY] = (int)intensity;
	}
	
	@Override
	protected double[] getRGB(int column, int row) {
		double h = getValue(column, row, HUE);
		double s = getValue(column, row, SATURATION);
		double i = getValue(column, row, INTENSITY);
		
		return hsi2rgb(h, s, i);
	}
	
	/**
	 * Converts HSI values to RGB values
	 * 
	 * @param h Hue
	 * @param s Saturation
	 * @param i Intensity
	 * @return Array with RGB-values
	 */
	private double[] hsi2rgb(double h, double s, double i) {
		// normalized HSI-values:
		h *= 2 * Math.PI / maxHue();
		s /= maxSaturation();
		i /= maxIntensity();
		double x = i * (1 - s);
		double y = i * (1 + ((s * Math.cos(h)) / (Math.cos(Math.PI / 3 - h))));
		double z = 3 * i - (x + y);
		
		double rgb[] = new double[3];
		if (h < (Math.PI * 2 / 3)) {
			rgb[2] = x;
			rgb[0] = y;
			rgb[1] = z;
		} else {
			if (h < 4 * Math.PI / 3) {
				h = h - 2 * Math.PI / 3;
				y = i * (1 + ((s * Math.cos(h)) / (Math.cos(Math.PI / 3 - h))));
				z = 3 * i - (x + y);
				rgb[0] = x;
				rgb[1] = y;
				rgb[2] = z;
			} else {
				h = h - 4 * Math.PI / 3;
				y = i * (1 + ((s * Math.cos(h)) / (Math.cos(Math.PI / 3 - h))));
				z = 3 * i - (x + y);
				rgb[1] = x;
				rgb[2] = y;
				rgb[0] = z;
			}
		}
		
		// Bound RGB values between 0 and 1
		for(int k = 0; k < 3; k++) {
			if(rgb[k] < 0)
				rgb[k] = 0;
			
			if(rgb[k] > 1)
				rgb[k] = 1;
		}

		// convert and round normalized values
		rgb[0] = Math.round(rgb[0] * 255);
		rgb[1] = Math.round(rgb[1] * 255);
		rgb[2] = Math.round(rgb[2] * 255);
		return rgb;
	}

}
