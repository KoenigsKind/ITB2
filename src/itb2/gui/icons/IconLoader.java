package itb2.gui.icons;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconLoader { //TODO Get better icons :D
	public static final String FILTER_OPEN = "filterOpen.png", FILTER_RUN = "filterRun.png", FILTER_CLOSE = "filterClose.png";
	public static final String IMAGE_OPEN = "imageOpen.png", IMAGE_SAVE = "imageSave.png", IMAGE_CLOSE = "imageClose.png";
	public static final String RESET_ZOOM = "resetZoom.png", FIT_TO_SCREEN = "fitToScreen.png";
	
	public static Icon getIcon(String file, String description) {
		URL url = IconLoader.class.getResource(file);
		return new ImageIcon(url, description);
	}

}
