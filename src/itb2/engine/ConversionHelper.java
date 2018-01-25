package itb2.engine;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import itb2.filter.AbstractFilter;
import itb2.image.BinaryImage;
import itb2.image.Channel;
import itb2.image.DrawableImage;
import itb2.image.GrayscaleImage;
import itb2.image.GroupedImage;
import itb2.image.HsiImage;
import itb2.image.HsvImage;
import itb2.image.Image;
import itb2.image.ImageConverter;
import itb2.image.ImageFactory;
import itb2.image.ImageUtils;
import itb2.image.RgbImage;

/**
 * Helper class for converting images.
 * This filter can convert between all basic images.
 *
 * @author Micha Strauch
 */
final class ConversionHelper extends AbstractFilter {
	
	/** Factory to use, to create image (null = keep precision of input) */
	private final ImageFactory factory;
	
	/** Image type to produce */
	private final ImageType type;
	
	/** Registers all known image conversions */
	public static void registerImageConversions() {
		ImageFactory[] factories = {ImageFactory.doublePrecision(), ImageFactory.bytePrecision(), null};
		ImageType[] types = {ImageType.RGB, ImageType.GRAYSCALE, ImageType.HSI, ImageType.HSV, ImageType.GROUPED};
		
		ImageConverter.register(Image.class, ImageFactory.bytePrecision().binary(), new ConversionHelper(ImageType.BINARY, ImageFactory.bytePrecision()));
		ImageConverter.register(Image.class, ImageFactory.bytePrecision().drawable(), new ConversionHelper(ImageType.DRAWABLE, ImageFactory.bytePrecision()));
		
		for(ImageFactory factory : factories)
			for(ImageType type : types)
				ImageConverter.register(Image.class, type.getClass(factory), new ConversionHelper(type, factory));
	}
	
	/** Converts any image into the given type, using the given factory */
	private ConversionHelper(ImageType type, ImageFactory factory) {
		this.factory = factory;
		this.type = type;
	}
	
	@Override
	public Image filter(Image input) {
		ImageFactory factory = getImageFactory(input);
		Image output = null;
		
		switch(type) {
			case RGB:
				output = toRgb(input, factory);
				break;
			case GRAYSCALE:
				output = toGrayscale(input, factory);
				break;
			case HSI:
				output = toHsi(input, factory);
				break;
			case HSV:
				output = toHsv(input, factory);
				break;
			case GROUPED:
				output = toGrouped(input, factory);
				break;
			case DRAWABLE:
				output = toDrawable(input, factory);
				break;
			case BINARY:
				output = toBinary(input, factory);
				break;
		}
		
		if(output != null && output.getName() == null)
				output.setName((Serializable) input.getName());
		
		return output;
	}
	
	/** Returns the correct factory to use, considering the given image */
	private ImageFactory getImageFactory(Image image) {
		if(factory != null)
			return factory;
		
		return ImageFactory.getPrecision(image);
	}
	
	/** Converts the input image into an RGB image using the given factory */
	private Image toRgb(Image input, ImageFactory factory) {
		if(input instanceof RgbImage) {
			Image output = factory.rgb(input.getSize());
			ImageUtils.copy(input, output);
			return output;
		}
		
		if(input instanceof GrayscaleImage) {
			Channel grayscale = input.getChannel(GrayscaleImage.GRAYSCALE);
			
			Image output = factory.rgb(input.getSize());
			for(Channel channel : output)
				ImageUtils.copy(grayscale, channel);
			
			return output;
		}
		
		return factory.rgb(input.asBufferedImage());
	}
	
	/** Converts the input image into a grayscale image using the given factory */
	private Image toGrayscale(Image input, ImageFactory factory) {
		if(input instanceof GrayscaleImage)
			return factory.gray(input.getChannel(GrayscaleImage.GRAYSCALE));
		if(input instanceof HsiImage)
			return factory.gray(input.getChannel(HsiImage.INTENSITY));
		if(input instanceof HsvImage || input instanceof GroupedImage || input instanceof BinaryImage)
			input = ImageFactory.bytePrecision().drawable(input.asBufferedImage());
		
		Image output = factory.gray(input.getSize());
		for(int col = 0; col < output.getWidth(); col++) {
			for(int row = 0; row < output.getHeight(); row++) {
				double value = 0;
				for(int chan = 0; chan < input.getChannelCount(); chan++)
					value += input.getValue(col, row, chan);
				value /= input.getChannelCount();
				output.setValue(col, row, value);
			}
		}
		
		return output;
	}
	
