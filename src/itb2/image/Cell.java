package itb2.image;

import java.util.function.Function;

/**
 * Represents the cell of a {@link Row} or a {@link Column}
 * 
 * @author Micha Strauch
 */
public interface Cell {
	
	/**
	 * Returns the image of this cell
	 * 
	 * @return Image of this cell
	 */
	public Image getImage();
	
	/**
	 * Returns the ID of the channel
	 * 
	 * @return ID of the channel
	 */
	public int getChannelID();
	
	/**
	 * Returns the ID of the row
	 * 
	 * @return ID of the row
	 */
	public int getRowID();
	
	/**
	 * Returns the ID of the column
	 * 
	 * @return ID of the column
	 */
	public int getColumnID();
	
	/**
	 * Returns the value of this cell
	 * 	
	 * @return Value of this cell
	 */
	public double getValue();
	
	/**
	 * Sets the value of this cell
	 * 
	 * @param value  Value of this cell
	 */
	public void setValue(double value);
	
	/**
	 * Modifies the value using the given modifier function.<p>
	 * For example, increase current value by 3:<br>
	 * <code>modifyValue(value -> value + 3);</code>
	 * 
	 * @param modifier Function providing new value
	 */
	default void modifyValue(Function<Double, Double> modifier) {
		double value = getValue();
		value = modifier.apply(value);
		setValue(value);
	}

}
