package itb2.image;

import java.util.function.BiFunction;
import java.util.function.Function;

import itb2.data.ConversionException;
import itb2.engine.CommunicationManager;

/**
 * Utility functions for images
 * 
 * @author Micha Strauch
 */
public class ImageUtils {
	
	/**
	 * Reads all values from the given image and writes them into the given
	 * double matrix. If null is given, a double matrix will be created.
	 * The matrix must be of the size double[channelCount][width][height]
	 *  
	 * @param image  Image to get values from
	 * @param values Matrix to write values into (or null)
	 * @return Matrix with values
	 */
	public static double[][][] getValues(Image image, double[][][] values) {
		if(values == null)
			values = new double[image.getChannelCount()][][];
		
		for(int chan = 0; chan < image.getChannelCount(); chan++)
			values[chan] = getValues(image.getChannel(chan), values[chan]);
		
		return values;
	}
	
	/**
	 * Reads all values from the given channel and writes them into the given
	 * double matrix. If null is given, a double matrix will be created.
	 * The matrix must be of the size double[width][height]
	 *  
	 * @param image  Channel to get values from
	 * @param values Matrix to write values into (or null)
	 * @return Matrix with values
	 */
	public static double[][] getValues(Channel channel, double[][] values) {
		if(values == null)
			values = new double[channel.getWidth()][channel.getHeight()];
		
		for(int col = 0; col < channel.getWidth(); col++)
			for(int row = 0; row < channel.getHeight(); row++)
					values[col][row] = channel.getValue(col, row);
		
		return values;
	}
	
	/**
	 * Fills the image with the values from the given matrix.
	 * The matrix must be of the size double[channelCount][width][height].
	 * 
	 * @param image  Image to set values for
	 * @param values Values to set
	 */
	public static void setValues(Image image, double[][][] values) {
		for(int chan = 0; chan < image.getChannelCount(); chan++)
			setValues(image.getChannel(chan), values[chan]);
	}
	
	/**
	 * Fills the channel with the values from the given matrix.
	 * The matrix must be of the size double[width][height].
	 * 
	 * @param image  Channel to set values for
	 * @param values Values to set
	 */
	public static void setValues(Channel channel, double[][] values) {
		for(int col = 0; col < channel.getWidth(); col++)
			for(int row = 0; row < channel.getHeight(); row++)
				channel.setValue(col, row, values[col][row]);
	}
	
	/**
	 * Copies all values from one image to the other. The <i>to</i>
	 * image must be at least as big as the <i>from</i> image.
	 * 
	 * @param from Image to copy values from
	 * @param to   Image to copy values to
	 * 
	 * @throws ArrayIndexOutOfBoundsException If <i>to</i> image is too small
	 */
	public static void copy(Image from, Image to) throws ArrayIndexOutOfBoundsException {
		if(to.getChannelCount() < from.getChannelCount())
			throw new ArrayIndexOutOfBoundsException();
		
		for(int channel = 0; channel < from.getChannelCount(); channel++)
			copy(from.getChannel(channel), to.getChannel(channel));
	}
	
	/**
	 * Copies all values from one channel to the other. The <i>to</i>
	 * channel must be at least as big as the <i>from</i> channel.
	 * 
	 * @param from Channel to copy values from
	 * @param to   Channel to copy values to
	 * 
	 * @throws ArrayIndexOutOfBoundsException If <i>to</i> channel is too small
	 */
	public static void copy(Channel from, Channel to) throws ArrayIndexOutOfBoundsException {
		if(to.getWidth() < from.getWidth() || to.getHeight() < from.getHeight())
			throw new ArrayIndexOutOfBoundsException();
		
		for(int col = 0; col < from.getWidth(); col++) {
			for(int row = 0; row < from.getHeight(); row++) {
				double value = from.getValue(col, row);
				to.setValue(col, row, value);
			}
		}
	}
	
	/**
	 * Scales the given image linearly.
	 * The new values will be between 0 and 255.
	 * 
	 * @param image
	 */
	public static void scaleLinearly(Image image) {
		double min = min(image);
		double max = max(image);
		
		scaleLinearly(image, min, max, 0, 255);
	}
	
	/**
	 * Scales the given image linearly.
	 * 
	 * @param image  Image to scale
	 * @param oldMin Old minimum value
	 * @param oldMax Old maximum value
	 * @param newMin New minimum value
	 * @param newMax New maximum value
	 */
	public static void scaleLinearly(Image image, double oldMin, double oldMax, double newMin, double newMax) {
		for(Channel channel : image)
			scaleLinearly(channel, oldMin, oldMax, newMin, newMax);
	}
	
	/**
	 * Scales the given channel linearly.
	 * The new values will be between 0 and 255.
	 * 
	 * @param channel Channel to scale
	 */
	public static void scaleLinearly(Channel channel) {
		double min = min(channel);
		double max = max(channel);
		
		scaleLinearly(channel, min, max, 0, 255);
	}
	
