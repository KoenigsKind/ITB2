package itb2.image;

import java.awt.Dimension;
import java.util.function.Function;

/**
 * Represents the channel of an {@link Image}
 * 
 * @author Micha Strauch
 */
public interface Channel {
	
	/**
	 * Returns the image of this channel
	 * 
	 * @return Image of this channel
	 */
	public Image getImage();
	
	/**
	 * Returns the ID of this channel
	 * 
	 * @return ID of this channel
	 */
	public int getChannelID();
	
	/**
	 * Returns the width of this channel
	 * 
	 * @return width
	 */
	public int getWidth();
	
	/**
	 * Returns the height of this channel
	 * 
	 * @return height
	 */
	public int getHeight();
	
	/**
	 * Returns the size of this channel
	 * 
	 * @return size
	 */
	public Dimension getSize();
	
	/**
	 * Returns the value of the given pixel
	 * 	
	 * @param column Column of the pixel
	 * @param row    Row of the pixel
	 * 
	 * @return Value of the pixel
	 */
	public double getValue(int column, int row);
	
	/**
	 * Sets the value of the given pixel
	 * 
	 * @param column Column of the pixel
	 * @param row    Row of the pixel
	 * @param value  Value of the pixel
	 */
	public void setValue(int column, int row, double value);
	
	/**
	 * Returns the row for the given ID
	 * 
	 * @param row ID of the row
	 * 
	 * @return row
	 */
	public Row getRow(int row);
	
	/**
	 * Returns the column for the given ID
	 * 
	 * @param column ID of the column
	 * 
	 * @return column
	 */
	public Column getColumn(int column);
	
	/**
	 * Lets one iterate over all rows
	 * 
	 * @return Iterable for a for-each-loop
	 */
	public Iterable<Row> rows();
	
	/**
	 * Lets one iterate over all columns
	 * 
	 * @return Iterable for a for-each-loop
	 */
	public Iterable<Column> columns();
	
	/**
	 * Modifies the value using the given modifier function.<p>
	 * For example, increase current value by 3:<br>
	 * <code>modifyValue(col, row, value -> value + 3);</code>
	 * 
	 * @param column   Column of the pixel
	 * @param row      Row of the pixel
	 * @param modifier Function providing new value
	 */
	default void modifyValue(int column, int row, Function<Double, Double> modifier) {
		double value = getValue(column, row);
		value = modifier.apply(value);
		setValue(column, row, value);
	}
	
}
