package itb2.utils;

import java.util.Arrays;

public class Utils {
	
	public static StackTraceElement getCaller(int depth) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		return stack[depth + 3];
	}
	
	public static double[][] copyOf(double[][] data) {
		double[][] copy = new double[data.length][];
		
		for(int x = 0; x < data.length; x++)
			copy[x] = Arrays.copyOf(data[x], data[x].length);
		
		return copy;
	}
	
	public static double[][][] copyOf(double[][][] data) {
		double[][][] copy = new double[data.length][][];
		
		for(int k = 0; k < data.length; k++)
			copy[k] = copyOf(data[k]);
		
		return copy;
	}

}
