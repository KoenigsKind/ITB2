package itb2.utils;

public class ImageUtils {
	
	private ImageUtils(){}
	
	public static void scaleLinearly(double[][]... data) {
		double min = min(data);
		double max = max(data);
		
		for(int k = 0; k < data.length; k++) {
			for(int x = 0; x < data[k].length; x++) {
				for(int y = 0; y < data[k][x].length; y++) {
					data[k][x][y] -= min;
					data[k][x][y] *= 255 / max;
				}
			}
		}
	}
	
	public static double max(double[][]... data) {
		double max = Double.MIN_VALUE;
		
		for(double[][] mat : data)
			for(double[] row : mat)
				for(double val : row)
					max = val > max ? val : max;
		
		return max;
	}
	
	public static double min(double[][]... data) {
		double min = Double.MAX_VALUE;
		
		for(double[][] mat : data)
			for(double[] row : mat)
				for(double val : row)
					min = val < min ? val : min;
		
		return min;
	}

}
