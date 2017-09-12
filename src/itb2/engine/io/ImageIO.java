package itb2.engine.io;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import itb2.image.Image;
import itb2.image.RgbImage;

public class ImageIO {
	
	public static Image load(File file) throws IOException {
		BufferedImage bufferedImage = javax.imageio.ImageIO.read(file);
		Raster raster = bufferedImage.getData();
		int width = raster.getWidth(), height = raster.getHeight();
		double[][][] data = new double[width][height][3];
		double[] rgb = new double[3];
		for(int x = raster.getMinX(); x < width; x++) {
			for(int y = raster.getMinY(); y < height; y++) {
				raster.getPixel(x, y, rgb);
				for(int c = 0; c < 3; c++)
					data[x][y][c] = rgb[c];
			}
		}
		Image image = new RgbImage(data);
		image.setName(file);
		return image;
	}
	
	public static void save(Image image, String format, File file) throws IOException {
		javax.imageio.ImageIO.write(image.asBufferedImage(), format, file);
	}
}
