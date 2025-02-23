package itb2.engine;

import java.io.File;
import java.io.IOException;

import itb2.data.ObservableLinkedList;
import itb2.engine.io.ImageIO;
import itb2.image.Image;

public class DefaultImageManager implements ImageManager {
	private ObservableLinkedList<Image> imageList;
	
	DefaultImageManager() {
		imageList = new ObservableLinkedList<>();
	}
	
	@Override
	public ObservableLinkedList<Image> getImageList() {
		return imageList;
	}
	
	@Override
	public Image loadImage(File file) throws IOException {
		Image image = ImageIO.load(file);
		imageList.add(image);
		return image;
	}
	
}
