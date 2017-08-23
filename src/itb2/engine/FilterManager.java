package itb2.engine;

import java.io.File;
import java.io.IOException;

import itb2.data.ObservableTreeSet;
import itb2.filter.Filter;
import itb2.image.Image;

public interface FilterManager {
	
	public Filter loadFilter(File file) throws IOException;
	
	public ObservableTreeSet<Filter> getFilters();
	
	public Filter getFilter(String clazz);
	
	public Image[] callFilter(Filter filter, Image... images);
	
}
