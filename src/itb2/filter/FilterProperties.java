package itb2.filter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import itb2.filter.property.BooleanProperty;
import itb2.filter.property.DoubleProperty;
import itb2.filter.property.FilterProperty;
import itb2.filter.property.IntegerProperty;
import itb2.filter.property.OptionProperty;
import itb2.filter.property.RangeProperty;
import itb2.filter.property.StringProperty;

public class FilterProperties {
	private final HashMap<String, FilterProperty> properties = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	private <T extends FilterProperty> T getProperty(String name) {
		T property = null;
		try {
			property = (T) properties.get(name);
		} catch(ClassCastException e) {
			// Treat as non existing
		}
		return property;
	}
	
	private void setProperty(FilterProperty property) {
		properties.put(property.name, property);
	}
	
	public void addBooleanProperty(String name, boolean defaultValue) {
		BooleanProperty property = getProperty(name);
		if(property != null)
			property.value = defaultValue;
		else {
			property = new BooleanProperty(name, defaultValue);
			setProperty(property);
		}
	}
	
	public boolean getBooleanProperty(String name) {
		try {
			FilterProperty property = properties.get(name);
			return (boolean) property.getValue();
		} catch(Exception e) {
			throw new PropertyNotFoundException("boolean", name);
		}
	}
	
	public void addDoubleProperty(String name, double defaultValue) {
		DoubleProperty property = getProperty(name);
		if(property != null)
			property.value = defaultValue;
		else {
			property = new DoubleProperty(name, defaultValue);
			setProperty(property);
		}
	}
	
	public double getDoubleProperty(String name) {
		try {
			FilterProperty property = properties.get(name);
			return (double) property.getValue();
		} catch(Exception e) {
			throw new PropertyNotFoundException("double", name);
		}
	}
	
	public void addIntegerProperty(String name, int defaultValue) {
		IntegerProperty property = getProperty(name);
		if(property != null)
			property.value = defaultValue;
		else {
			property = new IntegerProperty(name, defaultValue);
			setProperty(property);
		}
	}
	
	public int getIntegerProperty(String name) {
		try {
			FilterProperty property = properties.get(name);
			return (int) property.getValue();
		} catch(Exception e) {
			throw new PropertyNotFoundException("integer", name);
		}
	}
	
	public void addStringProperty(String name, String defaultValue) {
		StringProperty property = getProperty(name);
		if(property != null)
			property.value = defaultValue;
		else {
			property = new StringProperty(name, defaultValue);
			setProperty(property);
		}
	}
	
	public String getStringProperty(String name) {
		try {
			FilterProperty property = properties.get(name);
			return (String) property.getValue();
		} catch(Exception e) {
			throw new PropertyNotFoundException("String", name);
		}
	}
	
	public void addRangeProperty(String name, int defaultValue, int min, int step, int max) {
		RangeProperty property = getProperty(name);
		if(property != null) {
			property.value = defaultValue;
			property.min = min;
			property.step = step;
			property.max = max;
		} else {
			property = new RangeProperty(name, defaultValue, min, step, max);
			setProperty(property);
		}
	}
	
	public int getRangeProperty(String name) {
		try {
			FilterProperty property = properties.get(name);
			return (int) property.getValue();
		} catch(Exception e) {
			throw new PropertyNotFoundException("Range", name);
		}
	}
	
	public void addOptionProperty(String name, Object defaultValue, Object... options) {
		OptionProperty property = getProperty(name);
		if(property != null) {
			property.value = defaultValue;
			property.options = options;
		} else {
			property = new OptionProperty(name, defaultValue, options);
			setProperty(property);
		}
	}
	
	public <T> T getOptionProperty(String name) {
		try {
			FilterProperty property = properties.get(name);
			@SuppressWarnings("unchecked")
			T value = (T) property.getValue();
			return value;
		} catch(Exception e) {
			throw new PropertyNotFoundException("Option", name);
		}
	}
	
	public Collection<FilterProperty> getProperties() {
		return Collections.unmodifiableCollection(properties.values());
	}

}
