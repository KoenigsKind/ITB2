package itb2.image.doubleprecision;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import itb2.image.Channel;
import itb2.image.GrayscaleImage;
import itb2.image.HsiImage;
import itb2.image.ImageFactory;
import itb2.image.RgbImage;

public class DoubleImageFactory extends ImageFactory {

	@Override
	public Class<? extends RgbImage> rgb() {
		return RgbDoubleImage.class;
	}

	@Override
	public Class<? extends HsiImage> hsi() {
		return HsiDoubleImage.class;
	}

	@Override
	public Class<? extends GrayscaleImage> gray() {
		return GrayscaleDoubleImage.class;
	}

	@Override
	public RgbImage rgb(int width, int height) {
		return new RgbDoubleImage(width, height);
	}

	@Override
	public RgbImage rgb(Dimension size) {
		return new RgbDoubleImage(size);
	}

	@Override
	public RgbImage rgb(BufferedImage image) {
		return new RgbDoubleImage(image);
	}

	@Override
	public HsiImage hsi(int width, int height) {
		return new HsiDoubleImage(width, height);
	}

	@Override
	public HsiImage hsi(Dimension size) {
		return new HsiDoubleImage(size);
	}

	@Override
	public GrayscaleImage gray(int width, int height) {
		return new GrayscaleDoubleImage(width, height);
	}

	@Override
	public GrayscaleImage gray(Dimension size) {
		return new GrayscaleDoubleImage(size);
	}

	@Override
	public GrayscaleImage gray(Channel channel) {
		return new GrayscaleDoubleImage(channel);
	}

}
