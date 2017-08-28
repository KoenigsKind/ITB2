package itb2.filter;

import itb2.image.Image;

public interface Filter {
	
	public Image[] filter(Image[] input);
	
	public FilterProperties getProperties();

}
