import itb2.filter.AbstractFilter;
import itb2.filter.ImageConverter;
import itb2.filter.RequireImageType;
import itb2.image.GreyscaleImage;
import itb2.image.HsiImage;
import itb2.image.Image;
import itb2.image.RgbImage;

@ImageConverter(GreyscaleImage.class)
@RequireImageType({GreyscaleImage.class, HsiImage.class, RgbImage.class})
public class GreyscaleFilter extends AbstractFilter {
	public static final String CONVERSION_METHOD = "RGB Conversion Method";
	public static final RgbConverter EVEN, CCIR_601, BT_709, SMPTE_240M;
	
	static {
		EVEN       = new RgbConverter(" - Evenly - ",     (r,g,b) -> (r + g + b) / 3);
		CCIR_601   = new RgbConverter("CCIR 601",   (r,g,b) -> 0.2990*r + 0.5870*g + 0.1140*b);
		BT_709     = new RgbConverter("BT.709",     (r,g,b) -> 0.2126*r + 0.7152*g + 0.0722*b);
		SMPTE_240M = new RgbConverter("SMPTE 240M", (r,g,b) -> 0.2120*r + 0.7010*g + 0.0870*b);
	}
	
	public GreyscaleFilter() {
		getProperties().addOptionProperty(CONVERSION_METHOD, EVEN, EVEN, CCIR_601, BT_709, SMPTE_240M);
	}
	
	@Override
	public Image filter(Image input) {
		double[][][] src = input.getData();
		
		if(input instanceof GreyscaleImage)
			return input;
		
		if(input instanceof HsiImage)
			return new GreyscaleImage(src[HsiImage.INTENSITY]);
		
		if(input instanceof RgbImage) {
			RgbConverter converter = getProperties().getOptionProperty(CONVERSION_METHOD);
			
			double[][] dst = new double[input.getWidth()][input.getHeight()];
			for(int x = 0; x < input.getWidth(); x++) {
				for(int y = 0; y < input.getHeight(); y++) {
					double red   = src[0][x][y];
					double green = src[1][x][y];
					double blue  = src[2][x][y];
					
					dst[x][y] = converter.convert(red, green, blue);
				}
			}
			return new GreyscaleImage(dst);
		}
		
		return null; // Won't happen
	}
	
	static class RgbConverter {
		final String name;
		final Converter converter;
		
		RgbConverter(String name, Converter converter) {
			this.name = name;
			this.converter = converter;
		}
		
		double convert(double red, double green, double blue) {
			return converter.convert(red, green, blue);
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
