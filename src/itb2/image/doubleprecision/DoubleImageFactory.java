package itb2.image.doubleprecision;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import itb2.image.BinaryImage;
import itb2.image.Channel;
import itb2.image.DrawableImage;
import itb2.image.GrayscaleImage;
import itb2.image.GroupedImage;
import itb2.image.HsiImage;
import itb2.image.HsvImage;
import itb2.image.ImageFactory;
import itb2.image.RgbImage;

/**
 * Factory to create an image with double precision. Double
 * precision images can store any type of value but take eight
 * times more space then byte precision images. 
 *
 * @author Micha Strauch
 */
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
	public Class<? extends HsvImage> hsv() {
		return HsvDoubleImage.class;
	}

	@Override
	public Class<? extends GrayscaleImage> gray() {
		return GrayscaleDoubleImage.class;
	}

	@Override
	public Class<? extends GroupedImage> group() {
		return GroupedDoubleImage.class;
	}

	@Override
	public Class<? extends BinaryImage> binary() {
		throw new UnsupportedOperationException("BinaryImage not implemented for double precision");
	}

	@Override
	public Class<? extends DrawableImage> drawable() {
		throw new UnsupportedOperationException("DrawableImage not implemented for double precision");
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
	public HsvImage hsv(int width, int height) {
		return new HsvDoubleImage(width, height);
	}

	@Override
	public HsvImage hsv(Dimension size) {
		return new HsvDoubleImage(size);
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

	@Override
	public GroupedImage group(int width, int height) {
		return new GroupedDoubleImage(width, height, GroupedImage.AUTOMATIC_GROUP_COUNT);
	}

	@Override
	public GroupedImage group(int width, int height, int groupCount) {
		return new GroupedDoubleImage(width, height, groupCount);
	}

	@Override
	public GroupedImage group(Dimension size) {
		return new GroupedDoubleImage(size, GroupedImage.AUTOMATIC_GROUP_COUNT);
	}

	@Override
	public GroupedImage group(Dimension size, int groupCount) {
		return new GroupedDoubleImage(size, groupCount);
	}

	@Override
	public BinaryImage binary(int width, int height) {
		throw new UnsupportedOperationException("BinaryImage not implemented for double precision");
	}

	@Override
	public BinaryImage binary(Dimension size) {
		throw new UnsupportedOperationException("BinaryImage not implemented for double precision");
	}

	@Override
	public DrawableImage drawable(int width, int height) {
		throw new UnsupportedOperationException("DrawableImage not implemented for double precision");
	}

	@Override
	public DrawableImage drawable(Dimension size) {
		throw new UnsupportedOperationException("DrawableImage not implemented for double precision");
	}

	@Override
	public DrawableImage drawable(BufferedImage image) {
		throw new UnsupportedOperationException("DrawableImage not implemented for double precision");
	}

}
