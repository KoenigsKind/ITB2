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
class RgbByteImage extends AbstractByteImage implements RgbImage {
	private static final long serialVersionUID = -7012819999944752756L;

	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	RgbByteImage(int width, int height) {
		super(width, height, 3);
	}
	
	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	RgbByteImage(Dimension size) {
		super(size, 3);
	}
	
	/**
	 * Constructs an image from the given {@link BufferedImage}
	 * 
	 * @param image Original image
	 */
	RgbByteImage(BufferedImage image) {
		super(image.getWidth(), image.getHeight(), 3);
		
		Raster raster = image.getData();
		int width = raster.getWidth(), height = raster.getHeight();
		int minCol = raster.getMinX(), minRow = raster.getMinY();
		
		double[] rgb = new double[4];
		for(int col = 0; col < width; col++) {
			for(int row = 0; row < height; row++) {
				raster.getPixel(minCol + col, minRow + row, rgb);
				
				for(int cha = 0; cha < 3; cha++)
					setValue(col, row, cha, rgb[cha]);
			}
		}
	}

	@Override
	protected double[] getRGB(int column, int row) {
		double r = getValue(column, row, RED);
		double g = getValue(column, row, GREEN);
		double b = getValue(column, row, BLUE);
		
		return new double[]{r, g, b};
	}

}
