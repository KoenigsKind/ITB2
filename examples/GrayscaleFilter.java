import itb2.filter.AbstractFilter;
import itb2.image.GrayscaleImage;
import itb2.image.HsiImage;
import itb2.image.Image;
import itb2.image.ImageConverter;
import itb2.image.RgbImage;

public class GrayscaleFilter extends AbstractFilter {
	public static final String CONVERSION_METHOD = "RGB Conversion Method";
	public static final RgbConverter DEFAULT, CCIR_601, BT_709, SMPTE_240M;
	
	static {
		DEFAULT       = new RgbConverter(" - Default - ",     (r,g,b) -> (r + g + b) / 3);
		CCIR_601   = new RgbConverter("CCIR 601",   (r,g,b) -> 0.2990*r + 0.5870*g + 0.1140*b);
		BT_709     = new RgbConverter("BT.709",     (r,g,b) -> 0.2126*r + 0.7152*g + 0.0722*b);
		SMPTE_240M = new RgbConverter("SMPTE 240M", (r,g,b) -> 0.2120*r + 0.7010*g + 0.0870*b);
	}
	
	public GrayscaleFilter() {
		getProperties().addOptionProperty(CONVERSION_METHOD, DEFAULT, DEFAULT, CCIR_601, BT_709, SMPTE_240M);
		
		ImageConverter.register(HsiImage.class, GrayscaleImage.class, this);
		ImageConverter.register(RgbImage.class, GrayscaleImage.class, this);
	}
	
	@Override
	public Image filter(Image input) {
		
		if(input instanceof HsiImage)
			return new GrayscaleImage(input.getChannel(HsiImage.INTENSITY));
		
		if(input instanceof RgbImage) {
			RgbConverter converter = getProperties().getOptionProperty(CONVERSION_METHOD);
			GrayscaleImage output = new GrayscaleImage(input.getSize());
			
			for(int row = 0; row < input.getHeight(); row++) {
				for(int col = 0; col < input.getWidth(); col++) {
					double value = converter.convert(input.getValue(row, col));
					output.setValue(row, col, value);
				}
			}
			return output;
		}
		
		return ImageConverter.convert(input, GrayscaleImage.class);
	}
	
	static class RgbConverter {
		final String name;
		final Converter converter;
		
		RgbConverter(String name, Converter converter) {
			this.name = name;
			this.converter = converter;
		}
		
		double convert(double[] rgb) {
			return converter.convert(rgb[0], rgb[1], rgb[2]);
		}
		
		@Override
		public String toString() {
			return name;
		}
		
		@FunctionalInterface
		static interface Converter {
			double convert(double red, double green, double blue);
		}
		
	}

}
