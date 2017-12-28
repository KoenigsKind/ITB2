package itb2.image.doubleprecision;

import java.awt.Dimension;

import itb2.image.GroupedImage;

/**
 * Image, that lets user set group for each pixel.
 * Pixel of same group have the same color. 
 * 
 * @author Micha Strauch
 */
class GroupedDoubleImage extends AbstractDoubleImage implements GroupedImage {
	private static final long serialVersionUID = -2395222062447381765L;
	
	/** Number of groups */
	private final int groupCount;
	
	/**
	 * Constructs image with given size and number of groups.
	 * 
	 * @param width      Width of the image
	 * @param height     Height of the image
	 * @param groupCount Number of groups
	 */
	GroupedDoubleImage(int width, int height, int groupCount) {
		super(width, height, 1);
		
		if(groupCount < 2)
			throw new RuntimeException("Group count must be at least 2!");
		
		this.groupCount = groupCount;
	}
	
	/**
	 * Constructs image with given size and number of groups.
	 * 
	 * @param size       Size of the image
	 * @param groupCount Number of groups
	 */
	GroupedDoubleImage(Dimension size, int groupCount) {
		this(size.width, size.height, groupCount);
	}
	
	@Override
	public int getGroupCount() {
		return groupCount;
	}
	
	@Override
	public void setValue(int column, int row, double... values) {
		for(double value : values)
			check(value);
		
		super.setValue(column, row, values);
	}
	
	@Override
	public void setValue(int column, int row, int channel, double value) {
		check(value);
		
		super.setValue(column, row, channel, value);
	}

	@Override
	protected double[] getRGB(int column, int row) {
		double group = data[GROUP_ID][column][row];
		
		if(groupCount == 2)
			return hsv2rgb(0, 0, group < .5 ? 0 : 1);
		
		return hsv2rgb(360 * group / groupCount, 1, 1);
	}
	
	/**
	 * Checks whether the given value is a valid group id
	 * 
	 * @param value Value to check
	 */
	private void check(double value) {
		if(value < 0 || value >= groupCount)
			throw new RuntimeException("Value must be between 0 and " + (groupCount - 1));
	}
	
	/**
	 * Converts HSV values to RGB values
	 * 
	 * @param h Hue
	 * @param s Saturation
	 * @param v Value
	 * @return Array with RGB-values
	 */
	private double[] hsv2rgb(double h, double s, double v) {
		double c = v * s; // chroma
		double x = c * (1 - Math.abs(((h/60)%2) - 1));
		double m = v - c;
		
		double[] rgb;
		
		if(h < 60)
			rgb = new double[] {c, x, 0};
		else if(h < 120)
			rgb = new double[] {x, c, 0};
		else if(h < 180)
			rgb = new double[] {0, c, x};
		else if(h < 240)
			rgb = new double[] {0, x, c};
		else if(h < 300)
			rgb = new double[] {x, 0, c};
		else
			rgb = new double[] {c, 0, x};
		
		for(int i = 0; i < 3; i++)
			rgb[i] = 255 * (rgb[i] + m);
		
		return rgb;
	}

}
