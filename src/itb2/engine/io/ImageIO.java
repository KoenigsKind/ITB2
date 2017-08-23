package itb2.engine.io;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import itb2.image.Image;
import itb2.image.RgbImage;

public class ImageIO {
	
	public static Image load(File file) throws IOException {
		if(file.getName().toLowerCase().endsWith(".ppm"))
			return loadPpm(file);
		
		BufferedImage bufferedImage = javax.imageio.ImageIO.read(file);
		Raster raster = bufferedImage.getData();
		int width = raster.getWidth(), height = raster.getHeight();
		double[][][] data = new double[3][width][height];
		double[] rgb = new double[3];
		for(int x = raster.getMinX(); x < width; x++) {
			for(int y = raster.getMinY(); y < height; y++) {
				raster.getPixel(x, y, rgb);
				for(int k = 0; k < 3; k++)
					data[k][x][y] = rgb[k];
			}
		}
		return new RgbImage(file.getPath(), data);
	}
	
	private static Image loadPpm(File file) throws IOException {
		try(InputStream input = new FileInputStream(file)) {
			PortableAnymapReader reader = new PortableAnymapReader(input);
			return reader.read();
		}
	}
	
	public static void save(Image image, String format, File file) throws IOException {
		if(format.equalsIgnoreCase("ppm"))
			savePpm(image, file);
		else
			javax.imageio.ImageIO.write(image.asBufferedImage(), format, file);
	}
	
	private static void savePpm(Image image, File file) throws IOException {
		
	}

}
