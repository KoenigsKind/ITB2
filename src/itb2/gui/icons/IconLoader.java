package itb2.gui.icons;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import itb2.gui.ImageFrame;

/**
 * Class for loading icons
 * 
 * @author Micha Strauch
 */
public class IconLoader {
	
	/** Icons for filter actions */
	public static final String FILTER_OPEN = "filterOpen.png", FILTER_RUN = "filterRun.png", FILTER_CLOSE = "filterClose.png";
	
	/** Icons for image actions */
	public static final String IMAGE_OPEN = "imageOpen.png", IMAGE_SAVE = "imageSave.png", IMAGE_CLOSE = "imageClose.png";
	
	/** Icons for zooming actions */
	public static final String RESET_ZOOM = "resetZoom.png", FIT_TO_SCREEN = "fitToScreen.png";
	
	/** Icons for {@link ImageFrame} */
	public static  final String IMAGE = "image.png", HISTOGRAM = "histogram.png";
	
	/** Icon for log */
	public static final String LOG = "log.png";
	
	/**
	 * Loads and returns the icon with the given name
	 * 
	 * @param name        Name of the icon
	 * @param description Description of the icon
	 * @return Loaded icon
	 */
	public static Icon getIcon(String name, String description) {
		URL url = IconLoader.class.getResource(name);
		return new ImageIcon(url, description);
	}

}
