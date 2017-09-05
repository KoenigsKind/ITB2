package itb2.engine.io;

import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import itb2.engine.Controller;
import itb2.filter.Filter;
import itb2.filter.FilterProperties;
import itb2.image.Image;
import itb2.image.RgbImage;
import itb2.image.Selection;

public class FilterWrapper implements Filter {
	private final Object oldFilter;
	private final Method filter1, filter2;
	private final Method setDimensions;
	private final Method handleMouseClick;
	private final Field properties;
	private Method hasProperties;
	
	public FilterWrapper(Object oldFilter) throws IOException {
		this.oldFilter = oldFilter;
		
		filter1 = getMethod("filter", double[][][].class, double[][][].class);
		filter2 = getMethod("filter", double[][][].class, double[][][].class, double[][][].class);
		
		if(filter1 == null && filter2 == null)
			throw new IOException("Can't find any filter methods in " + oldFilter.getClass().getName());
		
		setDimensions = getMethod("setDimensions", int.class, int.class);
		handleMouseClick = getMethod("handleMouseClick", Point.class);
		hasProperties = getMethod("hasProperties");
		properties = getField("properties");
	}
	
	public Class<?> getWrappedClass() {
		return oldFilter.getClass();
	}
	
	private Method getMethod(String name, Class<?>... parameterTypes) {
		try {
			return oldFilter.getClass().getMethod(name, parameterTypes);
		} catch(Exception e) {
			return null;
		}
	}
	
	private Field getField(String name) {
		try {
			return oldFilter.getClass().getField(name);
		} catch(Exception e) {
			return null;
		}
	}
	
	private <T> T callMethod(Method method, Object... parameter) {
		if(method != null) try {
			@SuppressWarnings("unchecked")
			T result = (T) method.invoke(oldFilter, parameter);
			return result;
		} catch(Exception e) {
			// Do nothing
		}
		return null;
	}

	@Override
	public Image[] filter(Image[] input) {
		if(input.length >= 1 && input.length <= 2) {
			int width = input[0].getWidth(), height = input[0].getHeight();
			double[][][] dst = new double[3][width][height];
			
			callMethod(setDimensions, input[0].getWidth(), input[0].getHeight());
			
			for(Image image : input)
				for(Selection selection : image.getSelections())
					callMethod(handleMouseClick, new Point(selection.x, selection.y));
			
			if(input.length == 1)
				callMethod(filter1, input[0].getData(), dst);
			else
				callMethod(filter2, input[0].getData(), input[1].getData(), dst);
			
			return new Image[]{new RgbImage(dst)};
		}
		return new Image[0];
	}
	
	

	@Override
	public FilterProperties getProperties() {
		if(hasProperties != null) {
			if(properties == null) {
				Controller.getCommunicationManager().warning("Could not load properties for " + oldFilter.getClass().getName());
				hasProperties = null;
			} else try {
				return (FilterProperties) properties.get(oldFilter);
			} catch(Exception e) {
				// Do nothing
			}
		}
		return null;
	}

}
