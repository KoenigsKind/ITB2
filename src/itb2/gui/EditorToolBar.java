package itb2.gui;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import itb2.gui.icons.IconLoader;

/**
 * Toolbar for {@link EditorGui} 
 * 
 * @author Micha Strauch
 */
public class EditorToolBar extends JToolBar {
	private static final long serialVersionUID = -3053195396175146234L;
	
	/** Buttons for image actions */
	private final JButton openImage, saveImage, closeImage;
	
	/** Buttons for filter actions */
	private final JButton openFilter, runFilter, closeFilter;
	
	/** Buttons for zoom actions */
	private final JButton resetZoom, fitToScreen;
	
	/** Button for log */
	private final JButton log;

	/** Constructs toolbar for given {@link EditorGui} */
	public EditorToolBar(EditorGui gui) {
		super(HORIZONTAL);
		setFloatable(false);
		
		openImage = getButton(IconLoader.IMAGE_OPEN, "Open image");
		openImage.addActionListener(e -> gui.openImage());

		saveImage = getButton(IconLoader.IMAGE_SAVE, "Save image");
		saveImage.addActionListener(e -> gui.saveImage());

		closeImage = getButton(IconLoader.IMAGE_CLOSE, "Close image");
		closeImage.addActionListener(e -> gui.closeImage());

		openFilter = getButton(IconLoader.FILTER_OPEN, "Open filter");
		openFilter.addActionListener(e -> gui.openFilter());

		runFilter = getButton(IconLoader.FILTER_RUN, "Run filter");
		runFilter.addActionListener(e -> gui.runFilter());

		closeFilter = getButton(IconLoader.FILTER_CLOSE, "Close filter");
		closeFilter.addActionListener(e -> gui.closeFilter());
		
		resetZoom = getButton(IconLoader.RESET_ZOOM, "Reset zoom");
		resetZoom.addActionListener(e -> gui.resetZoom());
		
		fitToScreen = getButton(IconLoader.FIT_TO_SCREEN, "Fit to screen");
		fitToScreen.addActionListener(e -> gui.fitToScreen());
		
		log = getButton(IconLoader.LOG, "Open log");
		log.addActionListener(e -> gui.toggleLogFrame());

		add(log);
		add(Box.createHorizontalGlue());
		add(openImage);
		add(saveImage);
		add(closeImage);
		addSeparator();
		add(openFilter);
		add(closeFilter);
		addSeparator();
		add(runFilter);
		add(Box.createHorizontalGlue());
		add(resetZoom);
		add(fitToScreen);
	}
	
	/**
	 * Creates a button with given icon and tooltip
	 * 
	 * @param iconName Name of the icon
	 * @param tooltip  Tooltip to show when hovering above image
	 * @return Created button
	 */
	private JButton getButton(String iconName, String tooltip) {
		JButton button = new JButton();
		button.setBorder(null);
		button.setToolTipText(tooltip);
		
		Icon icon = IconLoader.getIcon(iconName, tooltip);
		if(icon != null)
			button.setIcon(icon);
		else
			button.setText(tooltip);
		
		return button;
	}

}
