package itb2.image;

import itb2.data.ConversionException;
import itb2.data.PathMap;
import itb2.engine.Controller;
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
	private static final PathMap<Image> map = new PathMap<>(Image.class);;
	
	/** Not instanceable */
	private ImageConverter(){}
	
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
	
}
