package itb2.filter;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import itb2.filter.FilterProperty.Option;
import itb2.filter.FilterProperty.Range;

public class FilterProperties extends LinkedHashMap<String, FilterProperty> {
	private static final long serialVersionUID = -7985094039685903402L;

	public <T> T getProperty(String name) {
		FilterProperty property = get(name);
		
		if(property != null) try {
			@SuppressWarnings("unchecked")
			T value = (T) property.getValue();
			return value;
		} catch(Exception e) {}
		
		throw new PropertyNotFoundException(name);
	}
	
	public void addProperty(String name, Object defaultValue) {
		addProperty(name, defaultValue, true);
	}
	
	public void addProperty(String name, Object defaultValue, boolean mayChangeValue) {
		if(defaultValue == null)
			throw new NullPointerException("defaultValue must not be null");
		
		FilterProperty property = new FilterProperty() {
			private final Class<?> valueClass = defaultValue.getClass();
			private Object value = defaultValue;
			
			@Override
			public String getName() {
				return name;
			}
			
			@Override
			public Object getValue() {
				return value;
			}
			
			@Override
			public void setValue(Object value) throws UnsupportedOperationException {
				if(!mayChangeValue)
					throw new UnsupportedOperationException();
				
				if(!valueClass.isInstance(value))
					throw new ClassCastException();
				
				this.value = value;
			}
		};
		
		put(name, property);
	}
	
	public void addRangeProperty(String name, int defaultValue, int min, int step, int max) {
		Range range = new Range() {
			private int selection = defaultValue;
			
			@Override
			public void setSelection(int selection) {
				this.selection = selection;
			}
			
			@Override
			public int getSelection() {
				return selection;
			}
			
			@Override
			public int getStep() {
				return step;
			}
			
			@Override
			public int getMin() {
				return min;
			}
			
			@Override
			public int getMax() {
				return max;
			}
		};
		
		addProperty(name, range, false);
	}
	
	public int getRangeProperty(String name) {
		Range range = getProperty(name);
		return range.getSelection();
	}
	
	public void addOptionProperty(String name, Object defaultValue, Object... options) {
		Option option = new Option() {
			private Object selection = defaultValue;
			
			@Override
			public void setSelection(Object selection) {
				this.selection = selection;
			}
			
			@Override
			public Object getSelection() {
				return selection;
			}
			
			@Override
			public Object[] getOptions() {
				return options;
			}
		};
		
		addProperty(name, option, false);
	}
	
	public <T> T getOptionProperty(String name) {
		Option option = getProperty(name);
		try {
			@SuppressWarnings("unchecked")
			T value = (T) option.getSelection();
			return value;
		} catch(ClassCastException e) {
			throw new PropertyNotFoundException(name, Option.class);
		}
	}
	
	public void addBooleanProperty(String name, boolean defaultValue) {
		addProperty(name, defaultValue);
	}
	
	public boolean getBooleanProperty(String name) {
		return getProperty(name);
	}
	
	public void addDoubleProperty(String name, double defaultValue) {
		addProperty(name, defaultValue);
	}
	
	public double getDoubleProperty(String name) {
		return getProperty(name);
	}
	
	public void addIntegerProperty(String name, int defaultValue) {
		addProperty(name, defaultValue);
	}
	
	public int getIntegerProperty(String name) {
		return getProperty(name);
	}
	
	public void addStringProperty(String name, String defaultValue) {
		addProperty(name, defaultValue);
	}
	
	public String getStringProperty(String name) {
		return getProperty(name);
	}
	
	public Collection<FilterProperty> getProperties() {
		return Collections.unmodifiableCollection(values());
	}

}
