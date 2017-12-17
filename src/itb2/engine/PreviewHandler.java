package itb2.engine;

import itb2.image.Image;

public interface PreviewHandler {
	
	public void preview(String message, Image image);
	
	public void close();

}
