package itb2.image.doubleprecision;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
	
	/** List of selections */
	protected final List<Point> selections;
	
	/** Size of this image */
	protected final Dimension size;
	
	/** Number of channels */
	protected final int channelCount;
	
	/** Data of this image <i>double[width][height][3]</i> */
	protected final double[][][] data;
	
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
		this.selections = new LinkedList<>();
		this.channelCount = channelCount;
		this.size = new Dimension(width, height);
		this.data = new double[height][width][channelCount];
	}
	
	/**
	 * Constructs an image using the given data
	 * 
	 * @param data Data of this image
	 */
	public AbstractDoubleImage(double[][][] data) {
		this.selections = new LinkedList<Point>();
		this.data = data;
		
		this.size = new Dimension(data[0].length, data.length);
		this.channelCount = data[0][0].length;
		
		for(int x = 0; x < size.height; x++)
			for(int y = 0; y < size.width; y++)
				if(data[x][y].length != channelCount)
					throw new ArrayIndexOutOfBoundsException(); // fail fast
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
		return Arrays.copyOf(data[row][column], channelCount);
	}
	
	@Override
	public double getValue(int row, int column, int channel) {
		return data[row][column][channel];
	}
	
	@Override
	public void setValue(int row, int column, double... values) {
		if(values.length != channelCount)
			throw new ArrayIndexOutOfBoundsException();
		
		for(int channel = 0; channel < channelCount; channel++)
			data[row][column][channel] = values[channel];
		image = null;
	}
	
	@Override
	public void setValue(int row, int column, int channel, double value) {
		data[row][column][channel] = value;
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
