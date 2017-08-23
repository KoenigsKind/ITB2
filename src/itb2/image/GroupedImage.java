package itb2.image;

import itb2.utils.Utils;

public class GroupedImage extends HsiImage {
	private final Group[] groups;
	
	public GroupedImage(int width, int height, int groupCount) {
		this(Utils.getCaller(0).getClassName(), width, height, groupCount);
	}
	
	public GroupedImage(String filename, int width, int height, int groupCount) {
		super(filename, new double[3][width][height]);
		
		if(groupCount == 2)
			groups = new Group[]{new Group(Group.BLACK), new Group(Group.WHITE)};
		else if(groupCount > 2) {
			groups = new Group[groupCount];
			for(int i = 0; i < groupCount; i++)
				groups[i] = new Group(i * 365. / groupCount);
		} else
			throw new RuntimeException("Group count must be at least 2!");
	}
	
	public void setGroup(int x, int y, int groupID) {
		double[][][] data = getData();
		Group group = groups[groupID];
		
		for(int k = 0; k < 3; k++)
			data[k][x][y] = group.hsi[k];
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
