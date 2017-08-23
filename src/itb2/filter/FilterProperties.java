package itb2.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FilterProperties {
	private final HashMap<String, Object> properties = new HashMap<>();
	
	public void addBooleanProperty(String name, boolean defaultValue) {
		properties.put(name, defaultValue);
	}
	
	public boolean getBooleanProperty(String name) {
		try {
			return (boolean) properties.get(name);
		} catch(Exception e) {
			throw new PropertyNotFoundException("boolean", name);
		}
	}
	
	public void addDoubleProperty(String name, double defaultValue) {
		properties.put(name, defaultValue);
	}
	
	public double getDoubleProperty(String name) {
		try {
			return (double) properties.get(name);
		} catch(Exception e) {
			throw new PropertyNotFoundException("double", name);
		}
	}
	
	public void addIntegerProperty(String name, int defaultValue) {
		properties.put(name, defaultValue);
	}
	
	public int getIntegerProperty(String name) {
		try {
			return (int) properties.get(name);
		} catch(Exception e) {
			throw new PropertyNotFoundException("integer", name);
		}
	}
	
	public void addStringProperty(String name, String defaultValue) {
		properties.put(name, defaultValue);
	}
	
	public String getStringProperty(String name) {
		try {
			return (String) properties.get(name);
		} catch(Exception e) {
			throw new PropertyNotFoundException("String", name);
		}
	}
	
	public void addRangeProperty(String name, double min, double step, double max, double defaultValue) {
		properties.put(name, new Range(min, step, max, defaultValue));
	}
	
	public double getRangeProperty(String name) {
		try {
			return ((Range) properties.get(name)).value;
		} catch(Exception e) {
			throw new PropertyNotFoundException("Range", name);
		}
	}
	
	public <T> void addOptionProperty(String name, T[] options, T defaultValue) {
		properties.put(name, new Option<T>(options, defaultValue));
	}
	
	public <T> T getOptionProperty(String name) {
		try {
			Option<?> option = (Option<?>) properties.get(name);
			@SuppressWarnings("unchecked")
			T value = (T)option.selected;
			return value;
		} catch(Exception e) {
			throw new PropertyNotFoundException("Option", name);
		}
	}
	
	public Map<String, Object> getProperties() {
		return Collections.unmodifiableMap(properties);
	}
	
	public class Range {
		public final double min, step, max;
		public double value;
		
		private Range(double min, double step, double max, double defaultValue) {
			this.min = min;
			this.step = step;
			this.max = max;
			this.value = defaultValue;
		}
		
	}
	
	public class Option<T> {
		public final T[] options;
		public T selected;
		
		private Option(T[] options, T defaultValue) {
			this.options = options;
			this.selected = defaultValue;
		}
	}

}
