package itb2.engine;

import java.io.File;
import java.io.IOException;

import itb2.data.ObservableLinkedList;
import itb2.image.Image;

/**
 * Manager for loaded images
 *
 * @author Micha Strauch
 */
public interface ImageManager {
	
	/** List of all currently loaded images */
	public ObservableLinkedList<Image> getImageList();
	
	/** 
	 * Loads an image from the given file, adds it to the
	 * list, and returns the loaded image
	 * 
	 * @param file File to load image from
	 * 
	 * @return Loaded image
	 */
	public Image loadImage(File file) throws IOException;
	
}
