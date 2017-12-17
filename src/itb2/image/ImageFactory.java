package itb2.image;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import itb2.image.byteprecision.AbstractByteImage;
import itb2.image.byteprecision.ByteImageFactory;
import itb2.image.doubleprecision.DoubleImageFactory;

public abstract class ImageFactory {
	private static ImageFactory doublePrecision, bytePrecision;
	
	public static ImageFactory doublePrecision() {
		if(doublePrecision == null) {
			doublePrecision = new DoubleImageFactory();
		}
		return doublePrecision;
	}
	
	public static ImageFactory bytePrecision() {
		if(bytePrecision == null) {
			bytePrecision = new ByteImageFactory();
		}
		return bytePrecision;
	}
	
	public static ImageFactory getPrecision(Image image) {
		if(image instanceof AbstractByteImage)
			return bytePrecision();
		
		return doublePrecision();
	}
	
	public abstract Class<? extends RgbImage> rgb();
	public abstract Class<? extends HsiImage> hsi();
	public abstract Class<? extends GrayscaleImage> gray();
	public abstract Class<? extends GroupedImage> group();
	
	public abstract RgbImage rgb(int width, int height);
	public abstract RgbImage rgb(Dimension size);
	public abstract RgbImage rgb(BufferedImage image);
	
	public abstract HsiImage hsi(int width, int height);
	public abstract HsiImage hsi(Dimension size);
	
	public abstract GrayscaleImage gray(int width, int height);
	public abstract GrayscaleImage gray(Dimension size);
	public abstract GrayscaleImage gray(Channel channel);
	
	public abstract GroupedImage group(int width, int height, int groupCount);
	public abstract GroupedImage group(Dimension size, int groupCount);
	
}
