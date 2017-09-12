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
	protected double[] getRGB(int x, int y) {
		double r = data[x][y][RED];
		double g = data[x][y][GREEN];
		double b = data[x][y][BLUE];
		
		return new double[]{r, g, b};
	}

}
