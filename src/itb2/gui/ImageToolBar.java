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
public class ImageToolBar extends JToolBar {
	private static final long serialVersionUID = -3053195396175146234L;
	
	/** Buttons for zoom actions */
	private final JButton resetZoom, fitToScreen;

	/** Constructs toolbar for given {@link EditorGui} */
	public ImageToolBar(ImageFrame imageFrame) {
		super(HORIZONTAL);
		setFloatable(false);
		
		resetZoom = getButton(IconLoader.RESET_ZOOM, "Reset zoom", KeyController.Shortcut.RESET_ZOOM);
		resetZoom.addActionListener(e -> imageFrame.resetZoom());
		
		fitToScreen = getButton(IconLoader.FIT_TO_SCREEN, "Fit to screen", KeyController.Shortcut.FIT_TO_SCREEN);
		fitToScreen.addActionListener(e -> imageFrame.fitToScreen());

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
	private JButton getButton(String iconName, String tooltip, KeyController.Shortcut shortcut) {
		if(shortcut != null)
			tooltip = String.format("<html>%s<br><i>%s</i></html>", tooltip, shortcut.description);
		
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
