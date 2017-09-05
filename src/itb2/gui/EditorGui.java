package itb2.gui;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import itb2.engine.Controller;
import itb2.filter.Filter;
import itb2.image.Image;

public class EditorGui extends JFrame {
	private static final long serialVersionUID = -1574070976560997812L;
	private static final int DEFAULT_WIDTH = 800, DEFAULT_HEIGHT = 600;
	protected static final String TITLE = "ImageToolBox²";
	private static final int OPEN = 1, SAVE = 2;
	private final Workbench workbench;
	private final ImageList imageList;
	private final FilterList filterList;
	private final FilterProperties filterProperties;
	private final EditorMenuBar menubar;
	private final EditorToolBar toolbar;
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
		
//		JPanel filterBoard = new JPanel(new BorderLayout());
//		filterBoard.add(filterList, BorderLayout.CENTER);
//		filterBoard.add(filterProperties, BorderLayout.SOUTH);
		
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
	
	public void closeFilter() {
		Filter filter = filterList.getSelectedFilter();
		if(filter != null)
			Controller.getFilterManager().getFilters().remove(filter);
	}
	
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
	
	public void saveImage() {
		JFileChooser fileChooser = getImageChooser(SAVE);
		if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				// TODO Save image
				throw new IOException();
			} catch(IOException e) {
				Controller.getCommunicationManager().error("Could not open file:\n%s", file.getAbsolutePath());
			}
		}
	}
	
	private JFileChooser getImageChooser(int mode) {
		if(imageChooser == null)
			imageChooser = new JFileChooser(".");
		
		switch(mode) {
			case OPEN:
				imageChooser.setDialogTitle("Open Image");
				imageChooser.setMultiSelectionEnabled(true);
				imageChooser.setFileFilter(new FileNameExtensionFilter("Image", "jpg", "jpeg", "png", "bmp", "ppm"));
				break;
			case SAVE:
				imageChooser.setDialogTitle("Save Image");
				imageChooser.setMultiSelectionEnabled(false);
				//TODO Set file filter
				break;
			default:
				throw new RuntimeException("Unknown mode");
		}
		
		return imageChooser;
	}
	
	public void closeImage() {
		List<Image> selectedImages = imageList.getSelectedImages();
		for(Image image : selectedImages)
			Controller.getImageManager().getImageList().remove(image);
	}
	
	public void runFilter() {
		Filter filter = filterList.getSelectedFilter();
		if(filter == null)
			return;
		
		List<Image> selectedImages = imageList.getSelectedImages();
		Image[] images = selectedImages.toArray(new Image[selectedImages.size()]);
		
		images = Controller.getFilterManager().callFilter(filter, images);
		Controller.getImageManager().getImageList().addAll(Arrays.asList(images));
	}
	
	public void resetZoom() {
		workbench.resetZoom();
	}
	
	public void fitToScreen() {
		workbench.fitToScreen();
	}

}
