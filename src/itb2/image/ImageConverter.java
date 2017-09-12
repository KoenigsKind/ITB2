package itb2.image;

import itb2.filter.AbstractFilter;
import itb2.filter.Filter;

public final class ImageConverter {
	private ImageConverter(){}
	
	static { // Add some basic conversion methods
		register(GrayscaleImage.class, RgbImage.class, new Gray2Rgb());
		register(GrayscaleImage.class, HsiImage.class, new Gray2Hsi());
		register(HsiImage.class, GrayscaleImage.class, new Hsi2Gray());
	}
	
	public static <T extends Image> T convert(Image image, Class<T> destination) {
		if(destination.isAssignableFrom(image.getClass()))
			return destination.cast(image);
		
		throw new UnsupportedOperationException("Not implemented yet"); //TODO Implement ImageConverter
	}
	
	public static void register(Class<? extends Image> source, Class<? extends Image> destination, Filter converter) {
		//TODO Implement ImageConverter
	}
	
	private static class Gray2Rgb extends AbstractFilter {
		@Override
		public Image filter(Image gray) {
			RgbImage rgb = new RgbImage(gray.getSize());
			
			for(int x = 0; x < gray.getWidth(); x++) {
				for(int y = 0; y < gray.getHeight(); y++) {
					double value = gray.getValue(x, y, 0);
					rgb.setValue(x, y, value, value, value);
				}
			}
			
			return rgb;
		}
	}
	
	private static class Hsi2Gray extends AbstractFilter {
		@Override
		public Image filter(Image hsi) {
			return new GrayscaleImage(hsi.getChannel(HsiImage.INTENSITY));
		}
	}
	
	private static class Gray2Hsi extends AbstractFilter {
		@Override
		public Image filter(Image gray) {
			Image hsi = new HsiImage(gray.getSize());
			double hue = 0, saturation = 0;
			
			for(int x = 0; x < gray.getWidth(); x++) {
				for(int y = 0; y < gray.getHeight(); y++) {
					double intensity = gray.getValue(x, y, 0);
					hsi.setValue(x, y, hue, saturation, intensity);
				}
			}
			
			return hsi;
		}
	}
	
}
