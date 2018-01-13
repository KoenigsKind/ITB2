package itb2.image;

/**
 * Image giving each group id an individual color.<p>
 * {@value #BLACK} is always colored {@link #BLACK black}<br>
 * {@value #WHITE} is always colored {@link #WHITE white}<br>
 * Values between 1 and 254 will have a custom value,
 * depending on the group count. 
 *
 * @author Micha Strauch
 */
public interface GroupedImage extends Image {
	
	/** Channel containing group id */
	public static final int GROUP_ID = 0;
	
	/** Whether the image should automatically detect the group count */
	public static final int AUTOMATIC_GROUP_COUNT = 0;
	
	/** Special group id with fixed color: black and white. */
	public static final int BLACK = 0, WHITE = 255;
	
	/**
	 * Number of groups
	 * 
	 * @return Number of groups
	 */
	public int getGroupCount();
	
	/**
	 * Sets the group of the given pixel. Equivalent to <br>
	 * {@link #setValue(int, int, int, double) setValue(column, row, GROUP_ID, groupID);}<p>
	 * The group ID is 1 based, the values {@value #BLACK} and {@value #WHITE} are reserved
	 * for {@link #BLACK black} and {@link #WHITE white} color.
	 * 
	 * @param column  Column of the pixel
	 * @param row     Row of the pixel
	 * @param groupID Group of the pixel (1..254) or
	 *                {@link #WHITE white} or {@link #BLACK black}
	 */
	default public void setGroup(int column, int row, int groupID) {
		setValue(column, row, GROUP_ID, groupID);
	}
	
	/**
	 * Returns the group of the given pixel. Equivalent to<br>
	 * {@link #getValue(int, int, int) (int)getValue(column, row, GROUP_ID);}
	 * 
	 * @param column  Column of the pixel
	 * @param row     Row of the pixel
	 * @param groupID Group of the pixel
	 */
	default public int getGroup(int column, int row) {
		return (int)getValue(column, row, GROUP_ID);
	}
	
}
