package itb2.image;

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
	 * Scales the given image linearly.
	 * <p>
	 * <strong>Important:</strong><br>
	 * Only works for images that use values 0-255 on all channels. Like {@link RgbImage} and {@link GrayscaleImage}.
	 * 
	 * @param image
	 */
	public static void scaleLinearly(Image image) {
		double min = min(image);
		double max = max(image);
		
		for(Channel channel : image) {
			for(Row row : channel.rows()) {
				for(Cell cell : row) {
					double value = cell.getValue();
					value = (value - min) / (max - min) * 255;
					cell.setValue(value);
				}
			}
		}
	}
	
	/**
	 * Scales the given channel linearly.
	 * 
	 * @param image
	 */
	public static void scaleLinearly(Channel channel) {
		double min = min(channel);
		double max = max(channel);
		
		for(Row row : channel.rows()) {
			for(Cell cell : row) {
				double value = cell.getValue();
				value = (value - min) / (max - min) * 255;
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
		if(colors >= 2) {
			image = ImageFactory.doublePrecision().group(width, height, colors);
			int columns = (int)Math.sqrt(colors);
			int rows = (int)Math.ceil((double) colors / columns);
			for(int col = 0; col < width; col++) {
				for(int row = 0; row < height; row++) {
					int group = (row * rows) / height;
					group *= columns;
					group += (col * columns) / width;
					group %= colors;
					image.setValue(col, row, group);
				}
			}
		} else {
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
		}
		return ImageConverter.convert(image, returnType);
	}

}
