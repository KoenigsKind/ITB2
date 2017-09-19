package itb2.engine.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import itb2.image.Image;
import itb2.image.RgbImage;

/**
 * Helper class for loading and saving images
 * 
 * @author Micha Strauch
 */
public class ImageIO {
	//TODO Add support for Portable Anymap
	
	/**
	 * Load image from file
	 * 
	 * @param file File image is stored
	 * @return Loaded image
	 * 
	 * @throws IOException If something goes wrong
	 */
	public static Image load(File file) throws IOException {
		BufferedImage bufferedImage = javax.imageio.ImageIO.read(file);
		Image image = new RgbImage(bufferedImage);
		image.setName(file);
		return image;
	}
	
	/**
	 * Save image to file.<br>
	 * The file extension will be used as the image-format.
	 * 
	 * @param image Image to save
	 * @param file  File to save image to
	 * 
	 * @throws IOException If format could not be identified or something else goes wrong
	 */
	public static void save(Image image, File file) throws IOException {
		String format = file.getName().replaceFirst("^.*(\\..*?)$", "$1");
		
		save(image, format, file);
	}
	
	/**
	 * Save image to file.
	 * 
	 * @param image  Image to save
	 * @param format Image-Format to use 
	 * @param file   File to save image to
	 * 
	 * @throws IOException If something goes wrong
	 */
	public static void save(Image image, String format, File file) throws IOException {
		javax.imageio.ImageIO.write(image.asBufferedImage(), format, file);
	}
}
