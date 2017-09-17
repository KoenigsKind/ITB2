package itb2.image;

import java.awt.Dimension;

/**
 * Represents an image with huse, saturation and intensity channel
 *  
 * @author Micha Strauch
 */
public class HsiImage extends AbstractImage {
	
	/** Max value for each channel */
	public static final double MAX_HUE = 360, MAX_SATURATION = 100, MAX_INTENSITY = 255;
	
	/** IDs for each channel */
	public static final int HUE = 0, SATURATION = 1, INTENSITY = 2;
	
	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	public HsiImage(int width, int height) {
		super(width, height, 3);
	}
	
	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	public HsiImage(Dimension size) {
		super(size, 3);
	}
	
	/**
	 * Constructs an image from the given data matrix.
	 * The matrix must be off the size: double[width][height][3]
	 * With the last Array being the HSI-Channel.
	 * 
	 * @param data Original data
	 */
	public HsiImage(double[][][] data) {
		super(data);
		
		if(channelCount != 3)
			throw new RuntimeException("data must have three channels (hue , saturation and intensity)");
	}
	
	@Override
	protected double[] getRGB(int row, int column) {
		double h = data[row][column][HUE];
		double s = data[row][column][SATURATION];
		double i = data[row][column][INTENSITY];
		
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
		h *= 2 * Math.PI / MAX_HUE;
		s /= MAX_SATURATION;
		i /= MAX_INTENSITY;
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

		// convert and round normalized values
		rgb[0] = Math.round(rgb[0] * 255);
		rgb[1] = Math.round(rgb[1] * 255);
		rgb[2] = Math.round(rgb[2] * 255);
		return rgb;
	}

}
