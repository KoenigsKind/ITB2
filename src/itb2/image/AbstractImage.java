package itb2.image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractImage implements Image {
	private final List<Selection> selections;
	private final String filename;
	private final double[][][] data;
	private final int width, height, channelCount;
	
	public AbstractImage(String filename, double[][]... data) {
		this.selections = new LinkedList<>();
		this.filename = filename;
		this.data = data;
		this.channelCount = data.length;
		this.width = channelCount == 0 ? 0 : data[0].length;
		this.height = width == 0 ? 0 : data[0][0].length;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getChannelCount() {
		return channelCount;
	}

	@Override
	public double[][][] getData() {
		return data;
	}

	@Override
	public List<Selection> getSelections() {
		return selections;
	}

	@Override
	public String getFileName() {
		return filename;
	}

	@Override
	public BufferedImage asBufferedImage() {
		double[][] samples = new double[3][width * height]; 
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int index = x + width * y;
				
				double[] rgb = getRGB(x, y);
				for(int k = 0; k < 3; k++)
					samples[k][index] = rgb[k];
			}
		}
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		
		for(int k = 0; k < 3; k++)
			raster.setSamples(0, 0, width, height, k, samples[k]);
		
		return image;
	}
	
	protected abstract double[] getRGB(int x, int y);

}
