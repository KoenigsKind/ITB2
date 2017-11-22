package itb2.image.byteprecision;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import itb2.image.RgbImage;

/**
 * Represents an image with red, green and blue channel
 *  
 * @author Micha Strauch
 */
public class RgbByteImage extends AbstractByteImage implements RgbImage {
	
	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	public RgbByteImage(int width, int height) {
		super(width, height, 3);
	}
	
	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	public RgbByteImage(Dimension size) {
		super(size, 3);
	}
	
	/**
	 * Constructs an image from the given {@link BufferedImage}
	 * 
	 * @param image Original image
	 */
	public RgbByteImage(BufferedImage image) {
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
		double r = getValue(row, column, RED);
		double g = getValue(row, column, GREEN);
		double b = getValue(row, column, BLUE);
		
		return new double[]{r, g, b};
	}

}
