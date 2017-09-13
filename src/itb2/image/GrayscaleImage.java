package itb2.image;

import java.awt.Dimension;

public class GrayscaleImage extends AbstractImage {
	public static final int GREYSCALE = 0;
	
	public GrayscaleImage(int width, int height) {
		super(width, height, 1);
	}
	
	public GrayscaleImage(Dimension size) {
		super(size, 1);
	}
	
	public GrayscaleImage(double[][] data) {
		this(data.length, data[0].length);
		
		for(int row = 0; row < size.height; row++)
			for(int col = 0; col < size.width; col++)
				this.data[row][col][0] = data[row][col];
	}
	
	public GrayscaleImage(Channel channel) {
		super(channel.getWidth(), channel.getHeight(), 1);
		
		for(int row = 0; row < size.height; row++)
			for(int col = 0; col < size.width; col++)
				this.data[row][col][0] = channel.getValue(row, col);
	}

	@Override
	protected double[] getRGB(int row, int column) {
		double value = data[row][column][GREYSCALE];
		
		return new double[]{value, value, value};
	}

}
