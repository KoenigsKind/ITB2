package itb2.engine;

import java.io.File;
import java.io.IOException;

import itb2.data.ObservableLinkedList;
import itb2.image.Image;

public interface ImageManager {
	
	public ObservableLinkedList<Image> getImageList();
	
	public Image loadImage(File file) throws IOException;
	
}
