package itb2.image;

import java.awt.Dimension;

/**
 * Represents an image with only one channel (grayscale)
 *  
 * @author Micha Strauch
 */
public class GrayscaleImage extends AbstractImage {
	
	/** ID of the channel */
	public static final int GREYSCALE = 0;
	
	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	public GrayscaleImage(int width, int height) {
		super(width, height, 1);
	}
	
	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	public GrayscaleImage(Dimension size) {
		super(size, 1);
	}
	
	/**
	 * Constructs an image from the given data matrix.
	 * The matrix must be off the size: double[width][height]
	 * 
	 * @param data Original data
	 */
	public GrayscaleImage(double[][] data) {
		this(data.length, data[0].length);
		
		for(int row = 0; row < size.height; row++)
			for(int col = 0; col < size.width; col++)
				this.data[row][col][GREYSCALE] = data[row][col];
	}
	
	/**
	 * Constructs an image from the given channel.
	 * 
	 * @param channel Channel to construct image from
	 */
	public GrayscaleImage(Channel channel) {
		super(channel.getWidth(), channel.getHeight(), 1);
		
		for(int row = 0; row < size.height; row++)
			for(int col = 0; col < size.width; col++)
				this.data[row][col][GREYSCALE] = channel.getValue(row, col);
	}

	@Override
	protected double[] getRGB(int row, int column) {
		double value = data[row][column][GREYSCALE];
		
		return new double[]{value, value, value};
	}

}
