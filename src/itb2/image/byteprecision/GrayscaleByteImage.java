package itb2.image.byteprecision;

import java.awt.Dimension;

import itb2.image.Channel;
import itb2.image.GrayscaleImage;

/**
 * Represents an image with only one channel (grayscale)
 *  
 * @author Micha Strauch
 */
class GrayscaleByteImage extends AbstractByteImage implements GrayscaleImage {
	private static final long serialVersionUID = -594941879275231638L;

	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	GrayscaleByteImage(int width, int height) {
		super(width, height, 1);
	}
	
	/**
	 * Constructs an image with given size.
	 * 
	 * @param width  Width of this image
	 * @param height Height of this image
	 */
	GrayscaleByteImage(Dimension size) {
		super(size, 1);
	}
	
	/**
	 * Constructs an image from the given channel.
	 * 
	 * @param channel Channel to construct image from
	 */
	GrayscaleByteImage(Channel channel) {
		super(channel.getWidth(), channel.getHeight(), 1);
		
		for(int col = 0; col < size.width; col++)
			for(int row = 0; row < size.height; row++)
				this.data[GRAYSCALE][col][row] = convert(channel.getValue(col, row));
	}

	@Override
	protected double[] getRGB(int column, int row) {
		double value = getValue(column, row, GRAYSCALE);
		
		return new double[]{value, value, value};
	}

}
