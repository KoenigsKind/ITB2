package itb2.image.doubleprecision;

import java.awt.Dimension;

import itb2.image.Channel;
import itb2.image.GrayscaleImage;

/**
 * Represents an image with only one channel (grayscale)
 *  
 * @author Micha Strauch
 */
public class GrayscaleDoubleImage extends AbstractDoubleImage implements GrayscaleImage {
	private static final long serialVersionUID = -5520424708879023970L;

	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	public GrayscaleDoubleImage(int width, int height) {
		super(width, height, 1);
	}
	
	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	public GrayscaleDoubleImage(Dimension size) {
		super(size, 1);
	}
	
	/**
	 * Constructs an image from the given data matrix.
	 * The matrix must be off the size: double[width][height]
	 * 
	 * @param data Original data
	 */
	@Deprecated
	public GrayscaleDoubleImage(double[][] data) {
		this(data.length, data[0].length);
		
		for(int col = 0; col < size.width; col++)
			for(int row = 0; row < size.height; row++)
				this.data[GRAYSCALE][col][row] = data[col][row];
	}
	
	/**
	 * Constructs an image from the given channel.
	 * 
	 * @param channel Channel to construct image from
	 */
	public GrayscaleDoubleImage(Channel channel) {
		super(channel.getWidth(), channel.getHeight(), 1);
		
		for(int col = 0; col < size.width; col++)
			for(int row = 0; row < size.height; row++)
				this.data[GRAYSCALE][col][row] = channel.getValue(col, row);
	}

	@Override
	protected double[] getRGB(int column, int row) {
		double value = data[GRAYSCALE][column][row];
		
		return new double[]{value, value, value};
	}

}
