package itb2.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import itb2.engine.Controller;
import itb2.image.Image;

/**
 * Simple implementation of the Filter, giving some additional auxiliary
 * functions. Either {@link #filter(Image)} or {@link #filter(Image[])}
 * have to be overwritten.
 * 
 * @author Micha Strauch
 *
 */
public abstract class AbstractFilter implements Filter {
	
	/** Properties of this filter */
	protected FilterProperties properties = new FilterProperties();

	@Override
	public Collection<FilterProperty> getProperties() {
		return properties.getProperties();
	}
	
	@Override
	public Image[] filter(Image[] input) {
		if(input.length == 0) {
			Controller.getCommunicationManager().warning("No image selected");
			return new Image[0];
		}
		
		List<Image> filteredImages = new ArrayList<>(input.length);
		
		for(Image image : input){
			Image filtered = filter(image);
			if(filtered != null)
				filteredImages.add(filtered);
		}
		
		return filteredImages.toArray(new Image[0]);
	}
	
	/**
	 * By default the {@link #filter(Image[])} function will call
	 * this function once for every input image and returns all
	 * of the produced filtered images.
	 * 
	 * @param input Image to filter
	 * @return Filtered image or null
	 */
	public Image filter(Image input) {
		return null;
	}
	
	
	/**
	 * Tries to call the filter with the given class name, if the
	 * filter is found it returns the (first) filtered image.
	 * Otherwise it returns <i>null</i>.
	 * 
	 * @param name Name of the filter
	 * @param image Image to filter
	 * @return First filtered image if filter was found, otherwise <i>null</i>
	 */
	protected static Image callFilter(String name, Image image) {
		Image[] filtered = callFilter(name, new Image[]{image});
		if(filtered == null || filtered.length == 0)
			return null;
		return filtered[0];
	}
	
	/**
	 * Tries to call the filter with the given class name,
	 * if the filter is found it returns the filtered images.
	 * Otherwise it returns <i>null</i>.
	 * 
	 * @param name Name of the filter
	 * @param image Images to filter
	 * @return Filtered images if filter was found, otherwise <i>null</i>
	 */
	protected static Image[] callFilter(String name, Image image[]) {
		Filter filter = Controller.getFilterManager().getFilter(name);
		if(filter == null)
			return null;
		return Controller.getFilterManager().callFilter(filter, image);
	}
	
	/**
	 * Auxiliary function. Returns:<br>
	 *  - val, if min <= val <= max<br>
	 *  - min, if val < min<br>
	 *  - max, if val > max<br>
	 * 
	 * @param min Minimum value
	 * @param val Given value
	 * @param max Maximum value
	 * @return Value, bounded to min and max
	 */
	protected static int bound(int min, int val, int max) {
		return min >= val ? min : max <= val ? max : val;
	}
	
	/**
	 * Auxiliary function. Returns:<br>
	 *  - val, if min <= val <= max<br>
	 *  - min, if val < min<br>
	 *  - max, if val > max<br>
	 * 
	 * @param min Minimum value
	 * @param val Given value
	 * @param max Maximum value
	 * @return Value, bounded to min and max
	 */
	protected static double bound(double min, double val, double max) {
		return min >= val ? min : max <= val ? max : val;
	}
	
	/**
	 * Auxiliary function. Throws an error if
	 * the given condition is not true.
	 * 
	 * @param message   Message to show if condition is false
	 * @param condition Condition that must be true 
	 */
	protected static void assertTrue(String message, boolean condition) {
		if(!condition)
			throw new RuntimeException(message);
	}
	
	/**
	 * Auxiliary function. Throws an error if
	 * the given object is null.
	 * 
	 * @param message   Message to show if object is null
	 * @param condition Object that must not be null 
	 */
	protected static void assertNotNull(String message, Object object) {
		if(object == null)
			throw new RuntimeException(message);
	}
	
}
