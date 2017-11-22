package itb2.image.byteprecision;

import java.awt.Dimension;

import itb2.image.Channel;
import itb2.image.GrayscaleImage;

/**
 * Represents an image with only one channel (grayscale)
 *  
 * @author Micha Strauch
 */
public class GrayscaleByteImage extends AbstractByteImage implements GrayscaleImage {
	
	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	public GrayscaleByteImage(int width, int height) {
		super(width, height, 1);
	}
	
	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	public GrayscaleByteImage(Dimension size) {
		super(size, 1);
	}
	
	/**
	 * Constructs an image from the given data matrix.
	 * The matrix must be off the size: double[width][height]
	 * 
	 * @param data Original data
	 */
	public GrayscaleByteImage(double[][] data) {
		this(data.length, data[0].length);
		
		for(int row = 0; row < size.height; row++)
			for(int col = 0; col < size.width; col++)
				this.data[row][col] = ((int)data[row][col]) & 0xFF;
	}
	
	/**
	 * Constructs an image from the given channel.
	 * 
	 * @param channel Channel to construct image from
	 */
	public GrayscaleByteImage(Channel channel) {
		super(channel.getWidth(), channel.getHeight(), 1);
		
		for(int row = 0; row < size.height; row++)
			for(int col = 0; col < size.width; col++)
				this.data[row][col] = ((int)channel.getValue(row, col)) & 0xFF;
	}

	@Override
	protected double[] getRGB(int row, int column) {
		double value = getValue(row, column, GRAYSCALE);
		
		return new double[]{value, value, value};
	}

}
