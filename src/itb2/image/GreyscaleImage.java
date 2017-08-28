package itb2.image;

import itb2.utils.Utils;

public class GreyscaleImage extends AbstractImage {
	public static final int GREYSCALE = 0;
	
	public GreyscaleImage(double[][] data) {
		this(Utils.getCaller(0).getClassName(), data);
	}
	
	public GreyscaleImage(String filename, double[][] data) {
		super(filename, data);
	}

	@Override
	protected double[] getRGB(int x, int y) {
		double[][][] data = getData();
		
		double value = data[GREYSCALE][x][y];
		
		return new double[]{value, value, value};
	}

}
