package itb2.engine.io;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import itb2.data.ObservableLinkedList;
import itb2.filter.Filter;

/**
 * Helper class for loading filters
 * 
 * @author Micha Strauch
 */
public final class FilterIO {
	/** Map of existing class loaders */
	private static final Map<File, ClassLoader> loaders = new HashMap<>();
	
	/** Last opened filters */
	private static final ObservableLinkedList<File> lastFilters = new ObservableLinkedList<>();
	
	/**
	 * Returns a list of last opened filters.
	 * Access should only be in synchronized environment!
	 * 
	 * @return Last opened filters
	 */
	public static ObservableLinkedList<File> getLastFilters() {
		return lastFilters;
	}
	
	/**
	 * Adds the given file to the list of last loaded filters
	 * 
	 * @param file Last loaded filters
	 */
	private static void loaded(File file) {
		synchronized (lastFilters) {
			lastFilters.remove(file);
			lastFilters.push(file);
			
			while(lastFilters.size() > 10)
				lastFilters.removeLast();
		}
	}
	
	/**
	 * Tries to load filter from given file (.java or .class)
	 * 
	 * @param file File to load filter from 
	 * @return Loaded filter
	 * @throws IOException If not successful
	 */
	public static Filter load(File file) throws IOException {
		if(file.getName().toLowerCase().endsWith(".java"))
			return loadJava(file);
		return loadClass(file);
	}
	
	/** Whether a compiler for .java files is available */
	public static boolean isCompilerAvailable() {
		return ToolProvider.getSystemJavaCompiler() != null;
	}
	
	/**
	 * Tries to compile a filter (.java)
	 * 
	 * @param file File to compile
	 * @return Compiled filter
	 * @throws IOException If not successful
	 */
	public static Filter loadJava(File file) throws IOException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		
		if(compiler == null)
			throw new IOException("No compiler found");
		
		if(compiler.run(null, null, null, file.getPath()) != 0)
			throw new IOException("Could not compile the file");
		
		String className = file.getName().replaceFirst(".java$", "");
		Filter filter = loadClass(file.getParentFile(), className);
		
		loaded(file);
		return filter;
	}
	
	/**
	 * Tries to load a compiled filter (.class)
	 * 
	 * @param file File to load filter from
	 * @return Loaded filter
	 * @throws IOException If not successful
	 */
	public static Filter loadClass(File file) throws IOException {
		String className = file.getName().replaceFirst(".class$", "");
		Filter filter = loadClass(file.getParentFile(), className);
		
		loaded(file);
		return filter;
	}
	
	/**
	 * Tries to load a compiled filter with given class name from given folder
	 * 
	 * @param folder    Folder to find filter in
	 * @param className Name of filter
	 * @return Loaded filter
	 * @throws IOException If not successful
	 */
	public static Filter loadClass(File folder, String className) throws IOException {
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		} catch(ClassNotFoundException e) {
			try {
				clazz = getLoader(folder).loadClass(className);
			} catch(ClassNotFoundException ee) {
				throw new IOException("Could not find class '" + className + "'");
			}
		}
		
		try {
			Object o = clazz.newInstance();
			if(o instanceof Filter)
				return (Filter)o;
			return wrapFilter(o);
		} catch(IllegalAccessException|InstantiationException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * Returns the class loader for given folder
	 * 
	 * @param folder Folder to get class loader for
	 * @return Class loader for given folder
	 * @throws IOException If not successful
	 */
	private static ClassLoader getLoader(File folder) throws IOException {
		if(!loaders.containsKey(folder)) {
			URL url = folder.toURI().toURL();
			
			URLClassLoader loader;
			try {
				// Try using the system class loader
				loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
				Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
				addURL.setAccessible(true);
				addURL.invoke(loader, url);
			} catch(Exception e) {
				// Use a custom loader instead
				loader = new URLClassLoader(new URL[]{url});
			}
			
			loaders.put(folder, loader);
		}
		return loaders.get(folder);
	}
	
	/**
	 * Tries to wrap the given object in a {@link FilterWrapper}
	 * <p>
	 * Used for filters written for the old ImageToolBox
	 * 
	 * @param oldFilter Instance of the old filter 
	 * @return Wrapped filter
	 * @throws IOException If not a filter
	 */
	private static Filter wrapFilter(Object oldFilter) throws IOException {
		return new FilterWrapper(oldFilter);
	}
	
	/** Should not be instantiated */
	private FilterIO() {}
}
