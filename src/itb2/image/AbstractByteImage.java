package itb2.image;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract image, for image implementations that can store
 * a byte value for each channel of each pixel.
 * Implementations can have up to four channels.
 * 
 * @author Micha Strauch
 */
public abstract class AbstractByteImage implements Image {
	
	/** List of selections */
	protected final List<Point> selections;

	/** Size of this image */
	protected final Dimension size;

	/** Number of channels */
	protected final int channelCount;

	/** Data of this image */
	protected final int[][] data;

	/** Name of this image */
	protected Object name;

	/** Last rendered state of this image */
	private BufferedImage image;
	
	/**
	 * Constructs an image with given size and channel count
	 * 
	 * @param size         Size of this image
	 * @param channelCount Number of channels
	 */
	public AbstractByteImage(Dimension size, int channelCount) {
		this(size.width, size.height, channelCount);
	}
	
	/**
	 * Constructs an image with given size and channel count
	 * 
	 * @param width        Width of this image
	 * @param height       Height of this image
	 * @param channelCount Number of channels
	 */
	public AbstractByteImage(int width, int height, int channelCount) {
		if(channelCount > 4)
			throw new IndexOutOfBoundsException("channelCount must be at max 4");
		
		this.selections = new LinkedList<>();
		this.channelCount = channelCount;
		this.size = new Dimension(width, height);
		this.data = new int[size.height][size.width];
	}
	
	/**
	 * Encodes the given value and channel for storing,
	 * while settings the other channels to 0.
	 * 
	 * @param value   Value to encode
	 * @param channel Channel ID of value
	 * @return Encoded value
	 */
	protected int encode(int value, int channel) {
		return (value & 0xFF) << (8 * channel);
	}
	
	/**
	 * Encodes the given value and channel for storing,
	 * while using values from iValue for other channels.
	 * 
	 * @param iValue  Default values for other channels
	 * @param value   Value to encode
	 * @param channel Channel ID of value
	 * @return Encoded value
	 */
	protected int encode(int iValue, int value, int channel) {
		int mask = 0xFF << (8 * channel);
		return (iValue & ~mask) | encode(value, channel);
	}
	
	/**
	 * Decodes the value for the given channel.
	 * 
	 * @param iValue  Encoded values
	 * @param channel Channel ID to retrieve value for
	 * @return Decoded channel value
	 */
	protected int decode(int iValue, int channel) {
		return (iValue >>> (8 * channel)) & 0xFF;
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
		return channelCount;
	}
	
	@Override
	public double[] getValue(int row, int column) {
		int iValue = data[row][column];
		double[] values = new double[channelCount];
		for(int channel = 0; channel < channelCount; channel++)
			values[channel] = decode(iValue, channel);
		return values;
	}
	
	@Override
	public double getValue(int row, int column, int channel) {
		return decode(data[row][column], channel);
	}
	
	@Override
	public void setValue(int row, int column, double... values) {
		if(values.length != channelCount)
			throw new ArrayIndexOutOfBoundsException();
		
		int iValue = 0;
		for(int channel = 0; channel < channelCount; channel++) {
			int value = (int)values[channel];
			value = value < 0 ? 0 : value > 255 ? 255 : value;			
			iValue |= encode(value, channel);
		}
		data[row][column] = iValue;
	}
	
	@Override
	public void setValue(int row, int column, int channel, double value) {
		int val = value < 0 ? 0 : value > 255 ? 255 : (int)value;
		
		int iValue = data[row][column];
		iValue = encode(iValue, val, channel);
		data[row][column] = iValue;
		
		image = null;
	}
	
	@Override
	public Channel getChannel(int channel) {
		return new SimpleChannel(this, channel);
	}

	@Override
	public List<Point> getSelections() {
		return selections;
	}
	
	@Override
	public Object getName() {
		return name;
	}
	
	@Override
	public void setName(Object name) {
		this.name = name;
	}

	@Override
	public BufferedImage asBufferedImage() {
		if(image != null)
			return image;
		
		double[][] samples = new double[3][size.width * size.height]; 
		for(int row = 0; row < size.height; row++) {
			for(int col = 0; col < size.width; col++) {
				int index = row * size.width + col;
				
				double[] rgb = getRGB(row, col);
				for(int c = 0; c < 3; c++)
					samples[c][index] = rgb[c];
			}
		}
		
		image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		
		for(int c = 0; c < 3; c++)
			raster.setSamples(0, 0, size.width, size.height, c, samples[c]);
		
		return image;
	}
	
	@Override
	public Iterator<Channel> iterator() {
		return new Iterator<Channel>() {
			int channel = 0;
			
			@Override
			public boolean hasNext() {
				return channel < channelCount;
			}
			
			@Override
			public Channel next() {
				return getChannel(channel++);
			}
		};
	}
	
	/**
	 * Used for {@link #asBufferedImage()}. The implementing image
	 * should return the RGB value for the given pixel.
	 * 
	 * @param row    Row of the pixel
	 * @param column Column of the pixel
	 * @return RBB value for pixel
	 */
	protected abstract double[] getRGB(int row, int column);

}
