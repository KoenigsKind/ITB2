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
	
	private <T extends FilterProperty> T getProperty(String name, Class<T> classOfT) {
		FilterProperty property = properties.get(name);
		
		if(property != null && classOfT.isInstance(property))
			return classOfT.cast(property);
		
		return null;
	}
	
	private void setProperty(FilterProperty property) {
		properties.put(property.name, property);
	}
	
	public void addBooleanProperty(String name, boolean defaultValue) {
		BooleanProperty property = getProperty(name, BooleanProperty.class);
		if(property != null)
			property.value = defaultValue;
		else {
			property = new BooleanProperty(name, defaultValue);
			setProperty(property);
		}
	}
	
	public boolean getBooleanProperty(String name) {
		BooleanProperty property = getProperty(name, BooleanProperty.class);
		
		if(property == null)
			throw new PropertyNotFoundException("boolean", name);
		
		return property.getValue();
	}
	
	public void addDoubleProperty(String name, double defaultValue) {
		DoubleProperty property = getProperty(name, DoubleProperty.class);
		if(property != null)
			property.value = defaultValue;
		else {
			property = new DoubleProperty(name, defaultValue);
			setProperty(property);
		}
	}
	
	public double getDoubleProperty(String name) {
		DoubleProperty property = getProperty(name, DoubleProperty.class);
		
		if(property == null)
			throw new PropertyNotFoundException("double", name);;
		
		return property.getValue();
	}
	
	public void addIntegerProperty(String name, int defaultValue) {
		IntegerProperty property = getProperty(name, IntegerProperty.class);
		if(property != null)
			property.value = defaultValue;
		else {
			property = new IntegerProperty(name, defaultValue);
			setProperty(property);
		}
	}
	
	public int getIntegerProperty(String name) {
		IntegerProperty property = getProperty(name, IntegerProperty.class);
		
		if(property == null)
			throw new PropertyNotFoundException("integer", name);
		
		return property.getValue();
	}
	
	public void addStringProperty(String name, String defaultValue) {
		StringProperty property = getProperty(name, StringProperty.class);
		if(property != null)
			property.value = defaultValue;
		else {
			property = new StringProperty(name, defaultValue);
			setProperty(property);
		}
	}
	
	public String getStringProperty(String name) {
		StringProperty property = getProperty(name, StringProperty.class);
		
		if(property == null)
			throw new PropertyNotFoundException("String", name);
		
		return property.getValue();
	}
	
	public void addRangeProperty(String name, int defaultValue, int min, int step, int max) {
		RangeProperty property = getProperty(name, RangeProperty.class);
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
		RangeProperty property = getProperty(name, RangeProperty.class);
		
		if(property == null)
			throw new PropertyNotFoundException("Range", name);
		
		return property.getValue();
	}
	
	public void addOptionProperty(String name, Object defaultValue, Object... options) {
		OptionProperty property = getProperty(name, OptionProperty.class);
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
