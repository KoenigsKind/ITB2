package itb2.image;

import java.util.LinkedList;
import java.util.List;

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
	private static final PathMap map;
	
	/** Not instanceable */
	private ImageConverter(){}
	
	/** Initialize */
	static {
		map = new PathMap(5);
		
		// Add some basic conversion methods, may be overwritten by other filter
		register(Image.class, DrawableImage.class, new Img2Draw());
		register(DrawableImage.class, RgbImage.class, new Draw2Rgb());
		register(GrayscaleImage.class, RgbImage.class, new Gray2Rgb());
		register(GrayscaleImage.class, HsiImage.class, new Gray2Hsi());
		register(HsiImage.class, GrayscaleImage.class, new Hsi2Gray());
	}
	
	/**
	 * Converts an image into the given class type.
	 * 
	 * @param image       Image to convert
	 * @param destination Desired image type
	 * @return Converted image
	 * @throws ConversionException If conversion not successful
	 */
	public static <T extends Image> T convert(Image image, Class<T> destination) throws ConversionException {
		// Just return the image, if the type is matching
		if(destination.isAssignableFrom(image.getClass()))
			return destination.cast(image);
		
		// Try to convert the image
		@SuppressWarnings("unchecked")
		T converted =  (T) map.convert(image, destination);
		return converted;
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
		map.add(source, destination, converter);
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
	
	/**
	 * Conversion-Path
	 * <p>
	 * For example, if no converter from HSI to RGB is registered, but
	 * there is a converter HSI to grayscale and one converter grayscale
	 * to RGB; the conversion path will be HSI &rarr; grayscale &rarr; RGB.
	 * 
	 * @author Micha Strauch
	 */
	private static class Path {
		/** Previous as posterior path */
		private final Path prev, post;
		/** Converter to call */
		private final Filter converter;
		
		/**
		 * Creates a path with only this converter in it.
		 * 
		 * @param converter Converter to call
		 */
		public Path(Filter converter) {
			this(null, converter, null);
		}
		
		/**
		 * Creates a path, with calling prev before converter
		 * 
		 * @param prev      Path to execute before calling converter
		 * @param converter Converter to call
		 */
		public Path(Path prev, Filter converter) {
			this(prev, converter, null);
		}
		
		/**
		 * Creates a path, with calling post after converter
		 * 
		 * @param converter Converter to call
		 * @param post      Path to execute after calling converter
		 */
		public Path(Filter converter, Path post) {
			this(null, converter, post);
		}
		
		/**
		 * Creates a full path, with calling prev before and post after converter
		 * 
		 * @param prev      Path to execute before calling converter
		 * @param converter Converter to call
		 * @param post      Path to execute after calling converter
		 */
		public Path(Path prev, Filter converter, Path post) {
			this.prev = prev;
			this.converter = converter;
			this.post = post;
		}
		
		/**
		 * Converts the given image
		 * 
		 * @param input Image to convert
		 * @return Converted image
		 * @throws ConversionException If conversion is not successful
		 */
		public Image convert(Image input) throws ConversionException {
			if(prev != null)
				input = prev.convert(input);
			
			Controller.getCommunicationManager().info("Converting image using '%s'", converter.getClass().getName());
			Image[] converted = converter.filter(new Image[]{input});
			if(converted.length != 1)
				throw new ConversionException(String.format("The converter '%s' must return a single image!", converter.getClass().getName()));
			input = converted[0];
			
			if(post != null)
				input = post.convert(input);
			
			return input;
		}
		
		/**
		 * Number of converters in this path
		 * 
		 * @return Converter count
		 */
		public int length() {
			int length = 1;
			if(prev != null)
				length += prev.length();
			if(post != null)
				length += post.length();
			return length;
		}
		
		@Override
		public String toString() {
			String output = "";
			if(prev != null)
				output += prev + ", ";
			output = converter.getClass().getSimpleName();
			if(post != null)
				output += ", " + post;
			return "<" + output + ">";
		}
	}
	
	/**
	 * Conversion map
	 * <p>
	 * Map keeping track of how to get from one image type to another.
	 * 
	 * @author Micha Strauch
	 */
	private static class PathMap {
		
		/** Indices for image types in map */
		private final List<Class<? extends Image>> order;
		
		/**
		 * Map with conversion paths.
		 * <p>
		 * {@code map[src][dst]} contains the path to get from
		 * image type {@code src} to image type {@code dst}
		 */ 
		private Path[][] map;
		
		/**
		 * Initializes the map with space for enough space for {@code initialSize} elements.
		 * 
		 * @param initialSize Number of image types to have space for.
		 */
		public PathMap(int initialSize) {
			order = new LinkedList<>();
			map = new Path[initialSize][initialSize];
		}
		
		/**
		 * Converts the given image into the requested image type 
		 * 
		 * @param image       Image to convert
		 * @param destination Requested image type
		 * @return Converted image
		 * 
		 * @throws ConversionException If conversion unsuccessful
		 */
		public Image convert(Image image, Class<? extends Image> destination) throws ConversionException {
			
			// First check for exact conversion
			int convSrc = order.indexOf(image.getClass());
			int convDst = order.indexOf(destination);
			
			if(convSrc >= 0 && convDst >= 0 && map[convSrc][convDst] != null)
				return map[convSrc][convDst].convert(image);
			
			// Check for conversion using sub and super types
			for(int src = 0; src < order.size(); src++) {
				if( !order.get(src).isInstance(image) )
					continue;
				
				for(int dst = 0; dst < order.size(); dst++) {
					if( !destination.isAssignableFrom(order.get(dst)) )
						continue;
					
					if(map[src][dst] != null)
						return map[src][dst].convert(image);
				}
			}
			
			throw new ConversionException("No conversion found");
		}
		
		/**
		 * Returns the index of the given class in the map.
		 * If the class does not exist yet, an index is generated. 
		 * 
		 * @param clazz Class to get index of
		 * @return Index of class
		 */
		private int indexOf(Class<? extends Image> clazz) {
			int index = order.indexOf(clazz);
			if(index >= 0)
				return index;
			order.add(clazz);
			if(map.length < order.size())
				resize();
			return order.size() - 1;
		}
		
		/** Expands the map to fit more classes in it */
		private void resize() {
			Path[][] oldMap = map;
			map = new Path[oldMap.length + 2][oldMap.length + 2];
			
			for(int i = 0; i < oldMap.length; i++)
				for(int j = 0; j < oldMap.length; j++)
					map[i][j] = oldMap[i][j];
		}
		
		/**
		 * Adds a conversion to the map.
		 * 
		 * @param source      Conversion source
		 * @param destination Conversion destination
		 * @param converter   Filter to perform conversion from source type to destination type
		 */
		public void add(Class<? extends Image> source, Class<? extends Image> destination, Filter converter) {
			int convSrc = indexOf(source), convDst = indexOf(destination);
			map[convSrc][convDst] = new Path(converter);
			
			for(int src = 0; src < order.size(); src++) {
				if(src == convDst || map[src][convSrc] == null)
					continue;
				
				Path path = new Path(map[src][convSrc], converter);
				if(map[src][convDst] == null || map[src][convDst].length() >= path.length()) {
					map[src][convDst] = path;
				}
			}
			
			for(int dst = 0; dst < order.size(); dst++) {
				if(dst == convSrc || map[convDst][dst] == null)
					continue;
				
				Path path = new Path(converter, map[convDst][dst]);
				if(map[convSrc][dst] == null || map[convSrc][dst].length() >= path.length()) {
					map[convSrc][dst] = path;
					
					for(int src = 0; src < order.size(); src++) {
						if(src == dst || map[src][convSrc] == null)
							continue;
						
						path = new Path(map[src][convSrc], converter, map[convDst][dst]);
						if(map[src][dst] == null || map[src][dst].length() >= path.length()) {
							map[src][dst] = path;
						}
					}
				}
			}
		}
		
		@Override
		public String toString() {
			String[][] sMap = new String[map.length][map.length];
			int width[] = new int[map.length];
			
			for(int i = 0; i < map.length; i++) {
				for(int j = 0; j < map.length; j++) {
					sMap[i][j] = i == j ? "--" : map[i][j] == null ? "<>" : map[i][j].toString();
					
					if(sMap[i][j].length() > width[j])
						width[j] = sMap[i][j].length();
				}
			}
			
			StringBuilder output = new StringBuilder();
			for(int i = 0; i < map.length; i++) {
				if(i > 0)
					output.append('\n');
				output.append('[');
				for(int j = 0; j < map[i].length; j++) {
					if(j > 0)
						output.append(';');
					output.append(String.format("%-" + width[j] + "s", sMap[i][j]));
				}
				output.append(']');
			}
			return output.toString();
		}
		
	}
	
}
