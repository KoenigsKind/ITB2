package itb2.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;

import itb2.data.ObservableLinkedList;
import itb2.engine.Controller;
import itb2.image.Image;

/**
 * List of all opened images
 * 
 * @author Micha Strauch
 */
public class ImageList extends JPanel {
	private static final long serialVersionUID = -7831547959723130632L;
	
	/** Size of thumbnails */
	private static final int SIZE = 100;
	
	/** Preferred size, used in {@link #getPreferredSize()} */
	private final Dimension preferredSize;
	
	/** List with all images */
	private final JList<Image> imageList;
	
	/** Scrollpane for the list */
	private final JScrollPane scrollPane;
	
	/** SelectionModel keeping selection order */
	private final OrderedSelectionModel selectionModel;
	
	/**
	 * Constructor
	 * 
	 * @param gui EditorGui, used to center ImageFrames
	 */
	public ImageList(EditorGui gui) {
		preferredSize = new Dimension(SIZE, 0);
		selectionModel = new OrderedSelectionModel();
		
		imageList = new JList<>();
		imageList.setModel(new ImageModel());
		imageList.setSelectionModel(selectionModel);
		imageList.setTransferHandler(new ImageTransferHandler(imageList));
		imageList.setDragEnabled(true);
		imageList.setDropMode(DropMode.INSERT);
		imageList.setCellRenderer(new ImageRenderer());
		imageList.setBackground(GuiConstants.DEFAULT_BACKGROUND);
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(imageList);
		scrollPane.setBorder(null);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		imageList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() > 1) {
					int index = imageList.locationToIndex(e.getPoint());
					Image image = imageList.getModel().getElementAt(index);
					if(image != null)
						new ImageFrame(gui, image).setVisible(true);
				}
			}
		});
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
	
	@Override
	public Dimension getPreferredSize() {
		JScrollBar bar = scrollPane.getVerticalScrollBar();
		int barWidth = bar.getWidth();
		preferredSize.width = SIZE + barWidth;

		return preferredSize;
	}
	
	/**
	 * Returns a list of selected images
	 * 
	 * @return List of selected images
	 */
	public List<Image> getSelectedImages() {
		int[] selectedIndexes = selectionModel.getSelectedIndexes();
		List<Image> images = Controller.getImageManager().getImageList();
		
		List<Image> selectedImages = new ArrayList<>();
		
		for(int index : selectedIndexes)
			selectedImages.add(images.get(index));
		
		return selectedImages;
	}
	
	/**
	 * Adds a selection listener
	 * 
	 * @param listener Selection listener to add
	 */
	public void addSelectionListener(ListSelectionListener listener) {
		imageList.addListSelectionListener(listener);
	}
	
	/**
	 * Renders the thumbnail for each image
	 * 
	 * @author Micha Strauch
	 */
	private class ImageRenderer extends JPanel implements ListCellRenderer<Image> {
		private static final long serialVersionUID = 2837759279638881717L;
		
		/** Border for selected and not selected images */
		private final Border selected, nonselected;
		
		/** Current image to render */
		private Image image;

		/** Constructor */
		public ImageRenderer() {
			setPreferredSize(new Dimension(SIZE, SIZE));
			setBackground(getBackground().darker());
			selected = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
			nonselected = null;
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends Image> list, Image value, int index, boolean isSelected, boolean cellHasFocus) {
			image = value;
			setBorder(isSelected ? selected : nonselected);
			setOpaque(cellHasFocus);
			
			String toolTip = value.getName() != null ? "<tr><th align='left'>Name</th><td>" + value.getName() + "</td></tr>" : "";
			toolTip += String.format("<tr><th align='left'>Type</th><td>%s</td></tr><tr><th align='left'>Size</th><td>%d x %d</td></tr>",
					value.getClass().getSimpleName(), value.getWidth(), value.getHeight());
			setToolTipText("<html><table>" + toolTip + "<table></html>");
			
			return this;
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			int size = SIZE - 10;
			double factor = (double) image.getWidth() / image.getHeight();
			int width = (int)(factor >= 1 ? size : size * factor);
			int height = (int)(factor <= 1 ? size : size / factor);
			
			int x = (SIZE - width) / 2;
			int y = (SIZE - height) / 2;
			
			g.drawImage(image.asBufferedImage(), x, y, width, height, null);
		}
		
	}
	
	/**
	 * ImageModel keeping track of currently opened images
	 * 
	 * @author Micha Strauch
	 */
	private class ImageModel extends AbstractListModel<Image> implements ListDataListener {
		private static final long serialVersionUID = -385609011901046837L;
		
		/** List of currently opened images */
		private final ObservableLinkedList<Image> images;
		
		/** Constructor */
		ImageModel() {
			images = Controller.getImageManager().getImageList();
			images.addListener(this);
		}

		@Override
		public Image getElementAt(int index) {
			try {
				return images.get(index);
			} catch(IndexOutOfBoundsException e) {
				return null;
			}
		}

		@Override
		public int getSize() {
			return images.size();
		}

		@Override
		public void contentsChanged(ListDataEvent e) {
			fireContentsChanged(this, e.getIndex0(), e.getIndex1());
		}

		@Override
		public void intervalAdded(ListDataEvent e) {
			fireIntervalAdded(this, e.getIndex0(), e.getIndex1());
		}

		@Override
		public void intervalRemoved(ListDataEvent e) {
			fireIntervalRemoved(this, e.getIndex0(), e.getIndex1());
		}
		
	}
}
