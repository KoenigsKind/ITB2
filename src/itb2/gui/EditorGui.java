package itb2.gui;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileFilter;

import itb2.engine.Controller;
import itb2.filter.Filter;

public class EditorGui extends JFrame {
	private static final long serialVersionUID = -1574070976560997812L;
	private static final int DEFAULT_WIDTH = 800, DEFAULT_HEIGHT = 600;
	private static final String TITLE = "ImageToolBox²";
	private final Workbench workbench;
	private final ImageList imageList;
	private final FilterList filterList;
	private final FilterProperties filterProperties;
	private JFileChooser filterChooser;
	
	public EditorGui() {
		super(TITLE);
		
		// Initialize objects
		workbench = new Workbench();
		imageList = new ImageList();
		filterList = new FilterList(this);
		filterProperties = new FilterProperties(filterList);
		
		// Add objects to contentPane
		JPanel imageBoard = new JPanel(new BorderLayout());
		imageBoard.add(imageList, BorderLayout.WEST);
		imageBoard.add(workbench, BorderLayout.CENTER);
		
		JPanel filterBoard = new JPanel(new BorderLayout());
		filterBoard.add(filterList, BorderLayout.CENTER);
		filterBoard.add(filterProperties, BorderLayout.SOUTH);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, imageBoard, filterBoard);
		splitPane.setDividerLocation(DEFAULT_WIDTH - 200);
		getContentPane().add(splitPane);
		
		// TODO Find a way to keep right side of splitpane same size, when resizing window
		
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
		if(filterChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File[] files = filterChooser.getSelectedFiles(); 
			int index = 0;
			try {
				for(; index < files.length; index++)
					Controller.getFilterManager().loadFilter(files[index]);
			} catch(IOException e) {
				Controller.getCommunicationManager().warning("Could not open file:\n%s", files[index].getAbsolutePath());
			}
		}
	}
	
	public void closeFilter() {
		Filter filter = filterList.getSelectedFilter();
		Controller.getFilterManager().getFilters().remove(filter);
	}

}
