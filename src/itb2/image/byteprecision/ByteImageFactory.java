package itb2.image.byteprecision;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import itb2.image.Channel;
import itb2.image.GrayscaleImage;
import itb2.image.HsiImage;
import itb2.image.ImageFactory;
import itb2.image.RgbImage;

public class ByteImageFactory extends ImageFactory {

	@Override
	public Class<? extends RgbImage> rgb() {
		return RgbByteImage.class;
	}

	@Override
	public Class<? extends HsiImage> hsi() {
		return HsiByteImage.class;
	}

	@Override
	public Class<? extends GrayscaleImage> gray() {
		return GrayscaleByteImage.class;
	}

	@Override
	public RgbImage rgb(int width, int height) {
		return new RgbByteImage(width, height);
	}

	@Override
	public RgbImage rgb(Dimension size) {
		return new RgbByteImage(size);
	}

	@Override
	public RgbImage rgb(BufferedImage image) {
		return new RgbByteImage(image);
	}

	@Override
	public HsiImage hsi(int width, int height) {
		return new HsiByteImage(width, height);
	}

	@Override
	public HsiImage hsi(Dimension size) {
		return new HsiByteImage(size);
	}

	@Override
	public GrayscaleImage gray(int width, int height) {
		return new GrayscaleByteImage(width, height);
	}

	@Override
	public GrayscaleImage gray(Dimension size) {
		return new GrayscaleByteImage(size);
	}

	@Override
	public GrayscaleImage gray(Channel channel) {
		return new GrayscaleByteImage(channel);
	}

}
