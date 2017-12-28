package itb2.image.doubleprecision;

import java.awt.Dimension;

import itb2.image.Channel;
import itb2.image.GrayscaleImage;

/**
 * Represents an image with only one channel (grayscale)
 *  
 * @author Micha Strauch
 */
class GrayscaleDoubleImage extends AbstractDoubleImage implements GrayscaleImage {
	private static final long serialVersionUID = -5520424708879023970L;

	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	GrayscaleDoubleImage(int width, int height) {
		super(width, height, 1);
	}
	
	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	GrayscaleDoubleImage(Dimension size) {
		super(size, 1);
	}
	
	/**
	 * Constructs an image from the given channel.
	 * 
	 * @param channel Channel to construct image from
	 */
	GrayscaleDoubleImage(Channel channel) {
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
