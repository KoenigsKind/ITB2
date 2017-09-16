package itb2.image;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

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
	
	public RgbImage(BufferedImage image) {
		super(image.getWidth(), image.getHeight(), 3);
		
		Raster raster = image.getData();
		int width = raster.getWidth(), height = raster.getHeight();
		int minCol = raster.getMinX(), minRow = raster.getMinY();
		
		double[] rgb = new double[3];
		for(int col = 0; col < width; col++) {
			for(int row = 0; row < height; row++) {
				raster.getPixel(minCol + col, minRow + row, rgb);
				setValue(row, col, rgb);
			}
		}
	}

	@Override
	protected double[] getRGB(int row, int column) {
		double r = data[row][column][RED];
		double g = data[row][column][GREEN];
		double b = data[row][column][BLUE];
		
		return new double[]{r, g, b};
	}

}
