package itb2.filter;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * Collection of {@link FilterProperty filter properties}.
 * Each property is identified by it's name.
 *
 * @author Micha Strauch
 */
public class FilterProperties extends LinkedHashMap<String, FilterProperty> {
	private static final long serialVersionUID = -7985094039685903402L;

	/**
	 * Returns the value of the property with the given value.
	 * 
	 * @param name Name of the property
	 * 
	 * @return Value of the property
	 * 
	 * @throws PropertyNotFoundException If no matching property was found.
	 */
	public <T> T getProperty(String name) throws PropertyNotFoundException {
		FilterProperty property = get(name);
		
		if(property != null) try {
			@SuppressWarnings("unchecked")
			T value = (T) property.getValue();
			return value;
		} catch(Exception e) {}
		
		throw new PropertyNotFoundException(name);
	}
	
	/**
	 * Adds the property with the given name and value.
	 * 
	 * @param name         Name of the property
	 * @param defaultValue Value of the property
	 */
	public void addProperty(String name, Object defaultValue) {
		addProperty(name, defaultValue, true);
	}
	
	/**
	 * Adds the property with the given name and value. <code>mayChangeValue</code>
	 * determines, whether the value can be changed or rather the value itself should
	 * be modified.
	 * 
	 * @see FilterProperty.Option
	 * @see FilterProperty.Range
	 * 
	 * @param name           Name of the property
	 * @param defaultValue   Value of the property
	 * @param mayChangeValue If the value may be changed (if set to false, the value
	 *                       itself should be modified)
	 */
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
	
	/**
	 * Adds a range property to this filter.<p>
	 * Example: <code>addRangeProperty("Foo", 5, 3, 2, 11);</code><br>
	 * Value may be an odd number between 3 and 11. By default it's set
	 * to 5.
	 * 
	 * @param name         Name of the property
	 * @param defaultValue Default value of the property
	 * @param min          Minimum value
	 * @param step         Step size
	 * @param max          Maximum value
	 */
	public void addRangeProperty(String name, int defaultValue, int min, int step, int max) {
		FilterProperty.Range range = new FilterProperty.Range() {
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
	
	/**
	 * Returns the current value for the range property with the given name.
	 * 
	 * @param name Name of the property
	 * 
	 * @return Value of the property
	 * 
	 * @throws PropertyNotFoundException If no matching property was found.
	 */
	public int getRangeProperty(String name) throws PropertyNotFoundException {
		FilterProperty.Range range = getProperty(name);
		return range.getSelection();
	}
	
	/**
	 * Adds an option property to this filter.<p>
	 * Example: <code>addOptionProperty("Foobar", "Bravo", "Alpha", "Bravo, "Charlie");</code><br>
	 * Value can either be <i>Alpha</i>, <i>Bravo</i> or <i>Charlie</i>. By default the
	 * value is set to <i>Bravo</i>.
	 * 
	 * @param name         Name of the property
	 * @param defaultValue Default value of the property
	 * @param options      Possible values to select from
	 */
	public void addOptionProperty(String name, Object defaultValue, Object... options) {
		FilterProperty.Option option = new FilterProperty.Option() {
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
	
	/**
	 * Returns the current value for the option property with the given name.
	 * 
	 * @param name Name of the property
	 * 
	 * @return Value of the property
	 * 
	 * @throws PropertyNotFoundException If no matching property was found.
	 */
	public <T> T getOptionProperty(String name) throws PropertyNotFoundException {
		FilterProperty.Option option = getProperty(name);
		try {
			@SuppressWarnings("unchecked")
			T value = (T) option.getSelection();
			return value;
		} catch(ClassCastException e) {
			throw new PropertyNotFoundException(name, FilterProperty.Option.class);
		}
	}
	
	/**
	 * Adds a boolean property to this filter.
	 * 
	 * @param name         Name of this property
	 * @param defaultValue Default value of this property
	 */
	public void addBooleanProperty(String name, boolean defaultValue) {
		addProperty(name, defaultValue);
	}
	
	/**
	 * Returns the current value for the boolean property with the given name.
	 * 
	 * @param name Name of the property
	 * 
	 * @return Value of the property
	 * 
	 * @throws PropertyNotFoundException If no matching property was found.
	 */
	public boolean getBooleanProperty(String name) throws PropertyNotFoundException {
		return getProperty(name);
	}
	
	/**
	 * Adds a double property to this filter.
	 * 
	 * @param name         Name of this property
	 * @param defaultValue Default value of this property
	 */
	public void addDoubleProperty(String name, double defaultValue) {
		addProperty(name, defaultValue);
	}
	
	/**
	 * Returns the current value for the double property with the given name.
	 * 
	 * @param name Name of the property
	 * 
	 * @return Value of the property
	 * 
	 * @throws PropertyNotFoundException If no matching property was found.
	 */
	public double getDoubleProperty(String name) throws PropertyNotFoundException {
		return getProperty(name);
	}
	
	/**
	 * Adds an integer property to this filter.
	 * 
	 * @param name         Name of this property
	 * @param defaultValue Default value of this property
	 */
	public void addIntegerProperty(String name, int defaultValue) {
		addProperty(name, defaultValue);
	}
	
	/**
	 * Returns the current value for the integer property with the given name.
	 * 
	 * @param name Name of the property
	 * 
	 * @return Value of the property
	 * 
	 * @throws PropertyNotFoundException If no matching property was found.
	 */
	public int getIntegerProperty(String name) throws PropertyNotFoundException {
		return getProperty(name);
	}
	
	/**
	 * Adds a string property to this filter.
	 * 
	 * @param name         Name of this property
	 * @param defaultValue Default value of this property
	 */
	public void addStringProperty(String name, String defaultValue) {
		addProperty(name, defaultValue);
	}
	
	/**
	 * Returns the current value for the string property with the given name.
	 * 
	 * @param name Name of the property
	 * 
	 * @return Value of the property
	 * 
	 * @throws PropertyNotFoundException If no matching property was found.
	 */
	public String getStringProperty(String name) throws PropertyNotFoundException {
		return getProperty(name);
	}
	
	/**
	 * Returns an unmodifiable collection of the stored {@link FilterProperty filter properties}.
	 * 
	 * @return Collection of properties
	 */
	public Collection<FilterProperty> getProperties() {
		return Collections.unmodifiableCollection(values());
	}

}
