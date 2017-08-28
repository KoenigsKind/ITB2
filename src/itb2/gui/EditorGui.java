package itb2.gui;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class EditorGui extends JFrame {
	private static final long serialVersionUID = -1574070976560997812L;
	private static final String TITLE = "ImageToolBox²";
	private final Workbench workbench;
	private final ImageList imageList;
	private final FilterList filterList;
	private final FilterProperties filterProperties;
	
	public EditorGui() {
		super(TITLE);
		
		// Initialize objects
		workbench = new Workbench();
		imageList = new ImageList();
		filterList = new FilterList();
		filterProperties = new FilterProperties(filterList);
		
		// Add objects to contentPane
		JPanel imageBoard = new JPanel(new BorderLayout());
		imageBoard.add(imageList, BorderLayout.WEST);
		imageBoard.add(workbench, BorderLayout.CENTER);
		
		JPanel filterBoard = new JPanel(new BorderLayout());
		filterBoard.add(filterList, BorderLayout.CENTER);
		filterBoard.add(filterProperties, BorderLayout.SOUTH);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, imageBoard, filterBoard);
		splitPane.setDividerLocation(0.5);
		getContentPane().add(splitPane);
		
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
