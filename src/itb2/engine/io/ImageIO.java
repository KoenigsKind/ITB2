package itb2.engine.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import itb2.image.Image;
import itb2.image.RgbImage;

public class ImageIO {
	
	public static Image load(File file) throws IOException {
		BufferedImage bufferedImage = javax.imageio.ImageIO.read(file);
		Image image = new RgbImage(bufferedImage);
		image.setName(file);
		return image;
	}
	
	public static void save(Image image, String format, File file) throws IOException {
		javax.imageio.ImageIO.write(image.asBufferedImage(), format, file);
	}
}
