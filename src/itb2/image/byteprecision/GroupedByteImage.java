package itb2.image.byteprecision;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import itb2.image.GroupedImage;

/**
 * Image, that lets user set group for each pixel.
 * Pixel of same group have the same color. 
 * 
 * @author Micha Strauch
 */
class GroupedByteImage extends AbstractByteImage implements GroupedImage {
	private static final long serialVersionUID = 3064241941101648155L;
	
	/** RGB values for black and white color */
	private static final double[] RGB_BLACK = {0, 0, 0}, RGB_WHITE = {255, 255, 255};

	/** Number of groups or 0 for automatic */
	private final int groupCount;
	
	/** Map binding group id to hue value */
	private final Map<Integer, double[]> groups;
	
	/** Whether a value changed<p>Rebuild groups if groupCount == AUTOMATIC_GROUP_COUNT */
	private boolean changed;
	
	/**
	 * Constructs image with given size and number of groups.
	 * 
	 * @param width      Width of the image
	 * @param height     Height of the image
	 * @param groupCount Number of groups
	 */
	GroupedByteImage(int width, int height, int groupCount) {
		super(width, height, 1);
		
		if( (groupCount < 1 && groupCount != AUTOMATIC_GROUP_COUNT) || groupCount > 254 )
			throw new RuntimeException("Group count must be between 1 - 254 or " + AUTOMATIC_GROUP_COUNT + " for automatic!");
		
		this.groupCount = groupCount;
		this.groups = new TreeMap<>();
	}
	
	/**
	 * Constructs image with given size and number of groups.
	 * 
	 * @param size       Size of the image
	 * @param groupCount Number of groups
	 */
	GroupedByteImage(Dimension size, int groupCount) {
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
		changed = true;
	}
	
	@Override
	public void setValue(int column, int row, int channel, double value) {
		check(value);
		
		super.setValue(column, row, channel, value);
		changed = false;
	}
	
	@Override
	public BufferedImage asBufferedImage() {
		if(groups.isEmpty() || (changed && groupCount == AUTOMATIC_GROUP_COUNT)) {
			changed = false;
			
			if(groupCount != AUTOMATIC_GROUP_COUNT) {
				for(int i = 0; i < groupCount; i++)
					setHue(i + 1, i * 360. / groupCount);
			} else {
				Set<Byte> values = new TreeSet<>();
				
				for(byte[] bb : data[GROUP_ID])
					for(byte b : bb)
						if(b != BLACK && b != WHITE)
							values.add(b);
				
				int i = 0;
				double delta = 360. / values.size();
				for(byte value : values)
					setHue(value & 0xFF, delta * i++);
			}
			
			groups.put(BLACK, RGB_BLACK);
			groups.put(WHITE, RGB_WHITE);
			
		}
		return super.asBufferedImage();
	}

	@Override
	protected double[] getRGB(int column, int row) {
		int group = data[GROUP_ID][column][row] & 0xFF;
		
		return groups.get(group);
	}
	
	/**
	 * Checks whether the given value is a valid group id
	 * 
	 * @param value Value to check
	 */
	private void check(double value) {
		value = (int) value;
		int maxVal = groupCount == AUTOMATIC_GROUP_COUNT ? 254 : groupCount;
		
		if(value != BLACK && value != WHITE)
			if(value < 1 || value > maxVal)
				throw new RuntimeException(String.format(
						"Value must be between 1 and %d or black (%d) or white (%d)",
						maxVal, BLACK, WHITE));
	}
	
	/**
	 * Sets the HSV value for the given group ID
	 * 
	 * @param id Group ID
	 * @param h  Hue
	 * @param s  Saturation
	 * @param v  Value
	 */
	private void setHue(int id, double hue) {
		double[] rgb = getColor(hue);
		groups.put(id, rgb);
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