	/** Converts the input image into an HSV image using the given factory */
	private Image toHsv(Image input, ImageFactory factory) {
		if(input instanceof HsvImage) {
			HsvImage output = factory.hsv(input.getSize());
			
			double[] factor = {
					output.maxHue() / ((HsvImage)input).maxHue(),
					output.maxSaturation() / ((HsvImage)input).maxSaturation(),
					output.maxValue() / ((HsvImage)input).maxValue()
			};
			
			for(int col = 0; col < output.getWidth(); col++) {
				for(int row = 0; row < output.getHeight(); row++) {
					double[] hsv = input.getValue(col, row);
					
					for(int c = 0; c < hsv.length; c++)
						hsv[c] *= factor[c];
					
					output.setValue(col, row, hsv);
				}
			}
			return output;
		}
		
		if( !(input instanceof RgbImage) )
			input = toRgb(input, factory);
		
		HsvImage output = factory.hsv(input.getSize());
		for(int col = 0; col < output.getWidth(); col++) {
			for(int row = 0; row < output.getHeight(); row++) {
				double[] rgb = input.getValue(col, row);
				double[] hsv = rgb2hsv(rgb, output);
				output.setValue(col, row, hsv);
			}
		}
		return output;
	}
	
	/** Converts the input image into an HSI image using the given factory */
	private Image toHsi(Image input, ImageFactory factory) {
		if(input instanceof HsiImage) {
			HsiImage output = factory.hsi(input.getSize());
			
			double[] factor = {
					output.maxHue() / ((HsiImage)input).maxHue(),
					output.maxSaturation() / ((HsiImage)input).maxSaturation(),
					output.maxIntensity() / ((HsiImage)input).maxIntensity()
			};
			
			for(int col = 0; col < output.getWidth(); col++) {
				for(int row = 0; row < output.getHeight(); row++) {
					double[] hsi = input.getValue(col, row);
					
					for(int c = 0; c < hsi.length; c++)
						hsi[c] *= factor[c];
					
					output.setValue(col, row, hsi);
				}
			}
			return output;
		}
		
		if(input instanceof GrayscaleImage) {
			HsiImage output = factory.hsi(input.getSize());
			ImageUtils.copy(input.getChannel(GrayscaleImage.GRAYSCALE), output.getChannel(HsiImage.INTENSITY));
			return output;
		}
		
		if( !(input instanceof RgbImage) )
			input = toRgb(input, factory);
		
		HsiImage output = factory.hsi(input.getSize());
		for(int col = 0; col < output.getWidth(); col++) {
			for(int row = 0; row < output.getHeight(); row++) {
				double[] rgb = input.getValue(col, row);
				double[] hsi = rgb2hsi(rgb, output);
				output.setValue(col, row, hsi);
			}
		}
		return output;
	}
	
	/** Converts the input image into a grouped image using the given factory */
	private Image toGrouped(Image input, ImageFactory factory) {
		if(input instanceof GroupedImage) {
			int count = ((GroupedImage)input).getGroupCount();
			GroupedImage output = factory.group(input.getSize(), count);
			ImageUtils.copy(input, output);
			
			return output;
		}
		
		BufferedImage image = input.asBufferedImage();
		int groupCount = 0;
		Map<Integer, Integer> groupMap = new TreeMap<>();
		groupMap.put(0x000000, GroupedImage.BLACK);
		groupMap.put(0xFFFFFF, GroupedImage.WHITE);
		
		for(int col = 0; col < image.getWidth(); col++) {
			for(int row = 0; row < image.getHeight(); row++) {
				int rgb = image.getRGB(col, row) & 0xFFFFFF;
				
				if(!groupMap.containsKey(rgb))
					groupMap.put(rgb, ++groupCount);
			}
		}
		
		if(groupCount > 254 && factory == ImageFactory.bytePrecision()) {
			// Too many colors, just use grayscale value
			if( !(input instanceof GrayscaleImage) )
				input = toGrayscale(input, factory);
			
			GroupedImage output = factory.group(input.getSize());
			ImageUtils.copy(input.getChannel(GrayscaleImage.GRAYSCALE), output.getChannel(GroupedImage.GROUP_ID));
			return output;
		}
		
		GroupedImage output = factory.group(input.getSize(), groupCount);
		
		for(int col = 0; col < image.getWidth(); col++) {
			for(int row = 0; row < image.getHeight(); row++) {
				int rgb = image.getRGB(col, row) & 0xFFFFFF;
				int group = groupMap.get(rgb);
				output.setGroup(col, row, group);
			}
		}
		
		return output;
		
	}
	
