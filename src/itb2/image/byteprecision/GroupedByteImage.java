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

	/** Number of groups or 0 for automatic */
	private final int groupCount;
	
	/** Map binding group id to hue value */
	private final Map<Integer, double[]> groups;
	
	/**
	 * Constructs image with given size and number of groups.
	 * 
	 * @param width      Width of the image
	 * @param height     Height of the image
	 * @param groupCount Number of groups
	 */
	GroupedByteImage(int width, int height, int groupCount) {
		super(width, height, 1);
		
		if( (groupCount < 2 && groupCount != AUTOMATIC_GROUP_COUNT) || groupCount > 255 )
			throw new RuntimeException("Group count must be between 2 - 255 or " + AUTOMATIC_GROUP_COUNT + " for automatic!");
		
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
	}
	
	@Override
	public void setValue(int column, int row, int channel, double value) {
		check(value);
		
		super.setValue(column, row, channel, value);
	}
	
	@Override
	public BufferedImage asBufferedImage() {
		if(groups.isEmpty() || groupCount == AUTOMATIC_GROUP_COUNT) {
			
			if(groupCount == 2) {
				setHsv(0, 0, 0, 0);
				setHsv(1, 0, 0, 1);
			} else if(groupCount > 2) {
				for(int i = 0; i < groupCount; i++)
					setHsv(i, i * 360. / groupCount, 1, 1);
			} else {
				Set<Byte> values = new TreeSet<>();
				
				for(byte[] bb : data[GROUP_ID])
					for(byte b : bb)
						values.add(b);
				
				if(values.size() == 2) {
					int i = 0;
					for(byte value : values)
						setHsv(value & 0xFF, 0, 0, i++);
				} else {
					int i = 0;
					double delta = 360. / values.size();
					for(byte value : values)
						setHsv(value & 0xFF, delta * i++, 1, 1);
				}
			}
			
		}
		return super.asBufferedImage();
	}

	@Override
	protected double[] getRGB(int column, int row) {
		int group = data[GROUP_ID][column][row] & 0xFF;
		
		double[] hsv = groups.get(group);
		
		return hsv2rgb(hsv[0], hsv[1], hsv[2]);
	}
	
	/**
	 * Checks whether the given value is a valid group id
	 * 
	 * @param value Value to check
	 */
	private void check(double value) {
		int maxVal = groupCount == AUTOMATIC_GROUP_COUNT ? 255 : groupCount - 1;
		
		if(value < 0 || value > maxVal)
			throw new RuntimeException("Value must be between 0 and " + maxVal);
	}
	
	/**
	 * Sets the HSV value for the given group ID
	 * 
	 * @param id Group ID
	 * @param h  Hue
	 * @param s  Saturation
	 * @param v  Value
	 */
	private void setHsv(int id, double... hsv) {
		groups.put(id, hsv);
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
