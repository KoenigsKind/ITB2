package itb2.gui;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import itb2.engine.Controller;
import itb2.engine.io.ImageIO;
import itb2.filter.Filter;
import itb2.image.Image;

/**
 * Main-GUI
 * 
 * @author Micha Strauch
 */
public class EditorGui extends JFrame {
	private static final long serialVersionUID = -1574070976560997812L;
	
	/** Default width and height of window */
	private static final int DEFAULT_WIDTH = 800, DEFAULT_HEIGHT = 600;
	
	/** Title of the GUI */
	protected static final String TITLE = "ImageToolBox²";
	
	/** Parameter for {@link #getImageChooser(int)} */
	private static final int OPEN = 1, SAVE = 2;
	
	/** Workbench, display the selected image */
	private final Workbench workbench;
	
	/** List of loaded images */
	private final ImageList imageList;
	
	/** List of loaded filters */
	private final FilterList filterList;
	
	/** Properties for selected filter */
	private final FilterProperties filterProperties;
	
	/** This GUI's menu bar */
	private final EditorMenuBar menubar;
	
	/** This GUI's toolbar */
	private final EditorToolBar toolbar;
	
	/** FileChooser for Filter and Images */
	private JFileChooser filterChooser, imageChooser;
	
	public EditorGui() {
		super(TITLE);
		
		// Initialize objects
		imageList = new ImageList();
		workbench = new Workbench(imageList);
		filterList = new FilterList(this);
		filterProperties = new FilterProperties(filterList);
		menubar = new EditorMenuBar(this);
		toolbar = new EditorToolBar(this);
		
		// Add menu to window
		setJMenuBar(menubar);
		
		// Add objects to contentPane
		JPanel imageBoard = new JPanel(new BorderLayout());
		imageBoard.add(imageList, BorderLayout.WEST);
		imageBoard.add(workbench, BorderLayout.CENTER);
		
		JSplitPane filterPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, filterList, filterProperties);
		filterPane.setResizeWeight(0.5);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, imageBoard, filterPane);
		splitPane.setDividerLocation(DEFAULT_WIDTH - 200);
		splitPane.setResizeWeight(1);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(toolbar, BorderLayout.NORTH);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		
		pack();
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocationRelativeTo(null);
	}
	
	@Override
	public void setTitle(String title) {
		if(title == null || title.isEmpty())
			super.setTitle(TITLE);
		else
			super.setTitle(TITLE + " - " + title);
	}
	
	/** Let's the user open a filter */
	public void openFilter() {
		JFileChooser fileChooser = getFilterChooser();
		if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File[] files = fileChooser.getSelectedFiles(); 
			int index = 0;
			try {
				for(; index < files.length; index++)
					Controller.getFilterManager().loadFilter(files[index]);
			} catch(IOException e) {
				e.printStackTrace();
				Controller.getCommunicationManager().error("Could not open file:\n%s", files[index].getAbsolutePath());
			}
		}
	}
	
	/** Returns the file chooser for opening filter */
	private JFileChooser getFilterChooser() {
		if(filterChooser == null) {
			filterChooser = new JFileChooser(".");
			filterChooser.setDialogTitle("Open Filter");
			filterChooser.setMultiSelectionEnabled(true);
			filterChooser.setFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return "Filter";
				}
				
				@Override
				public boolean accept(File f) {
					String name = f.getName();
					return name.endsWith(".class") || name.endsWith(".java") || f.isDirectory(); 
				}
			});
		}
		return filterChooser;
	}
	
	/** Closes the currently selected filter */
	public void closeFilter() {
		Filter filter = filterList.getSelectedFilter();
		if(filter != null)
			Controller.getFilterManager().getFilters().remove(filter);
	}
	
	/** Let's the user open an image */
	public void openImage() {
		JFileChooser fileChooser = getImageChooser(OPEN);
		if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File[] files = fileChooser.getSelectedFiles(); 
			int index = 0;
			try {
				for(; index < files.length; index++)
					Controller.getImageManager().loadImage(files[index]);
			} catch(IOException e) {
				Controller.getCommunicationManager().error("Could not open file:\n%s", files[index].getAbsolutePath());
			}
		}
	}
	
	/** Let's the user save the currently selected image */
	public void saveImage() {
		// Get selected image
		List<Image> images = imageList.getSelectedImages();
		
		if(images.size() != 1) {
			if(images.size() > 1)
				Controller.getCommunicationManager().warning("Please select only one image for saving!");
			return;
		}
		
		Image image = images.get(0);
		
		// Request file to save to
		JFileChooser fileChooser = getImageChooser(SAVE);
		File file = null;
		FileFilter filter = null;
		
		do {
			if(fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
				break;
			
			file = fileChooser.getSelectedFile();
			filter = fileChooser.getFileFilter();
			
			if(!file.exists())
				break;
			
			// If file exists > overwrite? 
			int answer = JOptionPane.showConfirmDialog(this, "Do you want to overwrite:\n" + file.getAbsolutePath(),
					"File exists", JOptionPane.QUESTION_MESSAGE & JOptionPane.YES_NO_CANCEL_OPTION);
			if(answer == JOptionPane.YES_OPTION)
				break;
			
			// Do not overwrite
			file = null;
			
			if(answer == JOptionPane.CANCEL_OPTION)
				break;
			
			// Ask again
			
		} while(true);
		
		// Exit on cancel
		if(file == null)
			return;
		
		// Save image
		try {
			
			if(filter instanceof FileNameExtensionFilter) {
				String format = ((FileNameExtensionFilter) filter).getExtensions()[0];
				ImageIO.save(image, format, file);
			} else {
				// Try to guess format
				ImageIO.save(image, file);
			}
			
		} catch(IOException e) {
			Controller.getCommunicationManager().error("Could not save image to:\n%s", file.getAbsolutePath());
		}
	}
	
	/**
	 * Returns the file chooser for opening/saving images
	 * 
	 * @param mode Either {@link #OPEN} or {@link #SAVE}
	 */
	private JFileChooser getImageChooser(int mode) {
		if(imageChooser == null)
			imageChooser = new JFileChooser(".");
		
		imageChooser.resetChoosableFileFilters();
		
		switch(mode) {
			case OPEN:
				imageChooser.setDialogTitle("Open Image");
				imageChooser.setMultiSelectionEnabled(true);
				imageChooser.setFileFilter(new FileNameExtensionFilter("Image", "jpg", "jpeg", "png", "bmp"));
				break;
			case SAVE:
				imageChooser.setDialogTitle("Save Image");
				imageChooser.setMultiSelectionEnabled(false);
				
				imageChooser.removeChoosableFileFilter(imageChooser.getAcceptAllFileFilter());
				imageChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG", "png"));
				imageChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPG", "jpg", "jpeg"));
				imageChooser.addChoosableFileFilter(new FileNameExtensionFilter("BMP", "bmp"));
				break;
			default:
				throw new RuntimeException("Unknown mode");
		}
		
		return imageChooser;
	}
	
	/** Closes the currently selected images */
	public void closeImage() {
		List<Image> selectedImages = imageList.getSelectedImages();
		for(Image image : selectedImages)
			Controller.getImageManager().getImageList().remove(image);
	}
	
	/** Runs the selected filter using the selected images */
	public void runFilter() {
		Filter filter = filterList.getSelectedFilter();
		if(filter == null)
			return;
		
		try {
			List<Image> selectedImages = imageList.getSelectedImages();
			Image[] images = selectedImages.toArray(new Image[selectedImages.size()]);
			
			images = Controller.getFilterManager().callFilter(filter, images);
			Controller.getImageManager().getImageList().addAll(Arrays.asList(images));
		} catch(Exception e) {
			e.printStackTrace();
			Controller.getCommunicationManager().error("Error occured while running '%s': %s",
					filter.getClass().getName(), e.getMessage());
		}
	}
	
	/** Resets the zoom on the workbench */
	public void resetZoom() {
		workbench.resetZoom();
	}
	
	/** Sets the zoom on the workbench to fit the image to the screen */
	public void fitToScreen() {
		workbench.fitToScreen();
	}

}
