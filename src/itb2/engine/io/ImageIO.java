package itb2.engine.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import itb2.data.ObservableLinkedList;
import itb2.image.Image;
import itb2.image.ImageFactory;

/**
 * Helper class for loading and saving images
 * 
 * @author Micha Strauch
 */
public final class ImageIO {
	
	/** Last opened images */
	private static final ObservableLinkedList<File> lastImages = new ObservableLinkedList<>();
	
	/** Accepted file formats */
	private static String[][] acceptedFormats;
	
	/**
	 * Returns a list of last loaded images.
	 * Access should only be in synchronized environment!
	 * 
	 * @return Last loaded images
	 */
	public static ObservableLinkedList<File> getLastImages() {
		return lastImages;
	}
	
	/**
	 * Utility function, returning the extension of the given file. 
	 * 
	 * @param file File to get Extension from.
	 * 
	 * @return Extension of the file
	 */
	public static String getExtension(String file) {
		return file.replaceFirst("^.*\\.(.*?)$", "$1");
	}
	
	/**
	 * Returns an array of accepted formats for saving/loading images
	 * <p>
	 * String[*][2+] &rarr; Each format contains the name <i>String[*][0]</i>
	 * and file extensions <i>String[*][1..*]</i>
	 * 
	 * @return Accepted image formats
	 */
	public static String[][] acceptedFormats() {
		if(acceptedFormats == null) {
			acceptedFormats = new String[][] {
				{
					"Portable Anymap",
					"ppm", "pgm", "pbm", "pnm"
				},
				{
					"PNG",
					"png"
				},
				{
					"JPEG",
					"jpg", "jpeg", "jpe", "jif", "jfif", "jfi"
				},
				{
					"Bitmap",
					"bmp", "dip"
				},
				{
					"GIF",
					"gif"
				}
			};
		}
		return acceptedFormats;
	}
	
	/**
	 * Load image from file
	 * 
	 * @param file File image is stored
	 * @return Loaded image
	 * 
	 * @throws IOException If something goes wrong
	 */
	public static Image load(File file) throws IOException {
		Image image;
		
		if(file.getName().toLowerCase().matches("^.*\\.p[pgbn]m$"))
			image = AnymapIO.load(file);
		else {
			BufferedImage buffered = javax.imageio.ImageIO.read(file);
			image = ImageFactory.bytePrecision().rgb(buffered);
		}
		
		image.setName(file);
		
		synchronized (lastImages) {
			lastImages.remove(file);
			lastImages.push(file);
			
			while(lastImages.size() > 10)
				lastImages.removeLast();
		}
		
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
		String format = getExtension(file.getName());
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
		if(format.equalsIgnoreCase("ppm"))
			AnymapIO.save(image, file);
		else if( !javax.imageio.ImageIO.write(image.asBufferedImage(), format, file) )
			throw new IOException("Unknown format: " + format);
	}
	
	/** Should not be instantiated */
	private ImageIO() {}
	
}
