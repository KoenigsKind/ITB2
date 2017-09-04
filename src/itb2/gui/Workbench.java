package itb2.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import itb2.image.Image;

public class Workbench extends JPanel {
	private static final long serialVersionUID = 1991777977948041657L;
	private final ImagePainter painter;
	private final DraggableScrollPane scrollPane;
	
	public Workbench(ImageList imageList) {
		painter = new ImagePainter();
		
		scrollPane = new DraggableScrollPane(painter);
		
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		
		imageList.addSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Image image = imageList.getSelectedImage();
				painter.setImage(image);
				painter.revalidate();
				painter.repaint();
			}
		});
	}
	
	private class ImagePainter extends JPanel {
		private static final long serialVersionUID = 642660245893976850L;
		private final Dimension dimension;
		private Image image;
		private double zoom;
		
		ImagePainter() {
			dimension = new Dimension();
			zoom = 1;
			setBackground(GuiConstants.WORKBENCH_BACKGROUND);
			
			addMouseWheelListener(new MouseWheelListener() {
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					double factor = Math.pow(1.1, e.getWheelRotation());
					zoom *= factor;
					updateSize();
					revalidate();
					repaint();
				}
			});
		}
		
		void setImage(Image image) {
			this.image = image;
			updateSize();
		}
		
		void updateSize() {
			dimension.height = (int)(image.getHeight() * zoom);
			dimension.width = (int)(image.getWidth() * zoom);
			
			setPreferredSize(dimension);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			int width = getWidth();
			int height = getHeight();
			
			int imgWidth = (int)(image != null ? image.getWidth() * zoom : 0);
			int imgHeight = (int)(image != null ? image.getHeight() * zoom : 0);
			
			int x = imgWidth > 0 && imgWidth < width ? (width - imgWidth) / 2 : 0;
			int y = imgHeight > 0 && imgHeight < height ? (height - imgHeight) / 2 : 0;
			
			if(image != null)
				g.drawImage(image.asBufferedImage(), x, y, imgWidth, imgHeight, null);
		}
	}
}
