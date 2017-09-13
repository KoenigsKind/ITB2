package itb2.image;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//TODO !! IMPORTANT !! Make sure height, width, row, column (coordinate system) is consistent

public abstract class AbstractByteImage implements Image {
	protected final List<Point> selections;
	protected final Dimension size;
	protected final int channelCount;
	protected final int[][] data;
	protected Object name;
	private BufferedImage image;
	
	public AbstractByteImage(int width, int height, int channelCount) {
		this(new Dimension(width, height), channelCount);
	}
	
	public AbstractByteImage(Dimension size, int channelCount) {
		if(channelCount > 4)
			throw new IndexOutOfBoundsException("channelCount must be at max 4");
		
		this.selections = new LinkedList<>();
		this.channelCount = channelCount;
		this.size = size;
		this.data = new int[size.height][size.width];
	}
	
	protected int encode(int value, int channel) {
		return (value & 0xFF) << (8 * channel);
	}
	
	protected int encode(int iValue, int value, int channel) {
		int mask = 0xFF << (8 * channel);
		return (iValue & ~mask) | encode(value, channel);
	}
	
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
	
	protected abstract double[] getRGB(int row, int column);

}
