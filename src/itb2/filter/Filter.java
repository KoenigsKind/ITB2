package itb2.filter;

import java.util.Collection;

import itb2.image.Image;

/**
 * Basic filter<br>
 * Can be run on images to produce new filtered images
 * 
 * @author Micha Strauch
 */
public interface Filter {
	
	/**
	 * Receives one or multiple images and may return
	 * any number of newly created images
	 * 
	 * @param input Images to be filtered
	 * 
	 * @return Filtered images
	 */
	public Image[] filter(Image[] input);
	
	/**
	 * Properties of this filter, the user is allowed to modify
	 * 
	 * @return Properties of this filter
	 */
	public Collection<FilterProperty> getProperties();

}