	/** Converts the input image into a binary image using the given factory */
	private Image toBinary(Image input, ImageFactory factory) {
		if( !(input instanceof GrayscaleImage) )
			input = toGrayscale(input, factory);
		Image output = factory.binary(input.getSize());
		
		for(int col = 0; col < input.getWidth(); col++) {
			for(int row = 0; row < output.getHeight(); row++) {
				double value = input.getValue(col, row, GrayscaleImage.GRAYSCALE);
				output.setValue(col, row, value < 128 ? 0 : 1);
			}
		}
		
		return output;
	}
	
	/** Converts the input image into a drawable image using the given factory */
	private Image toDrawable(Image input, ImageFactory factory) {
		return factory.drawable(input.asBufferedImage());
	}
	
	/**
	 * Converts the given RGB values to HSI. The output
	 * image is used to get the correct maximum values.
	 * <p>
	 * <a href="http://fourier.eng.hmc.edu/e161/lectures/ColorProcessing/node2.html">Source</a> 
	 * 
	 * @param rgb    RGB values
	 * @param output Image to get maximum values from
	 * 
	 * @return HSI values
	 */
	private double[] rgb2hsi(double[] rgb, HsiImage output) {
		double r = rgb[0], g = rgb[1], b = rgb[2];
		
		// Hue
		double h = Math.acos(0.5 * ( (r-g) + (r-b) ) / Math.sqrt( (r-g)*(r-g) + (r-b)*(g-b) ) );
		if(Double.isNaN(h))
			h = 0;
		if(b > g)
			h = 2 * Math.PI - h;
		h /= 2 * Math.PI;
		
		// Intensity
		double i = (r + g + b) / (3 * 255);
		
		// Saturation
		double s = 1 - Math.min(Math.min(r, g), b) / (i * 255);
		
		return new double[] {h * output.maxHue(), s * output.maxSaturation(), i * output.maxIntensity()};
	}
	
	/**
	 * Converts the given RGB values to HSV. The output
	 * image is used to get the correct maximum values.
	 * <p>
	 * <a href="https://de.wikipedia.org/wiki/HSV-Farbraum#Umrechnung_RGB_in_HSV/HSL">Source</a> 
	 * 
	 * @param rgb    RGB values
	 * @param output Image to get maximum values from
	 * 
	 * @return HSV values
	 */
	private double[] rgb2hsv(double[] rgb, HsvImage output) {
		double r = rgb[0] / 255, g = rgb[1] / 255, b = rgb[2] / 255;
		double max = Math.max(Math.max(r, g), b);
		double min = Math.min(Math.min(r, g), b);
		
		// Hue
		double h;
		if(max == min)
			h = 0;
		else if(max == r)
			h = 60 * (0 + (g - b)/(max - min));
		else if(max == g)
			h = 60 * (2 + (b - r)/(max - min));
		else // max == b
			h = 60 * (4 + (r - g)/(max - min));
		if(h < 0)
			h += 360;
		h /= 360;
		
		// Saturation
		double s = max == 0 ? 0 : (max - min) / max;
		
		// Value
		double v = max;
		
		return new double[] {h * output.maxHue(), s * output.maxSaturation(), v * output.maxValue()};
	}
	
	/**
	 * All known image types
	 *
	 * @author Micha Strauch
	 */
	private enum ImageType {
		RGB, GRAYSCALE, HSI, HSV, DRAWABLE, BINARY, GROUPED;
		
		/** Returns the class of the image type, considering the given factory */
		public Class<? extends Image> getClass(ImageFactory factory) {
			switch(this) {
				case RGB:
					return factory != null
							? factory.rgb()
							: RgbImage.class;
				case GRAYSCALE:
					return factory != null
							? factory.gray()
							: GrayscaleImage.class;
				case HSI:
					return factory != null
							? factory.hsi()
							: HsiImage.class;
				case HSV:
					return factory != null
							? factory.hsv()
							: HsvImage.class;
				case GROUPED:
					return factory != null
							? factory.group()
							: GroupedImage.class;
				case DRAWABLE:
					return DrawableImage.class;
				case BINARY:
					return BinaryImage.class;
			}
			
			throw new UnsupportedOperationException("How could this happen?");
		}
	}
	
}
