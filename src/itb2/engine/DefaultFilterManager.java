package itb2.engine;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import itb2.data.ObservableTreeSet;
import itb2.engine.io.FilterIO;
import itb2.engine.io.FilterWrapper;
import itb2.filter.Filter;
import itb2.filter.RequireImageType;
import itb2.image.Image;
import itb2.image.ImageConverter;

/**
 * Implementation of the {@link FilterManager}
 * 
 * @author Micha Strauch
 */
public class DefaultFilterManager implements FilterManager {
	
	/** Set of loaded filters */
	private final ObservableTreeSet<Filter> filterSet;
	
	/** Runs filter on different thread, is used in {@link #callFilter(Filter, Image[], Consumer)} */
	private final ExecutorService executor;
	
	/** Constructor */
	DefaultFilterManager() {
		executor = Executors.newSingleThreadExecutor();
		filterSet = new ObservableTreeSet<>(new Comparator<Filter>() {
			Collator collator = Collator.getInstance();
			
			public int compare(Filter a, Filter b) {
				Class<?> aClass = a instanceof FilterWrapper ? ((FilterWrapper)a).getWrappedClass() : a.getClass();
				Class<?> bClass = b instanceof FilterWrapper ? ((FilterWrapper)b).getWrappedClass() : b.getClass();
				
				return collator.compare(aClass.getSimpleName(), bClass.getSimpleName());
			};
		});
	}
	
	@Override
	public Filter loadFilter(File file) throws IOException {
		Filter filter = FilterIO.load(file);
		if(filter != null)
			filterSet.add(filter);
		return filter;
	}
	
	@Override
	public ObservableTreeSet<Filter> getFilters() {
		return filterSet;
	}
	
	@Override
	public Filter getFilter(String className) {
		for(Filter filter : filterSet)
			if(filter.getClass().getSimpleName().equals(className))
				return filter;
		return null;
	}
	
	@Override
	public Image[] callFilter(Filter filter, Image... images) {
		Class<? extends Image> requiredImageType = getRequiredImageType(filter);
		if(requiredImageType != null) {
			for(int i = 0; i < images.length; i++)
				images[i] = ImageConverter.convert(images[i], requiredImageType);
		}
		
		long startTime = System.currentTimeMillis();
		
		Image[] output = filter.filter(images);
		
		for(Image image : output) {
			if(image.getName() == null)
				image.setName(filter.getClass().getSimpleName());
		}
		
		double duration = (System.currentTimeMillis() - startTime) / 1000.;
		Controller.getCommunicationManager().info("Filter '%s' finished after %.2f seconds.",
				filter.getClass().getSimpleName(), duration);
		
		return output;
	}
	
	/**
	 * Returns the required image type for this filter, or null if not specified
	 * 
	 * @param filter Filter to get image type for
	 * @return Required image type
	 */
	private Class<? extends Image> getRequiredImageType(Filter filter) {
		RequireImageType require = filter.getClass().getAnnotation(RequireImageType.class);
		return require == null ? null : require.value();
	}

	@Override
	public void callFilter(Filter filter, Image[] images, Consumer<Image[]> receiver) {
		executor.submit(() -> {
			try {
				receiver.accept(callFilter(filter, images));
			} catch(Exception e) {
				e.printStackTrace();
				Controller.getCommunicationManager().error("Error occured while running '%s': %s",
						filter.getClass().getName(), e.getMessage());
				receiver.accept(null);
			}
		});
	}

}
