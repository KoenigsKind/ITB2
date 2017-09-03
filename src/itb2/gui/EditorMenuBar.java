package itb2.gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class EditorMenuBar extends JMenuBar {
	private static final long serialVersionUID = -217686192194157463L;
	private final JMenu imageMenu, filterMenu;
	private final JMenuItem imageOpen, imageSave, imageClose;
	private final JMenuItem filterOpen, filterClose;
	
	public EditorMenuBar(EditorGui gui) {
		// Build menu items
		imageOpen = new JMenuItem("Open image");
		imageOpen.addActionListener(e -> gui.openImage());
		
		imageSave = new JMenuItem("Save image");
		imageSave.addActionListener(e -> gui.saveImage());
		
		imageClose = new JMenuItem("Close image");
		imageClose.addActionListener(e -> gui.closeImage());
		
		filterOpen = new JMenuItem("Open filter");
		filterOpen.addActionListener(e -> gui.openFilter());
		
		filterClose = new JMenuItem("Close filter");
		filterClose.addActionListener(e -> gui.closeFilter());
		
		// Build image menu
		imageMenu = new JMenu("Images");
		imageMenu.add(imageOpen);
		imageMenu.add(imageSave);
		imageMenu.add(imageClose);
		add(imageMenu);
		
		// Build filter menu
		filterMenu = new JMenu("Filter");
		filterMenu.add(filterOpen);
		filterMenu.add(filterClose);
		add(filterMenu);
	}

}
