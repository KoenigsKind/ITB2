package itb2.image;

import java.awt.Dimension;

public class RgbImage extends AbstractImage {
	public static final int RED = 0, GREEN = 1, BLUE = 2;
	
	public RgbImage(int width, int height) {
		super(width, height, 3);
	}
	
	public RgbImage(Dimension size) {
		super(size, 3);
	}
	
	public RgbImage(double[][][] data) {
		super(data);
		
		if(channelCount != 3)
			throw new RuntimeException("data must have three channels (red, green and blue)");
	}

	@Override
	protected double[] getRGB(int row, int column) {
		double r = data[row][column][RED];
		double g = data[row][column][GREEN];
		double b = data[row][column][BLUE];
		
		return new double[]{r, g, b};
	}

}
