package itb2.filter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import itb2.data.LimitedList;
import itb2.image.Image;

public abstract class AbstractSequenceFilter extends AbstractFilter {
	private static final int MEMORY_SIZE = 20;
	private final LimitedList<Image> lastImages;
	private Image[] skippedImages;
	
	public AbstractSequenceFilter() {
		lastImages = new LimitedList<>(MEMORY_SIZE);
		skippedImages = new Image[0];
	}
	
	@Override
	public final Image[] filter(Image[] input) {
		if(input.length == 0)
			return new Image[0];
		
		skippedImages = Arrays.copyOf(input, input.length - 1);
		lastImages.addAll(Arrays.asList(input));
		
		Image filteredImage = filter(input[input.length - 1]);
		return new Image[]{filteredImage};
	}
	
	protected final List<Image> getLastImages() {
		return Collections.unmodifiableList(lastImages);
	}
	
	protected final Image[] getSkippedImages() {
		return skippedImages;
	}

}
