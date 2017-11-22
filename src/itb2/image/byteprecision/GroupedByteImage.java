package itb2.image.byteprecision;

import java.awt.Dimension;

/**
 * Image, that lets user set group for each pixel.
 * Pixel of same group have the same color. 
 * 
 * @author Micha Strauch
 */
public class GroupedByteImage extends HsiByteImage {
	
	/** Available groups */
	private final Group[] groups;
	
	/**
	 * Constructs image with given size and number of groups.
	 * 
	 * @param width      Width of the image
	 * @param height     Height of the image
	 * @param groupCount Number of groups
	 */
	public GroupedByteImage(int width, int height, int groupCount) {
		this(new Dimension(width, height), groupCount);
	}
	
	/**
	 * Constructs image with given size and number of groups.
	 * 
	 * @param size       Size of the image
	 * @param groupCount Number of groups
	 */
	public GroupedByteImage(Dimension size, int groupCount) {
		super(size);
		
		if(groupCount == 2)
			groups = new Group[]{new Group(Group.BLACK), new Group(Group.WHITE)};
		else if(groupCount > 2) {
			groups = new Group[groupCount];
			for(int i = 0; i < groupCount; i++)
				groups[i] = new Group(i * MAX_HUE / groupCount);
		} else
			throw new RuntimeException("Group count must be at least 2!");
	}
	
	/**
	 * Sets the group of the given pixel.
	 * 
	 * @param row     Row of the pixel
	 * @param column  Column of the pixel
	 * @param groupID Group of the pixel
	 */
	public void setGroup(int row, int column, int groupID) {
		Group group = groups[groupID];
		data[row][column] = group.hsi;
	}
	
	/**
	 * Contains HSI-values of the group
	 * 
	 * @author Micha Strauch
	 */
	class Group {
		/** ID for black and white color */
		static final int BLACK = -2, WHITE = -1;
		
		/** HSI value of group */
		final int hsi;
		
		/**
		 * Constructs the color from the given ID.
		 * 
		 * @param id
		 */
		Group(int id) {
			if(id == BLACK) {
				hsi = 0;
			} else if(id == WHITE) {
				hsi = (MAX_INTENSITY & 0xFF) << 24;
			} else {
				id &= 0xFF;
				id |= (MAX_SATURATION & 0xFF) << 16;
				id |= (MAX_INTENSITY & 0xFF) << 24;
				
				hsi = id;
			}
		}
		
	}

}
