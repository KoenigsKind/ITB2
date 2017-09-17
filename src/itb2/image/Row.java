package itb2.image;

/**
 * Represents the row of a {@link Channel}
 * 
 * @author Micha Strauch
 */
public interface Row extends Iterable<Cell> {
	
	/**
	 * Returns the image of this row
	 * 
	 * @return Image of this row
	 */
	public Image getImage();
	
	/**
	 * Returns the ID of the channel
	 * 
	 * @return ID of the channel
	 */
	public int getChannelID();
	
	/**
	 * Returns the ID of this row
	 * 
	 * @return ID of this row
	 */
	public int getRowID();
	
	/**
	 * Returns the width of this row
	 * 
	 * @return width
	 */
	public int getWidth();
	
	/**
	 * Returns the value of the given pixel
	 * 	
	 * @param column Column of the pixel
	 * @return Value of the pixel
	 */
	public double getValue(int column);
	
	/**
	 * Sets the value of the given pixel
	 * 
	 * @param column Column of the pixel
	 * @param value  Value of the pixel
	 */
	public void setValue(int column, double value);
	
	/**
	 * Returns the cell for the given column
	 * 
	 * @param column Column of the cell
	 * @return cell
	 */
	public Cell getCell(int column);

}
