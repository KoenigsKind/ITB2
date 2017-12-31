package itb2.image.byteprecision;

import java.awt.Dimension;

import itb2.image.BinaryImage;

/**
 * BinaryImage storing 8 pixels in a row in one byte
 *
 * @author Micha Strauch
 */
class BinaryByteImage extends AbstractByteImage implements BinaryImage {
	private static final long serialVersionUID = -2722823332210447391L;
	
	/** Number of bits to be stored in one element of {@link #data} */
	private static final int BITS = 8;
	
	/**
	 * Constructs an image with given size
	 * 
	 * @param size         Size of this image
	 */
	BinaryByteImage(Dimension size) {
		this(size.width, size.height);
	}
	
	/**
	 * Constructs an image with given size
	 * 
	 * @param width        Width of this image
	 * @param height       Height of this image
	 */
	BinaryByteImage(int width, int height) {
		super(width, (int)Math.ceil((double)height / BITS), 1);
		
		size.height = height;
	}

	@Override
	public double[] getValue(int column, int row) {
		return new double[] {getValue(column, row, BINARY)};
	}

	@Override
	public double getValue(int column, int row, int channel) {
		int mask = 0x1 << (row % BITS);
		int val = data[BINARY][column][row / BITS];
		
		return (val & mask) == mask ? 1 : 0;
	}

	@Override
	public void setValue(int column, int row, double... values) {
		if(values.length != 1)
			throw new IndexOutOfBoundsException();
		
		setValue(column, row, BINARY, values[0]);
	}

	@Override
	public void setValue(int column, int row, int channel, double value) {
		if(channel != BINARY)
			throw new IndexOutOfBoundsException();
		
		int mask = 0x1 << (row % BITS);
		
		if(value > 0)
			data[BINARY][column][row / BITS] |= mask; // Set to white
		else
			data[BINARY][column][row / BITS] &= ~mask; // Set to black
		
		updateImage();
	}

	@Override
	protected double[] getRGB(int column, int row) {
		int mask = 0x1 << (row % BITS);
		int value = data[BINARY][column][row / BITS];
		
		if((mask & value) == mask)
			return new double[] {255, 255, 255}; // White
		
		return new double[3]; // Black
	}

}
