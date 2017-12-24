package itb2.filter;

/**
 * Property for a {@link Filter}.<p>
 * Currently implemented types <i>T</i> of FilterProperties:<br>
 * Boolean, Double, Integer, String, FilterProperty.Range, FilterProperty.Option
 *
 * @author Micha Strauch
 * @param <T> Type of FilterProperty (see above)
 */
public interface FilterProperty<T> {
	
	/** Current value of this property */
	public T getValue();
	
	/**
	 * Sets the value of this property.<p>
	 * Might throw an exception, if the value itself should be modified.
	 * For example: FilterProperty.Range or FilterProperty.Option
	 * 
	 * @param value Value to set
	 * @throws UnsupportedOperationException If the value itself should be modified
	 */
	public void setValue(T value) throws UnsupportedOperationException;
	
	/** Returns the name of this property */
	public String getName();
	
	/** Class of T (the class of the value) */
	public Class<T> getClassOfT();
	
	/**
	 * Sets the value of this property.<p>
	 * Might throw an exception, if the value itself should be modified.
	 * For example: FilterProperty.Range or FilterProperty.Option<p>
	 * Tries to convert the value into the type of this property;
	 * if not possible it throws a {@link ClassCastException}.
	 * 
	 * @param value Value to set
	 * @throws UnsupportedOperationException If the value itself should be modified
	 * @throws ClassCastException If the value is of the wrong type
	 */
	default public void setCastedValue(Object value) throws UnsupportedOperationException, ClassCastException {
		T castedValue = getClassOfT().cast(value);
		setValue(castedValue);
	}
	
	/**
	 * Value to use, if this property represents a range of integers. 
	 *
	 * @author Micha Strauch
	 */
	public interface Range {
		/** Minimum value (may be negative) */
		public int getMin();
		/** Step size (must be 1 or higher) */
		public int getStep();
		/** Maximum value (may be negative) */
		public int getMax();
		
		/** Selected value */
		public int getSelection();
		/** Must be between min and max, and the difference to min must be a multiple of steps. */
		public void setSelection(int selection);
	}
	
	/**
	 * Value to use, if this property represents a list of objects to choose from.
	 *
	 * @author Micha Strauch
	 */
	public interface Option {
		/** Options to choose from */
		public Object[] getOptions();
		
		/** Chosen option */
		public Object getSelection();
		
		/** Sets the chosen option */
		public void setSelection(Object selection);
	}
	
}
