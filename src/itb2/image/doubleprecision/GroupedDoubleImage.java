package itb2.image.doubleprecision;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import itb2.image.GroupedImage;

/**
 * Image, that lets user set group for each pixel.
 * Pixel of same group have the same color. 
 * 
 * @author Micha Strauch
 */
class GroupedDoubleImage extends AbstractDoubleImage implements GroupedImage {
	private static final long serialVersionUID = -2395222062447381765L;
	
	/** RGB values for black and white color */
	private static final double[] RGB_BLACK = {0, 0, 0}, RGB_WHITE = {255, 255, 255};
	
	/** Number of groups */
	private final int groupCount;
	
	/** Maximum value, only used when groupCount == AUTO_GROUP_COUNT */
	private double maxValue;
	
	/**
	 * Constructs image with given size and number of groups.
	 * 
	 * @param width      Width of the image
	 * @param height     Height of the image
	 * @param groupCount Number of groups
	 */
	GroupedDoubleImage(int width, int height, int groupCount) {
		super(width, height, 1);
		
		if(groupCount < 1 && groupCount != AUTOMATIC_GROUP_COUNT)
			throw new RuntimeException("Group count must be at least 1  or " + AUTOMATIC_GROUP_COUNT + " for automatic!");
		
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
		maxValue = 0;
	}
	
	@Override
	public void setValue(int column, int row, int channel, double value) {
		check(value);
		
		super.setValue(column, row, channel, value);
		maxValue = 0;
	}
	
	@Override
	public BufferedImage asBufferedImage() {
		if(maxValue == 0) {
			if(groupCount != AUTOMATIC_GROUP_COUNT)
				maxValue = groupCount;
			else {
				for(int col = 0; col < size.width; col++) {
					for(int row = 0; row < size.height; row++) {
						double value = data[GROUP_ID][col][row];
						if(value != BLACK && value != WHITE)
							if(maxValue < value)
								maxValue = value;
					}
				}
			}
		}
		
		return super.asBufferedImage();
	}

	@Override
	protected double[] getRGB(int column, int row) {
		double group = data[GROUP_ID][column][row];
		
		if(group == BLACK)
			return RGB_BLACK;
		
		if(group == WHITE)
			return RGB_WHITE;
		
		double hue = 360 * group / maxValue;
		return getColor(hue % 360);
	}
	
	/**
	 * Checks whether the given value is a valid group id
	 * 
	 * @param value Value to check
	 */
	private void check(double value) {
		if(value < 0)
			throw new RuntimeException("Value must not be negative");
		
		if(value != BLACK && value != WHITE) {
			if(groupCount != AUTOMATIC_GROUP_COUNT && value > groupCount) {
				String message = "Value must not be bigger than " + groupCount;
				if(groupCount < WHITE)
					message += " or it must be white (" + WHITE + ")";
				throw new RuntimeException(message);
			}
		}
	}
	
	/**
	 * Returns the RGB value for the given Hue value.
	 * 
	 * @param hue Hue [0..360]
	 * @return Array with RGB-values
	 */
	private double[] getColor(double hue) {
		double x = 1 - Math.abs( ((hue / 60) % 2) - 1 );
		
		double[] rgb;
		
		if(hue < 60)
			rgb = new double[] {1, x, 0};
		else if(hue < 120)
			rgb = new double[] {x, 1, 0};
		else if(hue < 180)
			rgb = new double[] {0, 1, x};
		else if(hue < 240)
			rgb = new double[] {0, x, 1};
		else if(hue < 300)
			rgb = new double[] {x, 0, 1};
		else
			rgb = new double[] {1, 0, x};
		
		for(int i = 0; i < 3; i++)
			rgb[i] = 255 * rgb[i];
		
		return rgb;
	}

}
