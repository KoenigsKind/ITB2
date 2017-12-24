package itb2.filter;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import itb2.filter.FilterProperty.Option;
import itb2.filter.FilterProperty.Range;

public class FilterProperties extends LinkedHashMap<String, FilterProperty<?>> {
	private static final long serialVersionUID = -7985094039685903402L;

	protected <T> T getValue(String name, Class<T> classOfT) {
		FilterProperty<?> property = get(name);
		
		if(property != null) try {
			return classOfT.cast(property.getValue());
		} catch(Exception e) {}
		
		throw new PropertyNotFoundException(classOfT.getSimpleName(), name);
	}
	
	protected <T> void setValue(String name, T defaultValue, Class<T> classOfT) {
		setValue(name, defaultValue, classOfT, true);
	}
	
	protected <T> void setValue(String name, T defaultValue, Class<T> classOfT, boolean mayChangeValue) {
		FilterProperty<T> property = new FilterProperty<T>() {
			private T value = defaultValue;
			
			@Override
			public String getName() {
				return name;
			}
			
			@Override
			public Class<T> getClassOfT() {
				return classOfT;
			}
			
			@Override
			public T getValue() {
				return value;
			}
			
			@Override
			public void setValue(T value) throws UnsupportedOperationException {
				if(!mayChangeValue)
					throw new UnsupportedOperationException();
				this.value = value;
			}
		};
		
		put(name, property);
	}
	
	public void addBooleanProperty(String name, boolean defaultValue) {
		setValue(name, defaultValue, Boolean.class);
	}
	
	public boolean getBooleanProperty(String name) {
		return getValue(name, Boolean.class);
	}
	
	public void addDoubleProperty(String name, double defaultValue) {
		setValue(name, defaultValue, Double.class);
	}
	
	public double getDoubleProperty(String name) {
		return getValue(name, Double.class);
	}
	
	public void addIntegerProperty(String name, int defaultValue) {
		setValue(name, defaultValue, Integer.class);
	}
	
	public int getIntegerProperty(String name) {
		return getValue(name, Integer.class);
	}
	
	public void addStringProperty(String name, String defaultValue) {
		setValue(name, defaultValue, String.class);
	}
	
	public String getStringProperty(String name) {
		return getValue(name, String.class);
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
		
		setValue(name, range, Range.class, false);
	}
	
	public int getRangeProperty(String name) {
		Range range = getValue(name, Range.class);
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
		
		setValue(name, option, Option.class, false);
	}
	
	public <T> T getOptionProperty(String name) {
		Option option = getValue(name, Option.class);
		try {
			@SuppressWarnings("unchecked")
			T value = (T) option.getSelection();
			return value;
		} catch(ClassCastException e) {
			throw new PropertyNotFoundException(Option.class.getSimpleName(), name);
		}
	}
	
	public Collection<FilterProperty<?>> getProperties() {
		return Collections.unmodifiableCollection(values());
	}

}
