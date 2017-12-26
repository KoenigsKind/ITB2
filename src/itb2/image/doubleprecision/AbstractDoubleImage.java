package itb2.image.doubleprecision;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

import itb2.image.Channel;
import itb2.image.Image;
import itb2.image.SimpleChannel;

/**
 * Abstract image, for image implementations that can store
 * a double value for each channel of each pixel.
 * 
 * @author Micha Strauch
 */
public abstract class AbstractDoubleImage implements Image {
	private static final long serialVersionUID = 7443470161814281566L;

	/** Size of this image */
	protected final Dimension size;
	
	/** Number of channels */
	protected final int channelCount;
	
	/** Data of this image <br><code>double[channelCount][width][height]</code> */
	protected final double[][][] data;
	
	/** Name of this image */
	protected Serializable name;
	
	/** Last rendered state of this image */
	private transient BufferedImage image;
	
	/**
	 * Constructs an image with given size and channel count
	 * 
	 * @param size         Size of this image
	 * @param channelCount Number of channels
	 */
	public AbstractDoubleImage(Dimension size, int channelCount) {
		this(size.width, size.height, channelCount);
	}
	
	/**
	 * Constructs an image with given size and channel count
	 * 
	 * @param width        Width of this image
	 * @param height       Height of this image
	 * @param channelCount Number of channels
	 */
	public AbstractDoubleImage(int width, int height, int channelCount) {
		this.channelCount = channelCount;
		this.size = new Dimension(width, height);
		this.data = new double[channelCount][width][height];
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
	public double[] getValue(int column, int row) {
		double[] value = new double[channelCount];
		for(int c = 0; c < channelCount; c++)
			value[c] = data[c][column][row];
		return value;
	}
	
	@Override
	public double getValue(int column, int row, int channel) {
		return data[channel][column][row];
	}
	
	@Override
	public void setValue(int column, int row, double... values) {
		if(values.length != channelCount)
			throw new ArrayIndexOutOfBoundsException();
		
		for(int channel = 0; channel < channelCount; channel++)
			data[channel][column][row] = values[channel];
		image = null;
	}
	
	@Override
	public void setValue(int column, int row, int channel, double value) {
		data[channel][column][row] = value;
		image = null;
	}
	
	@Override
	public Channel getChannel(int channel) {
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
		if(image != null)
			return image;
		
		double[][] samples = new double[3][size.width * size.height]; 
		for(int col = 0; col < size.width; col++) {
			for(int row = 0; row < size.height; row++) {
				int index = row * size.width + col;
				
				double[] rgb = getRGB(col, row);
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
	 * Calling the function indicates, that the image has changed and the
	 * {@link BufferedImage} must be redrawn. Must be called by subclasses
	 * after directly changing values in {@link #data}. 
	 */
	protected void updateImage() {
		image = null;
	}
	
	/**
	 * Used for {@link #asBufferedImage()}. The implementing image
	 * should return the RGB value for the given pixel.
	 * 
	 * @param column Column of the pixel
	 * @param row    Row of the pixel
	 * @return RGB value for pixel
	 */
	protected abstract double[] getRGB(int column, int row);

}
