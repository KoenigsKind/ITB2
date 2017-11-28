package itb2.image;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * RGB-Image that can be drawn to, using a Graphics Object.
 * 
 * @author Micha Strauch
 */
public class DrawableImage implements Image {
	
	/** IDs for each channel */
	public static final int RED = 0, GREEN = 1, BLUE = 2;
	
	/** List of selections */
	private final List<Point> selections;
	
	/** Data of this image */
	private final BufferedImage image;

	/** Name of this image */
	private Object name;
	
	/**
	 * Constructs an image from the given image
	 * 
	 * @param image Original image
	 */
	public DrawableImage(Image image) {
		this(image.asBufferedImage());
	}
	
	/**
	 * Constructs an image from the given image
	 * 
	 * @param image Original image
	 */
	public DrawableImage(BufferedImage image) {
		this(image.getWidth(), image.getHeight());
		this.image.getGraphics().drawImage(image, 0, 0, null);
	}
	
	/**
	 * Constructs an image with given size
	 * 
	 * @param size Size of this image
	 */
	public DrawableImage(Dimension size) {
		this(size.width, size.height);
	}
	
	/**
	 * Constructs an image with given size
	 * 
	 * @param width        Width of this image
	 * @param height       Height of this image
	 */
	public DrawableImage(int width, int height) {
		selections = new LinkedList<>();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	/**
	 * Returns the {@link Graphics} object for drawing.
	 * 
	 * @return Graphics object
	 */
	public Graphics getGraphics() {
		return image.getGraphics();
	}

	@Override
	public int getWidth() {
		return image.getWidth();
	}

	@Override
	public int getHeight() {
		return image.getHeight();
	}

	@Override
	public Dimension getSize() {
		return new Dimension(image.getWidth(), image.getHeight());
	}

	@Override
	public int getChannelCount() {
		return 3;
	}

	@Override
	public double[] getValue(int column, int row) {
		return image.getRaster().getPixel(column, row, new double[3]);
	}

	@Override
	public double getValue(int column, int row, int channel) {
		return getValue(column, row)[channel];
	}

	@Override
	public void setValue(int column, int row, double... values) {
		image.getRaster().setPixel(column, row, values);
	}

	@Override
	public void setValue(int column, int row, int channel, double value) {
		double[] rgb = getValue(column, row);
		rgb[channel] = value;
		setValue(column, row, rgb);
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
		return image;
	}
	
	@Override
	public Iterator<Channel> iterator() {
		return new Iterator<Channel>() {
			int channel = 0;
			
			@Override
			public boolean hasNext() {
				return channel < 3;
			}

			@Override
			public Channel next() {
				return getChannel(channel++);
			}
			
		};
	}

}
