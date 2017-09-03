package itb2.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import itb2.image.Image;

public class Workbench extends JPanel {
	private static final long serialVersionUID = 1991777977948041657L;
	private final ImageList imageList;
	
	public Workbench(ImageList imageList) {
		this.imageList = imageList;
		setBackground(Color.RED);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Image image = imageList.getSelectedImage();
		if(image != null)
			g.drawImage(image.asBufferedImage(), 0, 0, getWidth(), getHeight(), null);
	}
	
}
