package itb2.image;

import itb2.utils.Utils;

public class RgbImage extends AbstractImage {
	public static final int RED = 0, GREEN = 1, BLUE = 2;
	
	public RgbImage(double[][][] data) {
		this(Utils.getCaller(0).getClassName(), data);
	}
	
	public RgbImage(String filename, double[][][] data) {
		super(filename, data);
		if(data.length != 3)
			throw new RuntimeException("data must have three channels (red, green and blue)");
	}

	@Override
	protected double[] getRGB(int x, int y) {
		double[][][] data = getData();
		
		double r = data[RED  ][x][y];
		double g = data[GREEN][x][y];
		double b = data[BLUE ][x][y];
		
		return new double[]{r, g, b};
	}

}
