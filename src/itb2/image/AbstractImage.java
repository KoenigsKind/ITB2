package itb2.image;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//TODO !! IMPORTANT !! Make sure height, width, row, column (coordinate system) is consistent

public abstract class AbstractImage implements Image {
	protected final List<Point> selections;
	protected final Dimension size;
	protected final int channelCount;
	protected final double[][][] data;
	protected Object name;
	private BufferedImage image;
	
	public AbstractImage(int width, int height, int channelCount) {
		this(new Dimension(width, height), channelCount);
	}
	
	public AbstractImage(Dimension size, int channelCount) {
		this.selections = new LinkedList<>();
		this.channelCount = channelCount;
		this.size = size;
		this.data = new double[size.height][size.width][channelCount];
	}
	
	public AbstractImage(double[][][] data) {
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
	
	protected abstract double[] getRGB(int row, int column);

}
