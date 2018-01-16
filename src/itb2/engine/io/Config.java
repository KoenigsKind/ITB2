package itb2.engine.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import itb2.engine.Controller;
import itb2.filter.Filter;
import itb2.image.Image;

/**
 * Writes current state (opened images) to file, or loads it from file.
 *
 * @author Micha Strauch
 */
public class Config implements Serializable {
	private static final long serialVersionUID = -4106334935531297234L;
	
	/** Config file */
	public static final File DEFAULT_CONFIG = new File("ITB2.bin");
	
	/**
	 * Saves the current ITB2 state
	 * 
	 * @throws IOException If something goes wrong
	 */
	public static void saveState() throws IOException {
		try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(DEFAULT_CONFIG))) {
			output.writeObject(new Config());
		} catch(Exception e) {
			throw new IOException("Could not save config to file '" + DEFAULT_CONFIG.getAbsolutePath() + "'", e);
		}
	}
	
	/**
	 * Loads the last saved ITB2 state
	 * 
	 * @throws FileNotFoundException If no config file was found
	 * @throws IOException If something goes wrong
	 */
	public static void loadState() throws FileNotFoundException, IOException {
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(DEFAULT_CONFIG))) {
			input.readObject();
		} catch(FileNotFoundException e) {
			throw e;
		} catch(Exception e) {
			e.printStackTrace();
			throw new IOException("Could not load config from file '" + DEFAULT_CONFIG.getAbsolutePath() + "'", e);
		}
	}
	
	/**
	 * Writes config to stream
	 * 
	 * @param stream Stream to write to
	 * 
	 * @throws IOException If something goes wrong
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {
		// Collect data
		List<Image> images = Controller.getImageManager().getImageList();
		Set<Filter> filters = Controller.getFilterManager().getFilters();
		Set<FilterClass> filterClasses = new HashSet<>();
		for(Filter filter : filters) try {
			Class<?> clazz = filter.getClass();
			if(filter instanceof FilterWrapper)
				clazz = ((FilterWrapper) filter).getWrappedClass();
			filterClasses.add(new FilterClass(clazz));
		} catch(Exception e) {
			// Skip filter
		}
		List<File> lastImages = new ArrayList<>(ImageIO.getLastImages());
		List<File> lastFilters = new ArrayList<>(FilterIO.getLastFilters());
		
		// Write data
		stream.writeObject(lastImages);
		stream.writeObject(lastFilters);
		stream.writeObject(filterClasses);
		stream.writeObject(images);
	}
	
	/**
	 * Loads config from stream
	 * 
	 * @param stream Stream to load from
	 * 
	 * @throws IOException If something goes wrong
	 * @throws ClassNotFoundException If something goes wrong
	 */
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		// First read list of last opened images
		List<?> lastImages = (List<?>) stream.readObject();
		List<File> lastImagesList = ImageIO.getLastImages();
		lastImagesList.clear();
		for(Object image : lastImages) {
			if(image instanceof File) {
				lastImagesList.add((File)image);
			}
		}
		
		// Then read list of last opened filters
		List<?> lastFilters = (List<?>) stream.readObject();
		List<File> lastFiltersList = FilterIO.getLastFilters();
		lastFiltersList.clear();
		for(Object filter : lastFilters) {
			if(filter instanceof File) {
				lastFiltersList.add((File)filter);
			}
		}
		
		// Then read filters, as they might contain image types
		Set<?> filterPaths = (Set<?>) stream.readObject();
		Controller.getFilterManager().getFilters().clear();
		for(Object obj : filterPaths) {
			if(obj instanceof FilterClass) try {
				FilterClass filterClass = (FilterClass) obj;
				Filter filter = filterClass.getInstance();
				Controller.getFilterManager().getFilters().add(filter);
			} catch(Exception e) {
				//Ignore filter
			}
		}
		
		// Then read images
		List<?> images = (List<?>) stream.readObject();
		List<Image> imageList = Controller.getImageManager().getImageList();
		imageList.clear();
		for(Object image : images) {
			if(image instanceof Image) {
				imageList.add((Image)image);
			}
		}
	}
	
	/**
	 * Helper class for storing loaded filters 
	 *
	 * @author Micha Strauch
	 */
	private class FilterClass implements Serializable {
		private static final long serialVersionUID = Config.serialVersionUID;
		
		/** Location of the filter */
		private final File file;
		
		/** Tries to determine the location of the given class */
		public FilterClass(Class<?> clazz) throws IOException, URISyntaxException {
			URL url = clazz.getResource(clazz.getSimpleName() + ".class");
			
			if(url == null)
				throw new IOException("Can't determine location of class " + clazz.getSimpleName());
			
			file = new File(url.toURI());
		}
		
		/** Returns an instance of the stored filter */
		public Filter getInstance() throws IOException {
			return FilterIO.loadClass(file);
		}
		
	}

}
