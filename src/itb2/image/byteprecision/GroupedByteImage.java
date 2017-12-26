package itb2.image.byteprecision;

import java.awt.Dimension;
import java.io.Serializable;

import itb2.image.GroupedImage;

/**
 * Image, that lets user set group for each pixel.
 * Pixel of same group have the same color. 
 * 
 * @author Micha Strauch
 */
public class GroupedByteImage extends HsiByteImage implements GroupedImage {
	private static final long serialVersionUID = -2056869939417487245L;
	
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
				groups[i] = new Group((i * maxHue()) / groupCount);
		} else
			throw new RuntimeException("Group count must be at least 2!");
	}
	
	@Override
	public void setGroup(int column, int row, int groupID) {
		Group group = groups[groupID];
		
		for(int chan = 0; chan < 3; chan++)
			data[chan][column][row] = group.hsi[chan];
		
		updateImage();
	}
	
	/**
	 * Contains HSI-values of the group
	 * 
	 * @author Micha Strauch
	 */
	class Group implements Serializable {
		private static final long serialVersionUID = 5350988366575593300L;

		/** ID for black and white color */
		static final int BLACK = -2, WHITE = -1;
		
		/** HSI value of group */
		final byte[] hsi;
		
		/**
		 * Constructs the color from the given ID.
		 * 
		 * @param id
		 */
		Group(int id) {
			if(id == BLACK) {
				hsi = new byte[3];
			} else if(id == WHITE) {
				hsi = new byte[] {0, 0, convert(maxIntensity())};
			} else {
				hsi = new byte[] {
					convert(id),
					convert(maxSaturation()),
					convert(maxIntensity() / 2)
				};
			}
		}
		
	}

}
