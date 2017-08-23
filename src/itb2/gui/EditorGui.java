package itb2.gui;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.SpringLayout;

public class EditorGui extends JFrame {
	private static final long serialVersionUID = -1574070976560997812L;
	private static final int DISTANCE_WALL = 10, DISTANCE_OTHER = 6;
	private static final String TITLE = "ImageToolBox²";
	private final Workbench workbench;
	private final ImageList imageList;
	private final FilterList filterList;
	private final FilterProperties filterProperties;
	
	public EditorGui() {
		super(TITLE);
		
		// Initialize objects
		workbench = new Workbench(this);
		imageList = new ImageList(this);
		filterList = new FilterList(this);
		filterProperties = new FilterProperties(this);
		
		// Add objects to content pane
		Container contentPane = getContentPane();
		contentPane.add(workbench);
		contentPane.add(imageList);
		contentPane.add(filterList);
		contentPane.add(filterProperties);
		
		// Build layout
		SpringLayout layout = new SpringLayout();
		// - imageList
		layout.putConstraint(SpringLayout.NORTH, imageList, DISTANCE_WALL, SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.SOUTH, imageList, -DISTANCE_WALL, SpringLayout.SOUTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, imageList, DISTANCE_WALL, SpringLayout.WEST, contentPane);
		// - workbench
		layout.putConstraint(SpringLayout.NORTH, workbench, DISTANCE_WALL, SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.EAST, workbench, -DISTANCE_OTHER, SpringLayout.WEST, filterList);
		layout.putConstraint(SpringLayout.SOUTH, workbench, -DISTANCE_WALL, SpringLayout.SOUTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, workbench, DISTANCE_OTHER, SpringLayout.EAST, imageList);
		// - filterList
		layout.putConstraint(SpringLayout.NORTH, filterList, DISTANCE_WALL, SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.EAST, filterList, -DISTANCE_WALL, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.SOUTH, filterList, -DISTANCE_OTHER/2, SpringLayout.VERTICAL_CENTER, workbench);
		// - filterProperties
		layout.putConstraint(SpringLayout.NORTH, filterProperties, DISTANCE_OTHER/2, SpringLayout.VERTICAL_CENTER, workbench);
		layout.putConstraint(SpringLayout.EAST, filterProperties, -DISTANCE_WALL, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.SOUTH, filterProperties, -DISTANCE_WALL, SpringLayout.SOUTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, filterProperties, 0, SpringLayout.WEST, filterList);
		
		getContentPane().setLayout(layout);
		
		
		pack();
		setSize(800, 600);
		setLocationRelativeTo(null);
	}
	
	@Override
	public void setTitle(String title) {
		if(title == null || title.isEmpty())
			super.setTitle(TITLE);
		else
			super.setTitle(TITLE + " - " + title);
	}

}
