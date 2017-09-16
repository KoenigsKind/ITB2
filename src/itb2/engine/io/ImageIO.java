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
		int minCol = raster.getMinX(), minRow = raster.getMinY();
		
		Image image = new RgbImage(width, height);
		image.setName(file);
		
		double[] rgb = new double[3];
		for(int col = 0; col < width; col++) {
			for(int row = 0; row < height; row++) {
				raster.getPixel(minCol + col, minRow + row, rgb);
				image.setValue(row, col, rgb);
			}
		}
		
		return image;
	}
	
	public static void save(Image image, String format, File file) throws IOException {
		javax.imageio.ImageIO.write(image.asBufferedImage(), format, file);
	}
}
