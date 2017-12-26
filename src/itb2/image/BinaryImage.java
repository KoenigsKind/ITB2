package itb2.image;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Iterator;

public class BinaryImage implements Image {
	private static final long serialVersionUID = -2722823332210447391L;
	public static final int BINARY = 0;
	private static final int BITS = 8 * Integer.BYTES;
	private final Dimension size;
	private final int[][] data;
	private Serializable name;
	private transient BufferedImage image;
	
	public BinaryImage(Dimension size) {
		this(size.width, size.height);
	}
	
	public BinaryImage(int width, int height) {
		int x = height / BITS;
		if(height % BITS != 0)
			x++;
		
		size = new Dimension(width, height);
		data = new int[width][x]; 
	}

	@Override
	public Iterator<Channel> iterator() {
		return new Iterator<Channel>() {
			int channel = BINARY;
			
			@Override
			public boolean hasNext() {
				return channel == BINARY;
			}
			
			@Override
			public Channel next() {
				return getChannel(channel++);
			}
		};
	}

	@Override
	public int getWidth() {
		return size.width;
	}

	@Override
	public int getHeight() {
		return size.height;
	}

	@Override
	public Dimension getSize() {
		return (Dimension)size.clone();
	}

	@Override
	public int getChannelCount() {
		return 1;
	}

	@Override
	public double[] getValue(int column, int row) {
		return new double[] {getValue(column, row, BINARY)};
	}

	@Override
	public double getValue(int column, int row, int channel) {
		int mask = 0x1 << (row % BITS);
		int val = data[column][row / BITS];
		
		return (val & mask) == mask ? 255 : 0;
	}

	@Override
	public void setValue(int column, int row, double... values) {
		if(values.length != 1)
			throw new IndexOutOfBoundsException();
		
		setValue(column, row, 0, values[0]);
	}

	@Override
	public void setValue(int column, int row, int channel, double value) {
		if(channel != BINARY)
			throw new IndexOutOfBoundsException();
		
		int mask = 0x1 << (row % BITS);
		
		if(value > 0)
			data[column][row / BITS] |= mask; // Set to white
		else
			data[column][row / BITS] &= ~mask; // Set to black
		
		image = null;
	}

	@Override
	public Channel getChannel(int channel) {
		if(channel != BINARY)
			throw new IndexOutOfBoundsException();
		
		return new SimpleChannel(this, channel);
	}

	@Override
	public Object getName() {
		return name;
	}

	@Override
	public void setName(Serializable name) {
		this.name = name;
	}

	@Override
	public BufferedImage asBufferedImage() {
		if(image == null) {
			image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
			int black = 0, white = 0xFFFFFF;
			
			for(int col = 0; col < size.width; col++) {
				for(int row = 0; row < size.height; row++) {
					int mask = 0x1 << (row % BITS);
					int value = data[col][row / BITS];
					
					int rgb = (mask & value) == mask ? white : black;
					image.setRGB(col, row, rgb);
				}
			}
			
		}
		return image;
	}

}
