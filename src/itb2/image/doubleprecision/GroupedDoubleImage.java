package itb2.image.doubleprecision;

import java.awt.Dimension;

import itb2.image.GroupedImage;

/**
 * Image, that lets user set group for each pixel.
 * Pixel of same group have the same color. 
 * 
 * @author Micha Strauch
 */
public class GroupedDoubleImage extends HsiDoubleImage implements GroupedImage {
	
	/** Available groups */
	private final Group[] groups;
	
	/**
	 * Constructs image with given size and number of groups.
	 * 
	 * @param width      Width of the image
	 * @param height     Height of the image
	 * @param groupCount Number of groups
	 */
	public GroupedDoubleImage(int width, int height, int groupCount) {
		this(new Dimension(width, height), groupCount);
	}
	
	/**
	 * Constructs image with given size and number of groups.
	 * 
	 * @param size       Size of the image
	 * @param groupCount Number of groups
	 */
	public GroupedDoubleImage(Dimension size, int groupCount) {
		super(size);
		
		if(groupCount == 2)
			groups = new Group[]{new Group(Group.BLACK), new Group(Group.WHITE)};
		else if(groupCount > 2) {
			groups = new Group[groupCount];
			for(int i = 0; i < groupCount; i++)
				groups[i] = new Group((double)(i * MAX_HUE) / groupCount);
		} else
			throw new RuntimeException("Group count must be at least 2!");
	}
	
	@Override
	public void setGroup(int column, int row, int groupID) {
		Group group = groups[groupID];
		
		for(int c = 0; c < 3; c++)
			data[column][row][c] = group.hsi[c];
		
		updateImage();
	}
	
	/**
	 * Contains HSI-values of the group
	 * 
	 * @author Micha Strauch
	 */
	class Group {
		/** ID for black and white color */
		static final double BLACK = -2, WHITE = -1;
		
		/** HSI value of group */
		final double[] hsi;
		
		/**
		 * Constructs the color from the given ID.
		 * 
		 * @param id
		 */
		Group(double id) {
			if(id == BLACK) {
				hsi = new double[]{0, 0, 0};
			} else if(id == WHITE) {
				hsi = new double[]{0, 0, MAX_INTENSITY};
			} else {
				hsi = new double[]{id, MAX_SATURATION, MAX_INTENSITY};
			}
		}
		
	}

}
