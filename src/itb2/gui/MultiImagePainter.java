package itb2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import itb2.image.Image;

class MultiImagePainter extends JPanel {
	private static final long serialVersionUID = 5176193091585415854L;
	private final DefaultListModel<Image> model;
	private final JList<Image> list;
	
	public MultiImagePainter() {
		model = new DefaultListModel<>();
		
		list = new JList<>(model);
		list.setCellRenderer(new ImageRenderer());
		list.setBackground(GuiConstants.WORKBENCH_BACKGROUND);
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		list.setBorder(new EmptyBorder(10, 10, 10, 10));
		list.setVisibleRowCount(-1);
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	void setImages(List<Image> images) {
		model.clear();
		for(Image image : images)
			model.addElement(image);
	}
	
	void setImages(Image... images) {
		model.clear();
		for(Image image : images)
			model.addElement(image);
	}
	
	class ImageRenderer extends JLabel implements ListCellRenderer<Image> {
		private static final long serialVersionUID = -5400394083339787019L;
		Image image;
		boolean isSelected;
		
		public ImageRenderer() {
			setOpaque(false);
			setPreferredSize(new Dimension(200, 200));
			setBorder(new EmptyBorder(5, 5, 5, 5));
		}
		
		@Override
		public Component getListCellRendererComponent(JList<? extends Image> list, Image value, int index, boolean isSelected, boolean cellHasFocus) {
			this.image = value;
			this.isSelected = isSelected;
			return this;
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			double width = getWidth(), height = getHeight();
			Insets insets = getInsets();
			if(insets != null) {
				width -= insets.left + insets.right;
				height -= insets.top + insets.bottom;
			}
			
			double zoomHor = width / image.getWidth();
			double zoomVer = height / image.getHeight();
			double zoom = zoomHor < zoomVer ? zoomHor : zoomVer;
			
			int imgWidth = (int)(image.getWidth() * zoom);
			int imgHeight = (int)(image.getHeight() * zoom);
			
			int x = imgWidth < getWidth() ? (getWidth() - imgWidth) / 2 : 0;
			int y = imgHeight < getHeight() ? (getHeight() - imgHeight) / 2 : 0;
			
			g.drawImage(image.asBufferedImage(), x, y, imgWidth, imgHeight, null);
			
			if(isSelected) {
				g.setColor(Color.BLACK);
				g.drawRect(x, y, imgWidth, imgHeight);
			}
		}
		
	}
	
}
