package itb2.image;

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

}
