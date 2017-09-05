package itb2.engine.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import itb2.filter.Filter;

public class FilterIO {
	private static final Map<File, ClassLoader> loaders = new HashMap<>();
	
	public static Filter load(File file) throws IOException {
		if(file.getName().toLowerCase().endsWith(".java"))
			return null; //TODO
		return loadClass(file);
	}
	
	public static Filter loadClass(File file) throws IOException {
		String className = file.getName().replaceFirst(".class$", "");
		return loadClass(file, className);
	}
	
	public static Filter loadClass(File file, String className) throws IOException {
		Class<?> clazz;
		String clazzName = file.getName().replaceFirst(".class$", "");
		try {
			clazz = Class.forName(clazzName);
		} catch(ClassNotFoundException e) {
			try {
				clazz = getLoader(file).loadClass(className);
			} catch(ClassNotFoundException ee) {
				throw new IOException("Could not find class '" + clazzName + "'");
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
	
	private static ClassLoader getLoader(File file) throws IOException {
		if(file.getName().toLowerCase().endsWith(".class"))
			file = file.getParentFile();
		
		if(!loaders.containsKey(file)) {
			URL url = file.toURI().toURL();
			ClassLoader loader = new URLClassLoader(new URL[]{url});
			loaders.put(file, loader);
		}
		return loaders.get(file);
	}
	
	private static Filter wrapFilter(Object oldFilter) throws IOException {
		return new FilterWrapper(oldFilter);
	}
	
}
