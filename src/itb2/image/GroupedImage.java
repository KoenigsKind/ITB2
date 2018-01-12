package itb2.image;

/**
 * Image giving each group id an individual color.
 *
 * @author Micha Strauch
 */
public interface GroupedImage extends Image {
	
	/** Channel containing group id */
	public static final int GROUP_ID = 0;
	
	/** Whether the image should automatically detect the group count */
	public static final int AUTOMATIC_GROUP_COUNT = 0;
	
	/**
	 * Number of groups
	 * 
	 * @return Number of groups
	 */
	public int getGroupCount();
	
	/**
	 * Sets the group of the given pixel. Equivalent to <br>
	 * {@link #setValue(int, int, int, double) setValue(column, row, GROUP_ID, groupID);}
	 * 
	 * @param column  Column of the pixel
	 * @param row     Row of the pixel
	 * @param groupID Group of the pixel
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
