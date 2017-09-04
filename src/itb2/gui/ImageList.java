package itb2.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;

import itb2.data.ObservableLinkedList;
import itb2.engine.Controller;
import itb2.image.Image;

public class ImageList extends JPanel {
	private static final long serialVersionUID = -7831547959723130632L;
	private static final int SIZE = 100;
	private final Dimension preferredSize;
	private final JList<Image> imageList;
	private final JScrollPane scrollPane;
	
	public ImageList() {
		preferredSize = new Dimension(SIZE, 0);
		
		imageList = new JList<>();
		imageList.setModel(new ImageModel());
		imageList.setCellRenderer(new ImageRenderer());
		imageList.setBackground(GuiConstants.DEFAULT_BACKGROUND);
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(imageList);
		scrollPane.setBorder(null);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
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
	
	public Image getSelectedImage() {
		return imageList.getSelectedValue();
	}
	
	public void addSelectionListener(ListSelectionListener listener) {
		imageList.addListSelectionListener(listener);
	}
	
	private class ImageRenderer extends JPanel implements ListCellRenderer<Image> {
		private static final long serialVersionUID = 2837759279638881717L;
		private final Border selected, nonselected;
		private Image image;

		public ImageRenderer() {
			setPreferredSize(new Dimension(SIZE, SIZE));
			selected = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
			nonselected = null;
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends Image> list, Image value, int index, boolean isSelected, boolean cellHasFocus) {
			image = value;
			setBorder(isSelected ? selected : nonselected);
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
	
	private class ImageModel implements ListModel<Image> {
		private final ObservableLinkedList<Image> images;
		private final Set<ListDataListener> listeners;
		
		ImageModel() {
			images = Controller.getImageManager().getImageList();
			listeners = new HashSet<>();
			images.addListener(new ObservableLinkedList.ListListener<Image>() {

				@Override
				public void itemsChanged(ObservableLinkedList<Image> list, int type) {
					ListDataEvent event = new ListDataEvent(ImageModel.this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
					
					for(ListDataListener listener : listeners)
						listener.contentsChanged(event);
					
					ImageList.this.revalidate();
				}
				
			});
			
		}

		@Override
		public void addListDataListener(ListDataListener listener) {
			listeners.add(listener);
		}

		@Override
		public Image getElementAt(int index) {
			try {
				return images.get(index);
			} catch(ArrayIndexOutOfBoundsException e) {
				return null;
			}
		}

		@Override
		public int getSize() {
			return images.size();
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			listeners.remove(l);
		}
	}
}
