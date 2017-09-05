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
	
	@Override
	public Image filter(Image input) {
		double[][][] src = input.getData();
		
		if(input instanceof GreyscaleImage)
			return input;
		
		if(input instanceof HsiImage)
			return new GreyscaleImage(src[HsiImage.INTENSITY]);
		
		if(input instanceof RgbImage) {
			double[][] dst = new double[input.getWidth()][input.getHeight()];
			for(int x = 0; x < input.getWidth(); x++) {
				for(int y = 0; y < input.getHeight(); y++) {
					double val = 0;
					for(int k = 0; k < input.getChannelCount(); k++)
						val += src[k][x][y];
					val /= input.getChannelCount();
					
					dst[x][y] = val;
				}
			}
			return new GreyscaleImage(dst);
		}
		
		return null; // Won't happen
	}

}
