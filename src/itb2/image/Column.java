package itb2.image;

/**
 * Represents the column of a {@link Channel}
 * 
 * @author Micha Strauch
 */
public interface Column extends Iterable<Cell> {
	
	/**
	 * Returns the image of this column
	 * 
	 * @return Image of this column
	 */
	public Image getImage();
	
	/**
	 * Returns the ID of the channel
	 * 
	 * @return ID of the channel
	 */
	public int getChannelID();
	
	/**
	 * Returns the ID of this column
	 * 
	 * @return ID of this column
	 */
	public int getColumnID();
	
	/**
	 * Returns the height of this column
	 * 
	 * @return height
	 */
	public int getHeight();
	
	/**
	 * Returns the value of the given pixel
	 * 	
	 * @param row Row of the pixel
	 * @return Value of the pixel
	 */
	public double getValue(int row);
	
	/**
	 * Sets the value of the given pixel
	 * 
	 * @param row   Row of the pixel
	 * @param value Value of the pixel
	 */
	public void setValue(int row, double value);
	
	/**
	 * Returns the cell for the given row
	 * 
	 * @param column Row of the cell
	 * @return cell
	 */
	public Cell getCell(int row);

}
