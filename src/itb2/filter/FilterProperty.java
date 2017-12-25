package itb2.filter;

/**
 * Property for a {@link Filter}.<p>
 * Currently implemented value types <i>T</i> of FilterProperties:<br>
 * Boolean, Double, Integer, String, FilterProperty.Range, FilterProperty.Option
 *
 * @author Micha Strauch
 */
public interface FilterProperty {
	
	/** Returns the name of this property */
	public String getName();
	
	/** Current value of this property */
	public Object getValue();
	
	/**
	 * Sets the value of this property.<p>
	 * Might throw an exception, if the value itself should be modified.
	 * For example: FilterProperty.Range or FilterProperty.Option
	 * 
	 * @param value Value to set
	 * @throws UnsupportedOperationException If the value itself should be modified
	 * @throws ClassCastException If the given value is of the wrong type
	 */
	public void setValue(Object value) throws UnsupportedOperationException, ClassCastException;
	
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
