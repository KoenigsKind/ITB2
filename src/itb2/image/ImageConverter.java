package itb2.image;

import itb2.data.ConversionException;
import itb2.data.PathMap;
import itb2.engine.Controller;
import itb2.filter.AbstractFilter;
import itb2.filter.Filter;

/**
 * Automatic conversion between two Image types using known {@link Filter filters}.
 * <p>
 * A filter can choose to {@link #register(Class, Class, Filter) register}
 * itself as a converter between two image types. The filter must return a
 * single image of the destination type, when accepting a single image from
 * the source type.
 * <p>
 * A single filter may register itself multiple times for different conversions.
 * 
 * @author Micha Strauch
 */
public final class ImageConverter {
	/** Map of conversions */
	private static final PathMap<Image> map;
	
	/** Not instanceable */
	private ImageConverter(){}
	
	/** Initialize */
	static {
		map = new PathMap<>(Image.class);
		
		// Add some basic conversion methods, may be overwritten by other filter
		register(Image.class, DrawableImage.class, new Img2Draw());
		register(DrawableImage.class, ImageFactory.bytePrecision().rgb(), new Draw2Rgb());
		
		register(ImageFactory.bytePrecision().gray(), ImageFactory.bytePrecision().rgb(), new Gray2Rgb());
		register(ImageFactory.bytePrecision().gray(), ImageFactory.bytePrecision().hsi(), new Gray2Hsi());
		register(ImageFactory.bytePrecision().hsi(), ImageFactory.bytePrecision().gray(), new Hsi2Gray());
		
		register(ImageFactory.doublePrecision().gray(), ImageFactory.doublePrecision().rgb(), new Gray2Rgb());
		register(ImageFactory.doublePrecision().gray(), ImageFactory.doublePrecision().hsi(), new Gray2Hsi());
		register(ImageFactory.doublePrecision().hsi(), ImageFactory.doublePrecision().gray(), new Hsi2Gray());
	}
	
	/**
	 * Converts an image into the given class type.
	 * 
	 * @param image       Image to convert
	 * @param destination Desired image type
	 * 
	 * @return Converted image
	 * 
	 * @throws ConversionException If conversion not successful
	 */
	public static <T extends Image> T convert(Image image, Class<T> destination) throws ConversionException {
		// Just return the image, if the type is matching
		if(destination.isAssignableFrom(image.getClass()))
			return destination.cast(image);
		
		// Try to convert the image
		synchronized(map) {
			return map.convert(image, destination);
		}
	}
	
	/**
	 * Registers a filter for image conversion. The filter
	 * must return a single image of type destination, when
	 * receiving a single image of type source.
	 * 
	 * @param source      Image type before conversion
	 * @param destination Image type after conversion
	 * @param converter   Filter to perform conversion
	 */
	public static void register(Class<? extends Image> source, Class<? extends Image> destination, Filter converter) {
		synchronized (map) {
			map.add(source, destination, image -> {
				Controller.getCommunicationManager().info("Converting image from '%s' to '%s' using '%s'",
						source.getSimpleName(),
						destination.getSimpleName(),
						converter.getClass().getName());
				
				Image[] converted = converter.filter(new Image[]{image});
				if(converted.length != 1)
					throw new ConversionException(String.format("The converter '%s' must return a single image!", converter.getClass().getName()));
				return converted[0];
			});
		}
	}
	
	/**
	 * Converts any image to a drawable image 
	 * 
	 * @author Micha Strauch
	 */
	private static class Img2Draw extends AbstractFilter {
		@Override
		public Image filter(Image input) {
			return new DrawableImage(input.asBufferedImage());
		}
	}
	
	/**
	 * Converts a drawable image to an RGB-image
	 * 
	 * @author Micha Strauch
	 */
	private static class Draw2Rgb extends AbstractFilter {
		@Override
		public Image filter(Image draw) {
			return ImageFactory.bytePrecision().rgb(draw.asBufferedImage());
		}
	}
	
	/**
	 * Converts a grayscale image into an RGB-image
	 * 
	 * @author Micha Strauch
	 */
	private static class Gray2Rgb extends AbstractFilter {
		@Override
		public Image filter(Image gray) {
			ImageFactory factory = ImageFactory.getPrecision(gray);
			RgbImage rgb =  factory.rgb(gray.getSize());
			
			for(int col = 0; col < gray.getWidth(); col++) {
				for(int row = 0; row < gray.getHeight(); row++) {
					double value = gray.getValue(col, row, 0);
					rgb.setValue(col, row, value, value, value);
				}
			}
			
			return rgb;
		}
	}
	
	/**
	 * Converts an HSI-image into a grayscale image
	 * 
	 * @author Micha Strauch
	 */
	private static class Hsi2Gray extends AbstractFilter {
		@Override
		public Image filter(Image hsi) {
			ImageFactory factory = ImageFactory.getPrecision(hsi);
			return factory.gray(hsi.getChannel(HsiImage.INTENSITY));
		}
	}
	
	/**
	 * Converts a grayscale image into an HSI-image
	 * 
	 * @author Micha Strauch
	 */
	private static class Gray2Hsi extends AbstractFilter {
		@Override
		public Image filter(Image gray) {
			ImageFactory factory = ImageFactory.getPrecision(gray);
			Image hsi = factory.hsi(gray.getSize());
			double hue = 0, saturation = 0;
			
			for(int col = 0; col < gray.getWidth(); col++) {
				for(int row = 0; row < gray.getHeight(); row++) {
					double intensity = gray.getValue(col, row, 0);
					hsi.setValue(col, row, hue, saturation, intensity);
				}
			}
			
			return hsi;
		}
	}
	
}
