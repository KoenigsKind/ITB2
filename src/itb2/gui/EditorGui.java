package itb2.gui;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class EditorGui extends JFrame {
	private static final long serialVersionUID = -1574070976560997812L;
	private static final int DEFAULT_WIDTH = 800, DEFAULT_HEIGHT = 600;
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
		splitPane.setDividerLocation(DEFAULT_WIDTH - 200);
		getContentPane().add(splitPane);
		
		/* TODO Find a way to keep right side of splitpane same size, when resizing window
		addComponentListener(new ComponentListener() {
			private int oldWidth = -1;
			
			@Override
			public void componentResized(ComponentEvent e) {
				if(oldWidth > 0) {
					int divider = splitPane.getDividerLocation();
					divider += getWidth() - oldWidth;
					splitPane.setDividerLocation(divider);
				}
				oldWidth = getWidth();
			}
			
			@Override public void componentShown(ComponentEvent e) {}
			@Override public void componentMoved(ComponentEvent e) {}
			@Override public void componentHidden(ComponentEvent e) {}
		});*/
		
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

}
