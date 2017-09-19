package itb2.utils;

import itb2.image.Cell;
import itb2.image.Channel;
import itb2.image.GrayscaleImage;
import itb2.image.Image;
import itb2.image.RgbImage;
import itb2.image.Row;

/**
 * Utility functions for images
 * 
 * @author Micha Strauch
 */
public class ImageUtils {
	
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
					value = (value - min) * 255 / max;
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
				value = (value - min) * 255 / max;
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

}
