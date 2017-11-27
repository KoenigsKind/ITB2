package itb2.image;

public interface GroupedImage extends Image {
	
	/**
	 * Sets the group of the given pixel.
	 * 
	 * @param row     Row of the pixel
	 * @param column  Column of the pixel
	 * @param groupID Group of the pixel
	 */
	public void setGroup(int row, int column, int groupID);
	
}