	/**
	 * Scales the given channel linearly.
	 * 
	 * @param channel Channel to scale
	 * @param oldMin  Old minimum value
	 * @param oldMax  Old maximum value
	 * @param newMin  New minimum value
	 * @param newMax  New maximum value
	 */
	public static void scaleLinearly(Channel channel, double oldMin, double oldMax, double newMin, double newMax) {
		for(Row row : channel.rows()) {
			for(Cell cell : row) {
				double value = cell.getValue();
				value = (value - oldMin) / (oldMax - oldMin) * (newMax - newMin) + newMin;
				cell.setValue(value);
			}
		}
	}
	
	/**
	 * Fills the channel with the values given by the provider.
	 * The provider is called for each cell with the first argument
	 * being the cell's column and the second arguemtn being the
	 * cell's row.
	 * 
	 * @param channel  Channel to fill with values
	 * @param provider Function providing values to fill into the image
	 */
	public static void fill(Channel channel, BiFunction<Integer, Integer, Double> provider) {
		for(int col = 0; col < channel.getWidth(); col++)
			for(int row = 0; row < channel.getHeight(); row++)
				channel.setValue(col, row, provider.apply(col, row));
	}
	
	/**
	 * Calls the replacer for each cell, replacing its value with the
	 * value returned by the replacer. The replacer is called for each
	 * cell with the argument being the current value.
	 * 
	 * @param channel  Channel to replace values in
	 * @param replacer Replacer returning the new value for each given value
	 */
	public static void replace(Channel channel, Function<Double, Double> replacer) {
		for(Row row : channel.rows()) {
			for(Cell cell : row) {
				double value = cell.getValue();
				value = replacer.apply(value);
				cell.setValue(value);
			}
		}
	}
	
	/**
	 * Returns the maximum value in this image.
	 * 
	 * @param image Image to find maximum value for.
	 * @return Maximum value
	 */
	public static double max(Image image) {
		double max = Double.MIN_VALUE;
		
		for(Channel channel : image) {
			double val = max(channel);
			max = val > max ? val : max;
		}
	
		return max;
	}
	
	/**
	 * Returns the maximum value in this channel.
	 * 
	 * @param channel Channel to find maximum value for.
	 * @return Maximum value
	 */
	public static double max(Channel channel) {
		double max = Double.MIN_VALUE;
		
		for(Row row : channel.rows()) {
			for(Cell cell : row) {
				double val = cell.getValue();
				max = val > max ? val : max;
			}
		}
	
		return max;
	}
	
	/**
	 * Returns the minimum value in this image.
	 * 
	 * @param image Image to find minimum value for.
	 * @return Minimum value
	 */
	public static double min(Image image) {
		double min = Double.MAX_VALUE;
		
		for(Channel channel : image) {
			double val = min(channel);
			min = val < min ? val : min;
		}
	
		return min;
	}
	
	/**
	 * Returns the minimum value in this channel.
	 * 
	 * @param channel Channel to find minimum value for.
	 * @return Minimum value
	 */
	public static double min(Channel channel) {
		double min = Double.MAX_VALUE;
		
		for(Row row : channel.rows()) {
			for(Cell cell : row) {
				double val = cell.getValue();
				min = val < min ? val : min;
			}
		}
	
		return min;
	}
	
	/**
	 * Returns an image containing multiple colors. This can be used with
	 * {@link CommunicationManager#getSelections(String, int, Image) Controller.getCommunicationManager.getSelections(...)}
	 * to let the user select a color.
	 * 
	 * @param width      Width of the image
	 * @param height     Height of the image
	 * @param colors     Number of colors (or 0 for full range)
	 * @param returnType ImageType to return
	 * 
	 * @return Image containing different colors
	 * 
	 * @throws ConversionException If it's not possible to convert to the requested image type
	 */
	public static <T extends Image> T colorPick(int width, int height, int colors, Class<T> returnType) throws ConversionException {
		Image image;
		if(colors == 0) {
			image = ImageFactory.doublePrecision().hsv(width, height);
			double h = ((HsvImage)image).maxHue();
			double s = ((HsvImage)image).maxSaturation();
			double v = ((HsvImage)image).maxValue();
			
			for(int col = 0; col < width; col++) {
				for(int row = 0; row < height/2; row++)
					image.setValue(col, row, col * h / width, s, 2 * row * v / height);
				for(int row = height/2; row < height; row++)
					image.setValue(col, row, col * h / width, 2 * (height - row) * s / height, v);
			}
		} else {
			image = ImageFactory.doublePrecision().group(width, height, colors);
			int columns = (int)Math.sqrt(colors);
			int rows = (int)Math.ceil((double) colors / columns);
			for(int col = 0; col < width; col++) {
				for(int row = 0; row < height; row++) {
					int group = (row * rows) / height;
					group *= columns;
					group += (col * columns) / width;
					group %= colors;
					image.setValue(col, row, group + 1);
				}
			}
		}
		return ImageConverter.convert(image, returnType);
	}

}
