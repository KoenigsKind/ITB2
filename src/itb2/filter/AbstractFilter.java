package itb2.filter;

import java.util.ArrayList;
import java.util.List;

import itb2.engine.Controller;
import itb2.image.Image;

public abstract class AbstractFilter implements Filter {
	protected FilterProperties properties = new FilterProperties();

	@Override
	public FilterProperties getProperties() {
		return properties;
	}
	
	@Override
	public Image[] filter(Image[] input) {
		List<Image> filteredImages = new ArrayList<>(input.length);
		for(Image image : input){
			Image filtered = filter(image);
			if(filtered != null)
				filteredImages.add(filtered);
		}
		return filteredImages.toArray(new Image[0]);
	}
	
	public Image filter(Image input) {
		return null;
	}
	
	public Image callFilter(String name, Image image) {
		Image[] filtered = callFilter(name, new Image[]{image});
		if(filtered == null || filtered.length == 0)
			return null;
		return filtered[0];
	}
	
	public Image[] callFilter(String name, Image image[]) {
		Filter filter = Controller.getFilterManager().getFilter(name);
		if(filter == null)
			return null;
		return filter.filter(image);
	}
	
}
