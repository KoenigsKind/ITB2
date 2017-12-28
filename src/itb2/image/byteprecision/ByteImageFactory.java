package itb2.image.byteprecision;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import itb2.image.Channel;
import itb2.image.GrayscaleImage;
import itb2.image.GroupedImage;
import itb2.image.HsiImage;
import itb2.image.HsvImage;
import itb2.image.ImageFactory;
import itb2.image.RgbImage;

/**
 * Factory to create an image with byte precision. Byte precision
 * images can store only integers between 0 and 255, but take eight
 * times less space then double precision images. 
 *
 * @author Micha Strauch
 */
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
	public Class<? extends HsvImage> hsv() {
		return HsvByteImage.class;
	}

	@Override
	public Class<? extends GrayscaleImage> gray() {
		return GrayscaleByteImage.class;
	}

	@Override
	public Class<? extends GroupedImage> group() {
		return GroupedByteImage.class;
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
	public HsvImage hsv(int width, int height) {
		return new HsvByteImage(width, height);
	}

	@Override
	public HsvImage hsv(Dimension size) {
		return new HsvByteImage(size);
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

	@Override
	public GroupedImage group(int width, int height, int groupCount) {
		return new GroupedByteImage(width, height, groupCount);
	}

	@Override
	public GroupedImage group(Dimension size, int groupCount) {
		return new GroupedByteImage(size, groupCount);
	}

}
