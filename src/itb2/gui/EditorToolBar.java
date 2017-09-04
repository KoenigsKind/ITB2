package itb2.gui;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import itb2.gui.icons.IconLoader;

public class EditorToolBar extends JToolBar {
	private static final long serialVersionUID = -3053195396175146234L;
	private final JButton openImage, saveImage, closeImage;
	private final JButton openFilter, runFilter, closeFilter;
	private final JButton resetZoom, fitToScreen;

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
	
	private JButton getButton(String file, String tooltip) {
		JButton button = new JButton();
		button.setBorder(null);
		button.setToolTipText(tooltip);
		
		Icon icon = IconLoader.getIcon(file, tooltip);
		if(icon != null)
			button.setIcon(icon);
		else
			button.setText(tooltip);
		
		return button;
	}

}
