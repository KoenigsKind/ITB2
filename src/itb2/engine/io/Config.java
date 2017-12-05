package itb2.engine.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import itb2.engine.Controller;
import itb2.filter.Filter;
import itb2.gui.EditorGui;
import itb2.image.Image;

/**
 * Writes current state (opened images) to file, or loads it from file.
 *
 * @author Micha Strauch
 */
public class Config implements Serializable {
	private static final long serialVersionUID = -4106334935531297234L;
	
	/** Config file */
	private static final File DEFAULT_CONFIG = new File("ITB2.config");
	
	/**
	 * Saves the current ITB2 state
	 * 
	 * @param gui EditorGui
	 * 
	 * @throws IOException If something goes wrong
	 */
	public static void saveState(EditorGui gui) throws IOException {
		try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(DEFAULT_CONFIG))) {
			output.writeObject(new Config());
		} catch(Exception e) {
			e.printStackTrace();
			throw new IOException("Could not save config to file '" + DEFAULT_CONFIG.getAbsolutePath() + "'", e);
		}
	}
	
	/**
	 * Loads the last saved ITB2 state
	 * 
	 * @param gui EditorGui
	 * 
	 * @throws FileNotFoundException If no config file was found
	 * @throws IOException If something goes wrong
	 */
	public static void loadState(EditorGui gui) throws FileNotFoundException, IOException {
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(DEFAULT_CONFIG))) {
			input.readObject();
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
		Set<File> filterPaths = new HashSet<>();
		for(Filter filter : filters) {
			Class<? extends Filter> clazz = filter.getClass();
			URL path = clazz.getResource(clazz.getSimpleName() + ".class");
			if(path != null)
				filterPaths.add(new File(path.getPath()));
		}
		
		// Write data
		stream.writeObject(filterPaths);
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
		// First read filters, as they might contain image types
		Set<?> filterPaths = (Set<?>) stream.readObject();
		Controller.getFilterManager().getFilters().clear();
		for(Object path : filterPaths) {
			if(path instanceof File) try {
				Controller.getFilterManager().loadFilter((File)path);
			} catch(IOException e) {
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

}
