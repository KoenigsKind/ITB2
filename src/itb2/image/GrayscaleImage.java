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
		
		for(int x = 0; x < size.width; x++)
			for(int y = 0; y < size.height; y++)
				this.data[x][y][0] = data[x][y];
	}
	
	public GrayscaleImage(Channel channel) {
		super(channel.getWidth(), channel.getHeight(), 1);
		
		for(int x = 0; x < size.width; x++)
			for(int y = 0; y < size.height; y++)
				this.data[x][y][0] = channel.getValue(x, y);
	}

	@Override
	protected double[] getRGB(int x, int y) {
		double value = data[x][y][GREYSCALE];
		
		return new double[]{value, value, value};
	}

}
