package itb2.image;

import java.awt.Dimension;

public class GroupedImage extends HsiImage {
	private final Group[] groups;
	
	public GroupedImage(int width, int height, int groupCount) {
		this(new Dimension(width, height), groupCount);
	}
	
	public GroupedImage(Dimension size, int groupCount) {
		super(size);
		
		if(groupCount == 2)
			groups = new Group[]{new Group(Group.BLACK), new Group(Group.WHITE)};
		else if(groupCount > 2) {
			groups = new Group[groupCount];
			for(int i = 0; i < groupCount; i++)
				groups[i] = new Group(i * 365. / groupCount);
		} else
			throw new RuntimeException("Group count must be at least 2!");
	}
	
	public void setGroup(int row, int column, int groupID) {
		Group group = groups[groupID];
		
		for(int c = 0; c < 3; c++)
			data[row][column][c] = group.hsi[c];
	}
	
	class Group {
		static final double BLACK = -2, WHITE = -1; 
		final double[] hsi;
		
		Group(double id) {
			if(id == BLACK) {
				hsi = new double[]{0, 0, 0};
			} else if(id == WHITE) {
				hsi = new double[]{0, 0, 255};
			} else {
				hsi = new double[]{id, 100, 255};
			}
		}
		
	}

}
