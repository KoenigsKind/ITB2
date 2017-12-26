package itb2.image.doubleprecision;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import itb2.image.RgbImage;

/**
 * Represents an image with red, green and blue channel
 *  
 * @author Micha Strauch
 */
public class RgbDoubleImage extends AbstractDoubleImage implements RgbImage {
	private static final long serialVersionUID = -2730549401612308560L;

	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	public RgbDoubleImage(int width, int height) {
		super(width, height, 3);
	}
	
	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	public RgbDoubleImage(Dimension size) {
		super(size, 3);
	}
	
	/**
	 * Constructs an image from the given {@link BufferedImage}
	 * 
	 * @param image Original image
	 */
	public RgbDoubleImage(BufferedImage image) {
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
		double r = data[RED][column][row];
		double g = data[GREEN][column][row];
		double b = data[BLUE][column][row];
		
		return new double[]{r, g, b};
	}

}
