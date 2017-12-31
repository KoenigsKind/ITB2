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
		register(Image.class, ImageFactory.bytePrecision().drawable(), new Img2Draw());
		
		register(GrayscaleImage.class, ImageFactory.doublePrecision().rgb(), new Gray2Rgb(ImageFactory.doublePrecision()));
		register(GrayscaleImage.class, ImageFactory.doublePrecision().hsi(), new Gray2Hsi(ImageFactory.doublePrecision()));
		register(HsiImage.class, ImageFactory.doublePrecision().gray(), new Hsi2Gray(ImageFactory.doublePrecision()));
		register(DrawableImage.class, ImageFactory.doublePrecision().rgb(), new Draw2Rgb(ImageFactory.doublePrecision()));
		
		register(GrayscaleImage.class, ImageFactory.bytePrecision().rgb(), new Gray2Rgb(ImageFactory.bytePrecision()));
		register(GrayscaleImage.class, ImageFactory.bytePrecision().hsi(), new Gray2Hsi(ImageFactory.bytePrecision()));
		register(HsiImage.class, ImageFactory.bytePrecision().gray(), new Hsi2Gray(ImageFactory.bytePrecision()));
		register(DrawableImage.class, ImageFactory.bytePrecision().rgb(), new Draw2Rgb(ImageFactory.bytePrecision()));
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
	 * Registers a filter for image conversion. The filter must return a single
	 * image of type destination, when receiving a single image of type source.
	 * The <u>source</u> should be the <i>least specific</i> acceptable type, e.g.
	 * {@link RgbImage} is better than {@link ImageFactory#rgb() RgbByteImage}.
	 * While the <u>destination</u> should be the <i>most specific</i> type,
	 * e.g. {@link ImageFactory#gray() GrayscaleByteImage} is better than
	 * {@link GrayscaleImage}.
	 * 
	 * @param source      Least specific image type before conversion
	 * @param destination Most specific image type after conversion
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
			}, converter.getClass().getSimpleName());
		}
	}
	
	/** Returns the current mappings, for debugging purposes. */
	public static String getMappings() {
		synchronized(map) {
			return map.toString();
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
			return ImageFactory.bytePrecision().drawable(input.asBufferedImage());
		}
	}
	
	/**
	 * Converts an image
	 * 
	 * @author Micha Strauch
	 */
	private static abstract class AbstractConverter extends AbstractFilter {
		/** Factory to use for output image */
		final ImageFactory factory;
		
		AbstractConverter(ImageFactory factory) {
			this.factory = factory;
		}
		
		@Override
		public final Image filter(Image input) {
			if(factory != null)
				return filter(input, factory);
			return filter(input, ImageFactory.getPrecision(input));
		}
		
		/** Converts an image using the given factory */
		abstract Image filter(Image input, ImageFactory factory);
	}
	
	/**
	 * Converts a drawable image to an RGB-image
	 * 
	 * @author Micha Strauch
	 */
	private static class Draw2Rgb extends AbstractConverter {
		public Draw2Rgb(ImageFactory factory) {
			super(factory);
		}
		
		@Override
		public Image filter(Image draw, ImageFactory factory) {
			return factory.rgb(draw.asBufferedImage());
		}
	}
	
	/**
	 * Converts a grayscale image into an RGB-image
	 * 
	 * @author Micha Strauch
	 */
	private static class Gray2Rgb extends AbstractConverter {
		public Gray2Rgb(ImageFactory factory) {
			super(factory);
		}
		
		@Override
		public Image filter(Image gray, ImageFactory factory) {
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
	private static class Hsi2Gray extends AbstractConverter {
		public Hsi2Gray(ImageFactory factory) {
			super(factory);
		}
		
		@Override
		public Image filter(Image hsi, ImageFactory factory) {
			return factory.gray(hsi.getChannel(HsiImage.INTENSITY));
		}
	}
	
	/**
	 * Converts a grayscale image into an HSI-image
	 * 
	 * @author Micha Strauch
	 */
	private static class Gray2Hsi extends AbstractConverter {
		public Gray2Hsi(ImageFactory factory) {
			super(factory);
		}
		
		@Override
		public Image filter(Image gray, ImageFactory factory) {
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
