package itb2.image.byteprecision;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;

import itb2.engine.io.AnymapIO;
import itb2.image.Channel;
import itb2.image.DrawableImage;
import itb2.image.RgbImage;
import itb2.image.SimpleChannel;

/**
 * RGB-Image that can be drawn to, using a Graphics Object.
 * 
 * @author Micha Strauch
 */
class DrawableByteImage implements RgbImage, DrawableImage {
	private static final long serialVersionUID = -8586173019718312748L;
	
	/** Data of this image */
	private BufferedImage image;

	/** Name of this image */
	private Serializable name;
	
	/**
	 * Constructs an image from the given image
	 * 
	 * @param image Original image
	 */
	DrawableByteImage(BufferedImage image) {
		this(image.getWidth(), image.getHeight());
		getGraphics().drawImage(image, 0, 0, null);
	}
	
	/**
	 * Constructs an image with given size
	 * 
	 * @param size Size of this image
	 */
	DrawableByteImage(Dimension size) {
		this(size.width, size.height);
	}
	
	/**
	 * Constructs an image with given size
	 * 
	 * @param width        Width of this image
	 * @param height       Height of this image
	 */
	DrawableByteImage(int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	/**
	 * Writes the BufferedImage to the given OutputStream for serialization
	 * 
	 * @param stream OutputStream to write to
	 * 
	 * @throws IOException If something goes wrong
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.writeObject(name);
		AnymapIO.save(this, stream);
	}
	
	/**
	 * Reads the BufferedImage from the given InputStream for deserialization
	 * 
	 * @param stream InputStream to read from
	 * 
	 * @throws IOException If something goes wrong
	 */
	private void readObject(ObjectInputStream stream) throws IOException {
		try {
			
			name = (Serializable) stream.readObject();
			image = AnymapIO.load(stream).asBufferedImage();
			
		} catch (ClassNotFoundException e) {
			throw new IOException("Could not deserialize " + getClass().getSimpleName());
		}
	}
	
	@Override
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
		return image.getRaster().getSample(column, row, channel);
	}

	@Override
	public void setValue(int column, int row, double... values) {
		image.getRaster().setPixel(column, row, values);
	}

	@Override
	public void setValue(int column, int row, int channel, double value) {
		image.getRaster().setSample(column, row, channel, value);
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
