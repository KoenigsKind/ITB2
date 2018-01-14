package itb2.engine;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import itb2.data.ObservableTreeSet;
import itb2.filter.Filter;
import itb2.filter.RequireImageType;
import itb2.image.Image;

/**
 * Manager for Filter actions
 * 
 * @author Micha Strauch
 */
public interface FilterManager {
	
	/**
	 * Loads a filter from the given file
	 * and adds it to the filter list
	 * 
	 * @param file File to load filter from
	 * 
	 * @return Loaded filter
	 * 
	 * @throws IOException If the filter can't be loaded successfully
	 */
	public Filter loadFilter(File file) throws IOException;
	
	/**
	 * List of all currently loaded filters
	 * 
	 * @return List of all loaded filters
	 */
	public ObservableTreeSet<Filter> getFilters();
	
	/**
	 * Returns the filter with the given class name
	 * 
	 * @param className Name of filter 
	 * 
	 * @return Filter or null, if no filter found
	 */
	public Filter getFilter(String className);
	
	/**
	 * Calls the given filter and returns filtered images. This method
	 * should be called instead of directly calling the filter, as this
	 * method treats {@link RequireImageType} annotations correctly.
	 * 
	 * @param filter Filter to call
	 * @param images Images to filter
	 * 
	 * @return Filtered images
	 */
	public Image[] callFilter(Filter filter, Image... images);
	
	/**
	 * Similar to {@link #callFilter(Filter, Image...)}
	 * <p>
	 * Uses a different thread to first run the filter and afterwards
	 * run the given receiver with filtered images.<br>
	 * If an error occurred the receiver will be called with <i>null</i>
	 * as parameter. 
	 * 
	 * @param filter   Filter to call
	 * @param images   Images to filter
	 * @param receiver Consumer to receive filtered images
	 */
	public void callFilter(Filter filter, Image[] images, Consumer<Image[]> receiver);
	
}
