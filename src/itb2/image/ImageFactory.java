package itb2.image;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import itb2.image.byteprecision.AbstractByteImage;
import itb2.image.byteprecision.ByteImageFactory;
import itb2.image.doubleprecision.DoubleImageFactory;

/**
 * Factory to create an image with either double or byte precision.
 * Double precision images can store any type of value, while byte
 * precision images only store values between 0 and 255.<br> 
 * As double precision images use eight times the amount of space as
 * byte precision images, it's better to use double precision images
 * only, when needed. Better use byte precision images by default.
 * 
 * @author Micha Strauch
 */
public abstract class ImageFactory {
	/** Factories for creating images */
	private static ImageFactory doublePrecision, bytePrecision;
	
	/**
	 * Returns the ImageFactory for creating double precision images.
	 * {@link #bytePrecision()} should be preferred, as images created
	 * by the double precision factory are eight times bigger.
	 *  
	 * @return Factory for creating double precision images
	 */
	public static ImageFactory doublePrecision() {
		if(doublePrecision == null) {
			doublePrecision = new DoubleImageFactory();
		}
		return doublePrecision;
	}
	
	/**
	 * Returns the ImageFactory for creating byte precision images.
	 * 
	 * @return Factory for creating byte precision images
	 */
	public static ImageFactory bytePrecision() {
		if(bytePrecision == null) {
			bytePrecision = new ByteImageFactory();
		}
		return bytePrecision;
	}
	
	/**
	 * Returns the factory to create images using the same precision
	 * as the given image. If the precision can't be determined the
	 * double precision image factory is returned, to make sure any
	 * value can be stored.
	 * 
	 * @param image Image with certain precision
	 * @return ImageFactory matching the given image precision
	 */
	public static ImageFactory getPrecision(Image image) {
		if(image instanceof AbstractByteImage)
			return bytePrecision();
		
		// Special case: DrawableByteImage does not extends AbstractByteImage
		if(bytePrecision().drawable().isInstance(image))
			return bytePrecision();
		
		return doublePrecision();
	}
	
	/** Class for RgbImage with the precision of this factory. */
	public abstract Class<? extends RgbImage> rgb();
	/** Class for HsiImage with the precision of this factory. */
	public abstract Class<? extends HsiImage> hsi();
	/** Class for HsvImage with the precision of this factory. */
	public abstract Class<? extends HsvImage> hsv();
	/** Class for GrayscaleImage with the precision of this factory. */
	public abstract Class<? extends GrayscaleImage> gray();
	/** Class for GroupedImage with the precision of this factory. */
	public abstract Class<? extends GroupedImage> group();
	/** Class for BinaryImage with the precision of this factory. */
	public abstract Class<? extends BinaryImage> binary();
	/** Class for DrawableImage with the precision of this factory. */
	public abstract Class<? extends DrawableImage> drawable();
	
	/**
	 * Creates an RgbImage
	 * 
	 * @param width  Width of the image
	 * @param height Height of the image
	 * 
	 * @return Created RgbImage
	 */
	public abstract RgbImage rgb(int width, int height);
	
	/**
	 * Creates an RgbImage
	 * 
	 * @param size Size of the image
	 * 
	 * @return Created RgbImage
	 */
	public abstract RgbImage rgb(Dimension size);
	
	/**
	 * Creates an RgbImage filled with values of the given image
	 * 
	 * @param image Image to create RgbImage from
	 * 
	 * @return Created RgbImage
	 */
	public abstract RgbImage rgb(BufferedImage image);
	
	/**
	 * Creates an HsiImage
	 * 
	 * @param width  Width of the image
	 * @param height Height of the image
	 * 
	 * @return Created HsiImage
	 */
	public abstract HsiImage hsi(int width, int height);

	/**
	 * Creates an HsiImage
	 * 
	 * @param size Size of the image
	 * 
	 * @return Created HsiImage
	 */
	public abstract HsiImage hsi(Dimension size);
	
	/**
	 * Creates an HsvImage
	 * 
	 * @param width  Width of the image
	 * @param height Height of the image
	 * 
	 * @return Created HsvImage
	 */
	public abstract HsvImage hsv(int width, int height);

	/**
	 * Creates an HsvImage
	 * 
	 * @param size Size of the image
	 * 
	 * @return Created HsvImage
	 */
	public abstract HsvImage hsv(Dimension size);
	
	/**
	 * Creates a GrayscaleImage
	 * 
	 * @param width  Width of the image
	 * @param height Height of the image
	 * 
	 * @return Created GrayscaleImage
	 */
	public abstract GrayscaleImage gray(int width, int height);

	/**
	 * Creates a GrayscaleImage
	 * 
	 * @param size Size of the image
	 * 
	 * @return Created GrayscaleImage
	 */
	public abstract GrayscaleImage gray(Dimension size);
	
	/**
	 * Creates a GrayscaleImage filled with values of the given channel
	 * 
	 * @param channel Channel to create GrayscaleImage from
	 * 
	 * @return Created GrayscaleImage
	 */
	public abstract GrayscaleImage gray(Channel channel);
	
	/**
	 * Creates a GroupedImage
	 * 
	 * @param width  Width of the image
	 * @param height Height of the image
	 * 
	 * @return Created GroupedImage
	 */
	public abstract GroupedImage group(int width, int height);
	
	/**
	 * Creates a GroupedImage, with fixed group count.<p>
	 * Group count does not contain {@link GroupedImage#BLACK} and
	 * {@link GroupedImage#WHITE}, those two group IDs are always available.
	 * 
	 * @param width      Width of the image
	 * @param height     Height of the image
	 * @param groupCount Number of groups
	 * 
	 * @return Created GroupedImage
	 */
	public abstract GroupedImage group(int width, int height, int groupCount);

	/**
	 * Creates a GroupedImage
	 * 
	 * @param size Size of the image
	 * 
	 * @return Created GroupedImage
	 */
	public abstract GroupedImage group(Dimension size);

	/**
	 * Creates a GroupedImage, with fixed group count.<p>
	 * Group count does not contain {@link GroupedImage#BLACK} and
	 * {@link GroupedImage#WHITE}, those two group IDs are always available.
	 * 
	 * @param size       Size of the image
	 * @param groupCount Number of groups
	 * 
	 * @return Created GroupedImage
	 */
	public abstract GroupedImage group(Dimension size, int groupCount);
	
	/**
	 * Creates a BinaryImage
	 * 
	 * @param width  Width of the image
	 * @param height Height of the image
	 * 
	 * @return Created BinaryImage
	 */
	public abstract BinaryImage binary(int width, int height);
	
	/**
	 * Creates a BinaryImage
	 * 
	 * @param size Size of the image
	 * 
	 * @return Created BinaryImage
	 */
	public abstract BinaryImage binary(Dimension size);
	
	/**
	 * Creates a DrawableImage
	 * 
	 * @param width  Width of the image
	 * @param height Height of the image
	 * 
	 * @return Created DrawableImage
	 */
	public abstract DrawableImage drawable(int width, int height);
	
	/**
	 * Creates a DrawableImage
	 * 
	 * @param size Size of the image
	 * 
	 * @return Created DrawableImage
	 */
	public abstract DrawableImage drawable(Dimension size);
	
	/**
	 * Creates a DrawableImage filled with values of the given image
	 * 
	 * @param image Image to create DrawableImage from
	 * 
	 * @return Created DrawableImage
	 */
	public abstract DrawableImage drawable(BufferedImage image);
	
}
