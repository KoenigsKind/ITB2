package itb2.utils;

import itb2.image.Image;

public class ImageUtils {
	
	private ImageUtils(){}
	
	public static void scaleLinearly(Image image) { //TODO Use streams
		double min = min(image);
		double max = max(image);
		
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				for(int c = 0; c < image.getChannelCount(); c++) {
					double value = image.getValue(x, y, c);
					value = (value - min) * 255 / max;
					image.setValue(x, y, c, value);
				}
			}
		}
	}
	
	public static double max(Image image) { //TODO Use streams
		double max = Double.MIN_VALUE;
		
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				for(int c = 0; c < image.getChannelCount(); c++) {
					double val = image.getValue(x, y, c);
					max = val > max ? val : max;
				}
			}
		}
	
		return max;
	}
	
	public static double min(Image image) { //TODO Use streams
		double min = Double.MAX_VALUE;
		
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				for(int c = 0; c < image.getChannelCount(); c++) {
					double val = image.getValue(x, y, c);
					min = val < min ? val : min;
				}
			}
		}
	
		return min;
	}

}
