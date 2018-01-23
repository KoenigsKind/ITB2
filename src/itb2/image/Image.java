package itb2.image;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.function.Function;

/**
 * Interface for images.<br>
 * The coordinate system has it's point of origin in the top
 * left corner. The rows are counting from top to bottom, and
 * the columns are counting from left to right. The order of
 * the channels depends on the implementation of this interface.
 * 
 * @author Micha Strauch
 */
public interface Image extends Iterable<Channel>, Serializable {
	
	/**
	 * Returns the width of this image
	 * 
	 * @return width of this image
	 */
	public int getWidth();
	
	/**
	 * Returns the height of this image
	 * 
	 * @return height of this image
	 */
	public int getHeight();
	
	/**
	 * Returns the size of this image
	 * 
	 * @return size of this image
	 */
	public Dimension getSize();
	
	/**
	 * Returns the number of channels of this image
	 * 
	 * @return Number of channels
	 */
	public int getChannelCount();
	
	/**
	 * Returns the values of the given pixel. 
	 * 
	 * @param column Column of the pixel
	 * @param row    Row of the pixel
	 * 
	 * @return Array of values
	 */
	public double[] getValue(int column, int row);
	
	/**
	 * Returns the value of the pixel in the given channel.
	 * 
	 * @param column  Column of the pixel
	 * @param row     Row of the pixel
	 * @param channel Channel ID
	 * 
	 * @return Value
	 */
	public double getValue(int column, int row, int channel);
	
	/**
	 * Sets the values for the given pixel. The number of values must match the number of channels.
	 * Keep in mind, that the underlaying image, might not store double values.
	 *  
	 * @param column Column of the pixel
	 * @param row    Row of the pixel
	 * @param values Value for each channel
	 */
	public void setValue(int column, int row, double... values);
	
	/**
	 * Sets the value of the given pixel in the given channel.
	 * Keep in mind, that the underlaying image, might not store double values.
	 * 
	 * @param column  Column of the pixel
	 * @param row     Row of the pixel
	 * @param channel Channel to set value for
	 * @param value   Value to set
	 */
	public void setValue(int column, int row, int channel, double value);
	
	/**
	 * Returns the channel with the given ID.
	 * 
	 * @param channel Channel ID
	 * @return Channel of the given ID
	 */
	public Channel getChannel(int channel);
	
	/**
	 * Returns the name of this image.
	 * 
	 * @return Name of image
	 */
	public Object getName();
	
	/**
	 * Sets the name of this image.
	 * 
	 * @param name Name of image
	 */
	public void setName(Serializable name);
	
	/**
	 * Creates a {@link BufferedImage} of this image. This function will
	 * be used for displaying the image on the GUI and storing it to disk. 
	 * 
	 * @return This image as a BufferedImage
	 */
	public BufferedImage asBufferedImage();
	
	/**
	 * Modifies the value using the given modifier function.<p>
	 * For example, increase current value by 3:<br>
	 * <code>modifyValue(col, row, chan, value -> value + 3);</code>
	 * 
	 * @param column   Column of the pixel
	 * @param row      Row of the pixel
	 * @param channel  Channel to modify value for
	 * @param modifier Function providing new value
	 */
	default void modifyValue(int column, int row, int channel, Function<Double, Double> modifier) {
		double value = getValue(column, row, channel);
		value = modifier.apply(value);
		setValue(column, row, channel, value);
	}
	
}
