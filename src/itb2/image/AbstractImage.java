package itb2.image;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
		this.data = new double[size.width][size.height][channelCount];
	}
	
	public AbstractImage(double[][][] data) {
		this.selections = new LinkedList<Point>();
		this.data = data;
		
		this.size = new Dimension(data.length, data[0].length);
		this.channelCount = data[0][0].length;
		
		for(int x = 0; x < size.width; x++)
			for(int y = 0; y < size.height; y++)
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
	public double[] getValue(int x, int y) {
		return data[x][y];
	}
	
	@Override
	public double getValue(int x, int y, int channel) {
		return data[x][y][channel];
	}
	
	@Override
	public void setValue(int x, int y, double... values) {
		if(values.length != channelCount)
			throw new ArrayIndexOutOfBoundsException();
		
		data[x][y] = values;
		image = null;
	}
	
	@Override
	public void setValue(int x, int y, int channel, double value) {
		data[x][y][channel] = value;
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
		for(int x = 0; x < size.width; x++) {
			for(int y = 0; y < size.height; y++) {
				int index = x + size.width * y;
				
				double[] rgb = getRGB(x, y);
				for(int k = 0; k < 3; k++)
					samples[k][index] = rgb[k];
			}
		}
		
		image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		
		for(int k = 0; k < 3; k++)
			raster.setSamples(0, 0, size.width, size.height, k, samples[k]);
		
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
	
	protected abstract double[] getRGB(int x, int y);

}
