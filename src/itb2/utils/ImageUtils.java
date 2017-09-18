package itb2.utils;

import itb2.image.Image;

public class ImageUtils {
	
	private ImageUtils(){}
	
	public static void scaleLinearly(Image image) { //TODO Use streams
		double min = min(image);
		double max = max(image);
		
		for(int row = 0; row < image.getHeight(); row++) {
			for(int col = 0; col < image.getWidth(); col++) {
				for(int c = 0; c < image.getChannelCount(); c++) {
					double value = image.getValue(row, col, c);
					value = (value - min) * 255 / max;
					image.setValue(row, col, c, value);
				}
			}
		}
	}
	
	public static double max(Image image) { //TODO Use streams
		double max = Double.MIN_VALUE;
		
		for(int row = 0; row < image.getHeight(); row++) {
			for(int col = 0; col < image.getWidth(); col++) {
				for(int c = 0; c < image.getChannelCount(); c++) {
					double val = image.getValue(row, col, c);
					max = val > max ? val : max;
				}
			}
		}
	
		return max;
	}
	
	public static double min(Image image) { //TODO Use streams
		double min = Double.MAX_VALUE;
		
		for(int row = 0; row < image.getHeight(); row++) {
			for(int col = 0; col < image.getWidth(); col++) {
				for(int c = 0; c < image.getChannelCount(); c++) {
					double val = image.getValue(row, col, c);
					min = val < min ? val : min;
				}
			}
		}
	
		return min;
	}

}
